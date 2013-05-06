using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Microsoft.Samples.Kinect.DepthBasics
{
    class Observation
    {
        private int x;
        private int y;
        private int certainty;
        private bool isOccupied;

        public static Observation vectorToObservation(Vector2D v, bool isOccupied, Vector2D rbotPos, double rbotAngle, int unitLength, int certainty)
        {
            v.rotate(rbotAngle - 90);
            v.translate(rbotPos);
            v.scale(1.0 / unitLength);

            return new Observation((int)v.getX(), (int)v.getY(), isOccupied, certainty);
        }

        public Observation(int x, int y, bool isOccupied, int certainty)
        {
            this.x = x;
            this.y = y;
            this.isOccupied = isOccupied;
            this.certainty = certainty;
        }

        public int getX()
        {
            return x;
        }
        public int getY()
        {
            return y;
        }
        public bool getIsOccupied()
        {
            return isOccupied;
        }
        public int getCertainty()
        {
            return certainty;
        }

        public byte[] serialize()
        {
            byte[] bytes = new byte[6];
            //Command Type
            bytes[0] = (byte)0x05;

            int x2 = Math.Abs(x), y2 = Math.Abs(y), c = certainty;
            bool x_negative = x < 0, y_negative = y < 0;

            //X coordinate
            bytes[1] = (byte)((0xFE & (x2 >> 7)) ^ (x_negative ? 0x01 : 0x00));
            bytes[2] = (byte)(0xFF & x2);
            //Y coordinate
            bytes[3] = (byte)((0xFE & (y2 >> 7)) ^ (y_negative ? 0x01 : 0x00));
            bytes[4] = (byte)(0xFF & y2);
            //Occupied and Certainty
            bytes[5] = (byte)(((c & 0x7F) << 1) ^ (isOccupied ? 0x01 : 0x00));

            return bytes;
        }
    }
}
