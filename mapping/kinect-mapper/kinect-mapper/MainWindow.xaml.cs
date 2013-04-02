

namespace kinect_mapper
{
    using System;
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
        /// <summary>
        /// Active Kinect sensor
        /// </summary>
        private KinectSensor sensor;

        /// <summary>
        /// Robot position
        /// </summary>
        private Vector2D rbotPos;

        /// <summary>
        /// Robot angle in radians
        /// </summary>
        private double rbotAngle;

        /// <summary>
        /// Intermediate storage for the depth data received from the camera
        /// </summary>
        private short[] depthPixels;

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
                this.depthPixels = new short[this.sensor.DepthStream.FramePixelDataLength];


                // Add an event handler to be called whenever there is new depth frame data
                this.sensor.DepthFrameReady += this.SensorDepthFrameReady;

                // Put the RBot at (0,0)

                rbotPos = new Vector2D(0, 0);

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
                //this.statusBarText.Text = Properties.Resources.NoKinectReady;
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
        }
        /// <summary>
        /// Event handler for Kinect sensor's DepthFrameReady event
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void SensorDepthFrameReady(object sender, DepthImageFrameReadyEventArgs e)
        {
            using (DepthImageFrame depthFrame = e.OpenDepthImageFrame())
            {
                if (depthFrame != null)
                {
                    // Copy the pixel data from the image to a temporary array
                    depthFrame.CopyPixelDataTo(this.depthPixels);

                    // Convert the depth to RGB
                    int width = 480;
                    int height = 640;
                    int start = width * (height / 2 - 1); //start at the beginning of the middle line
                    for (int i = 0; i < 480; ++i)
                    {
                        // discard the portion of the depth that contains only the player index
                        short depth = (short)(this.depthPixels[i + start] >> DepthImageFrame.PlayerIndexBitmaskWidth);
                        //find the angle to the left (if negative) or right (if positive) of the depth. The kinect's fov is 58
                        //so we halve it because it's going to be either left or right
                        double angle = (i - 240) / 29 * Math.PI / 180;
                        double xFromRBot = Math.Sin(angle) * depth;
                        double yFromRBot = Math.Cos(angle) * depth;
                        Vector2D posFromRBot = new Vector2D(xFromRBot, yFromRBot);
                        posFromRBot.translate(rbotPos);
                        posFromRBot.rotate(rbotAngle);
                        Vector2D absolutePos = posFromRBot;
                    }
                }
            }
        }
    }
}
