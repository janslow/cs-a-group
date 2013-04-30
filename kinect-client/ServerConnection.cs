using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;


namespace Microsoft.Samples.Kinect.DepthBasics
{
    class ServerConnection
    {
        private Socket socket;
        private bool connected = false;

        public ServerConnection(String address, String name)
        {
            Console.WriteLine("Attempting to connect to server...");
            socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            System.Net.IPAddress remoteIPAddress;
            try
            {
                remoteIPAddress = System.Net.IPAddress.Parse(address);

            }
            catch (FormatException e)
            {
                Console.WriteLine("Using \"localhost\" as IP address of server connection");
                remoteIPAddress = Dns.Resolve("localhost").AddressList[0];
            }
            System.Net.IPEndPoint remoteEndPoint = new System.Net.IPEndPoint(remoteIPAddress, 2003);
            try
            {
                socket.Connect(remoteEndPoint);
                Console.WriteLine("Connected to server!");
                connected = true;
            }
            catch (SocketException e)
            {
                Console.WriteLine("***SERVER CONNECTION FAILED***");
                Console.WriteLine(e.Message);
            }
        }

        public void close()
        {
            socket.Close();
            connected = false;
        }

        public void SendPacket(byte[] data)
        {
            if (socket.Connected)
            {
                socket.Send(data);
            }
        }

        public bool isConnected()
        {
            return connected;
        }

    }
}
