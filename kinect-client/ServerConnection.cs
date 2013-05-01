using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;


namespace Microsoft.Samples.Kinect.DepthBasics
{
    class ServerConnection
    {
        // Command ids
        private const byte RSTATUS = (byte)0x06;
        private const byte RLOCK = (byte)0x08;
        private const byte RUNLOCK = (byte)0x09;

        // Ranges for conversion of serialized angle to degrees
        private const double MIN_INT = -32767;
        private const double MAX_INT = 32767;
        private const double MIN_DEGREES = 0.0;
        private const double MAX_DEGREES = 359.98901;

        // State
        private MainWindow kinectMapping;
        private Socket socket;
        private bool connected = false;
        private Thread listeningThread;

        // Byte reading state
        private int i;
        private byte[] bytes;
        private int cmdLength = 1;

        public ServerConnection(String address, String name, MainWindow kinectMapping)
        {
            this.kinectMapping = kinectMapping;
            Console.WriteLine("Attempting to connect to server...");
            socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            System.Net.IPAddress remoteIPAddress;
            try
            {
                remoteIPAddress = System.Net.IPAddress.Parse(address);

            }
            catch (FormatException e)
            {
                Console.WriteLine(e.Message);
                Console.WriteLine("Using \"localhost\" as IP address of server connection");
                remoteIPAddress = Dns.Resolve("localhost").AddressList[0];
            }
            System.Net.IPEndPoint remoteEndPoint = new System.Net.IPEndPoint(remoteIPAddress, 2003);
            try
            {
                socket.Connect(remoteEndPoint);
                // create and send connected packet
                byte[] connectedMsg = new byte[2];
                connectedMsg[0] = (byte)0x0A;
                connectedMsg[1] = (byte)0x02;     
                SendPacket(connectedMsg);
                Console.WriteLine("Connected to server!");
                connected = true;

                // initialize server listening thread
                Console.WriteLine("Starting listener to server...");
                listeningThread = new Thread(run);
                listeningThread.Start();
                Console.WriteLine("Listening activated!");
            }
            catch (SocketException)
            {
                Console.WriteLine("***SERVER CONNECTION FAILED***");
            }
        }

        public void close()
        {
            socket.Close();
            connected = false;
            listeningThread.Abort();
        }

        public void SendPacket(byte[] data)
        {
            if (socket.Connected)
                socket.Send(data);
        }

        public bool isConnected()
        {
            return connected;
        }

        public void run()
        {
            byte[] buffer = new byte[1];
            bool skipByte = false;
            while (true)
            {
                try
                {
                    socket.Receive(buffer);
                }
                catch (SocketException)
                {
                    this.close();
                }
                Console.WriteLine("> IN: " + buffer[0]);
                if (i == cmdLength && !skipByte)
                {
                    // Just finished read and buffer contains first byte to drop
                    skipByte = true;
                }
                else
                {
                    if (skipByte)
                    {
                        // Finished read and buffer contains second byte to drop
                        skipByte = false;
                        i = 0;
                    }
                    else
                    {
                        // Forward byte to be parsed
                        Console.WriteLine("> NQ: " + buffer[0]);
                        enqueue(buffer);
                    }
                }
            }
        }

        public void enqueue(byte[] bs)
        {
            foreach (byte b in bs)
                enqueue(b);
        }

        public void enqueue(byte b)
        {
            if (bytes == null)
            {
                // new set of bytes to receive
                switch (b)
                {
                    case RSTATUS:
                        cmdLength = 8;
                        bytes = new byte[cmdLength];
                        bytes[0] = b;
                        i = 1;
                        break;
                    case RLOCK:
                        kinectMapping.updateReadyToMap(true);
                        cmdLength = 1;
                        i = 1;
                        break;
                    case RUNLOCK:
                        kinectMapping.updateReadyToMap(false);
                        cmdLength = 1;
                        i = 1;
                        break;
                    default:
                        Console.WriteLine("Invalid command read and ignored (" + b + ")"); return;
                }
            }
            else
            {
                bytes[i++] = b;
                if (i >= cmdLength)
                {
                    // Parse Status
                    Vector2D v = ParseRStatusCommandPosition(bytes);
                    double x = v.getX();
                    double y = v.getY();
                    double a = ParseRStatusCommandAngle(bytes);
                    kinectMapping.updateRbotPosition(x, y, a);
                    bytes = null;
                }  
            }
        }

        private Vector2D ParseRStatusCommandPosition(byte[] bytes)
        {
            //X-coordinate
            int x = ((bytes[1] & 0xFE) << 7) ^ bytes[2];
            if ((bytes[1] & 0x01) > 0) x *= -1;
            //Y-coordinate
            int y = ((bytes[3] & 0xFE) << 7) ^ bytes[4];
            if ((bytes[3] & 0x01) > 0) y *= -1;

            if ((bytes[7] & 0x01) == 0) x = -x;
            return new Vector2D(x, y);
        }

        private double ParseRStatusCommandAngle(byte[] bytes)
        {
            //Generates the serialized angle from the MSB and the LSB
            int serialAngle = (bytes[6] << 7) + (bytes[7] >> 1);
		    //Convert serial angle to degrees
		    double z = (serialAngle - MIN_INT) / (MAX_INT - MIN_INT);

		    return (z * (MAX_DEGREES - MIN_DEGREES)) + MIN_DEGREES;
        }
    }
}
