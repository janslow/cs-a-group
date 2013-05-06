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
        // Debug Network?
        private const bool debugNetwork = false;

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
                SendPacket(ServerCommands.getConnectMessage());
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
            {
                foreach (byte d in data)
                    if (debugNetwork)
                        Console.WriteLine(d);
                socket.Send(convertForSending(data));
            }
        }

        public bool isConnected()
        {
            return connected;
        }

        public void run()
        {
            byte[] buffer = new byte[1];
            while (true)
            {
                try
                {
                    socket.Receive(buffer);
                    buffer = convertOnReceive(buffer);
                }
                catch (SocketException)
                {
                    Console.WriteLine("*** Socket exception");
                    this.close();
                }
                if (debugNetwork)
                    Console.WriteLine("> IN: " + buffer[0]);
                enqueue(buffer);
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
                    case ServerCommands.RSTATUS:
                        cmdLength = 8;
                        bytes = new byte[cmdLength];
                        bytes[0] = b;
                        i = 1;
                        break;
                    case ServerCommands.RLOCK:
                        kinectMapping.updateReadyToMap(true);
                        cmdLength = 1;
                        i = 1;
                        break;
                    case ServerCommands.RUNLOCK:
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
                    Vector2D v = ServerCommands.ParseRStatusCommandPosition(bytes);
                    double x = v.getX();
                    double y = v.getY();
                    double a = ServerCommands.ParseRStatusCommandAngle(bytes) + 90.0;
                    kinectMapping.updateRbotPosition(x, y, a);
                    bytes = null;
                }  
            }
        }

        private byte[] convertForSending(byte[] bs)
        {
            for (int i = 0; i < bs.Count(); i++)
                bs[i] = convertForSending(bs[i]);
            return bs;
        }

        private byte convertForSending(byte b)
        {
            return (byte)((int)b - 127);
        }

        private byte[] convertOnReceive(byte[] bs)
        {
            for (int i = 0; i < bs.Count(); i++)
                bs[i] = convertOnReceive(bs[i]);
            return bs;
        }

        private byte convertOnReceive(byte b)
        {
            return (byte)((int)b + 127);
        }

    }
}
