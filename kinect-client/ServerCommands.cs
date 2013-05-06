using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Microsoft.Samples.Kinect.DepthBasics
{
    class ServerCommands
    {
        // Command ids
        public const byte RSTATUS = (byte)0x06;
        public const byte RLOCK = (byte)0x08;
        public const byte RUNLOCK = (byte)0x09;
        public const byte CONNECT = (byte)0x0B;

        // Ranges for conversion of serialized angle to degrees
        private const int MIN_INT = -32768;
        private const int MAX_INT = 32767;
        private const double MIN_DEGREES = 0.0;
        private const double MAX_DEGREES = 359.99451;

        public static Vector2D ParseRStatusCommandPosition(byte[] bytes)
        {
            //X-coordinate
            int x = ((bytes[1] & 0xFE) << 7) ^ bytes[2];
            if ((bytes[1] & 0x01) > 0) x *= -1;
            //Y-coordinate
            int y = ((bytes[3] & 0xFE) << 7) ^ bytes[4];
            if ((bytes[3] & 0x01) > 0) y *= -1;

            return new Vector2D(x, y);
        }

        public static double ParseRStatusCommandAngle(byte[] bytes)
        {
            //Generates the serialized angle from the MSB and the LSB

            int serialAngle = ((bytes[6] << 8) ^ bytes[7]) + MIN_INT;
            //Convert serial angle to degrees
            double z = (serialAngle - (double)MIN_INT) / ((double)(MAX_INT - MIN_INT));

            return (z * (MAX_DEGREES - MIN_DEGREES)) + MIN_DEGREES;
        }

        public static byte[] getConnectMessage()
        {
            byte[] connectedMsg = new byte[2];
            connectedMsg[0] = ServerCommands.CONNECT;
            connectedMsg[1] = (byte)0x02;
            return connectedMsg;
        }

        public static byte[] getRUnlockMessage()
        {
            byte[] unlockMsg = new byte[1];
            unlockMsg[0] = ServerCommands.RUNLOCK;
            return unlockMsg;
        }
    }
}
