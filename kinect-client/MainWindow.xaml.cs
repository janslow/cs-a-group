﻿//------------------------------------------------------------------------------
// <copyright file="MainWindow.xaml.cs" company="Microsoft">
//     Copyright (c) Microsoft Corporation.  All rights reserved.
// </copyright>
//------------------------------------------------------------------------------

namespace Microsoft.Samples.Kinect.DepthBasics
{
    using System;
    using System.Collections.Generic;
    using System.Diagnostics;
    using System.Globalization;
    using System.IO;
    using System.Windows;
    using System.Windows.Media;
    using System.Windows.Media.Imaging;
    using Microsoft.Kinect;

    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        ServerConnection server;

        List<List<Tuple<int, int>>> pointsToCheck = new List<List<Tuple<int, int>>>();
        
        bool readyToRead = false;
        int framesRead = 0;
        const int framesPerRead = 1;

        /// <summary>
        /// Robot position
        /// </summary>
        private Vector2D rbotPos;

        /// <summary>
        /// Robot angle in radians
        /// </summary>
        private double rbotAngle;

        /// <summary>
        /// Active Kinect sensor
        /// </summary>
        private KinectSensor sensor;

        /// <summary>
        /// Intermediate storage for the depth data received from the camera
        /// </summary>
        private DepthImagePixel[] depthPixels;
        
        /// <summary>
        /// Initializes a new instance of the MainWindow class.
        /// </summary>
        public MainWindow()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Execute startup tasks
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void WindowLoaded(object sender, RoutedEventArgs e)
        {
            Console.WriteLine();

            // Read from file the pre-computed list of points in the view of the Kinect sensor
            readPointsToCheck();

            // Setup server connection
            server = new ServerConnection("192.168.52.50", "Remote Kinect Client");
            
            // Look through all sensors and start the first connected one.
            // This requires that a Kinect is connected at the time of app startup.
            // To make your app robust against plug/unplug, 
            // it is recommended to use KinectSensorChooser provided in Microsoft.Kinect.Toolkit
            foreach (var potentialSensor in KinectSensor.KinectSensors)
            {
                if (potentialSensor.Status == KinectStatus.Connected)
                {
                    this.sensor = potentialSensor;
                    break;
                }
            }

            if (null != this.sensor)
            {
                // Turn on the depth stream to receive depth frames
                this.sensor.DepthStream.Enable(DepthImageFormat.Resolution640x480Fps30);
                
                // Allocate space to put the depth pixels we'll receive
                this.depthPixels = new DepthImagePixel[this.sensor.DepthStream.FramePixelDataLength];

                // Add an event handler to be called whenever there is new depth frame data
                this.sensor.DepthFrameReady += this.SensorDepthFrameReady;

                // Start the sensor!
                try
                {
                    this.sensor.Start();
                }
                catch (IOException)
                {
                    this.sensor = null;
                }
            }

            if (null == this.sensor)
            {
                this.statusBarText.Text = Properties.Resources.NoKinectReady;
            }
        }

        /// <summary>
        /// Execute shutdown tasks
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void WindowClosing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            if (null != this.sensor)
            {
                this.sensor.Stop();
            }
            if (server.isConnected())
            {
                server.close();
            }
        }

        /// <summary>
        /// Event handler for Kinect sensor's DepthFrameReady event
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void SensorDepthFrameReady(object sender, DepthImageFrameReadyEventArgs e)
        {
            readyToRead = true;     // temporary, needs to be set by the server when the robot stops
            using (DepthImageFrame depthFrame = e.OpenDepthImageFrame())
            {
                // Add guard Back:  && framesRead < framesPerRead
                if (readyToRead)
                {

                    if (depthFrame != null)
                    {
                        //TODO get robot position from the server (set to identities)
                        rbotPos = new Vector2D(0, 0);
                        rbotAngle = 90;

                        // Copy the pixel data from the image to a temporary array
                        depthFrame.CopyDepthImagePixelDataTo(this.depthPixels);

                        // Lists of vectors of free points in space and occupied points in space
                        List<Vector2D> freeVectors = new List<Vector2D>();
                        List<Vector2D> occupiedVectors = new List<Vector2D>();

                        // Frame dimensions
                        int width = 640;
                        int height = 480;

                        // Analyse the middle line
                        int start = width * (height / 2 - 1);

                        // Go through each pixel of the middle line
                        for (int i = 0; i < width; ++i)
                        {
                            // Get the depth corresponding to this pixel from the Kinect [in millimiters]
                            short depth = this.depthPixels[i + start].Depth;
                            double anglePerPixel = 58.0 / 640.0;
                            double angle = DegreesToRadians(119 - i * anglePerPixel);

                            // Project to obtain (x,y) coordinates relative to kinect
                            double xFromRBot = Math.Cos(angle) * depth;
                            double yFromRBot = Math.Sin(angle) * depth;
                            Vector2D posFromRBot = new Vector2D(xFromRBot, yFromRBot);

                            // Check range and add obstacle
                            if (1200 <= depth && depth <= 3000)
                                occupiedVectors.Add(posFromRBot);

                            // If obstacle within augmented accepted depth interval, then add free "line of sight"
                            if (1200 <= depth && depth <= 6000)
                            {
                                for (int k = 0; k < pointsToCheck[i].Count; ++k)
                                {
                                    Tuple<int, int> v = pointsToCheck[i][k];
                                    // convert to millimeters
                                    int x = v.Item1 * 10;
                                    int y = v.Item2 * 10;
                                    int distanceSq = x * x + y * y;

                                    if (distanceSq < depth * depth)
                                        freeVectors.Add(new Vector2D(x, y));
                                }
                            }
                        }

                        // Collate list of observations to send, transformed to global coordinates
                        List<Observation> observations = new List<Observation>();
                        foreach (Vector2D v in freeVectors)
                            observations.Add(Observation.vectorToObservation(v, false, rbotPos, rbotAngle, 50, 127));
                        foreach (Vector2D v in occupiedVectors)
                            observations.Add(Observation.vectorToObservation(v, true, rbotPos, rbotAngle, 50, 127));
                        Console.WriteLine();
                        Console.WriteLine("Observation set is generated @ " + System.DateTime.Now);

                        // Send observations
                        foreach (Observation o in observations)                     
                            server.SendPacket(o.serialize());
                        Console.WriteLine("Observation set packets sent @ " + System.DateTime.Now);
                    }
                    framesRead++;
                }
            }
        }

        private double DegreesToRadians(double angle)
        {
            return Math.PI * angle / 180.0;
        }
        private void readPointsToCheck()
        {
            List<Tuple<int, int>> listOfCoords = new List<Tuple<int, int>>();
            String sLine = "";
            StreamReader objReader = new StreamReader("points.txt");
            bool even = true;
            int x = 0;
            int y = 0;
            while (sLine != null)
            {

                sLine = objReader.ReadLine();

                if (sLine != null && sLine != "")
                {
                    if (sLine == "new")
                    {
                        pointsToCheck.Add(listOfCoords);
                        listOfCoords = new List<Tuple<int, int>>();
                        even = true;
                    }
                    else
                    {
                        if (even)
                        {
                            x = Convert.ToInt32(sLine);
                            even = false;
                        }
                        else
                        {
                            y = Convert.ToInt32(sLine);
                            Tuple<int, int> point = new Tuple<int, int>(x, y);
                            listOfCoords.Add(point);
                            even = true;
                        }
                    }
                }
            }
            objReader.Close();
        }
    }
}