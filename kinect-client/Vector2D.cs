using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Microsoft.Samples.Kinect.DepthBasics
{
    class Vector2D
    {
        private double x;
        private double y;

        public Vector2D(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        public double getX()
        {
            return x;
        }

        public double getY()
        {
            return y;
        }

        public void translate(Vector2D v)
        {
            x = x + v.getX();
            y = y + v.getY();
        }

        public void rotate(double thetaDegrees)
        {
            double thetaRadians = thetaDegrees * Math.PI / 180;
            double oldX = x;
            double oldY = y;
            x = Math.Cos(thetaRadians) * oldX - Math.Sin(thetaRadians) * oldY;
            y = Math.Sin(thetaRadians) * oldX + Math.Cos(thetaRadians) * oldY;
        }

        public void scale(double scaleFactor)
        {
            x = x * scaleFactor;
            y = y * scaleFactor;
        }


        public void reflectInXaxis()
        {
            y = (-1.0) * y;
        }

        public void reflectInYaxis()
        {
            x = (-1.0) * x;
        }

        public double getModulus()
        {
            return (Math.Sqrt(x * x + y * y));
        }

        public double getModulusSquared()
        {
            return (x * x + y * y);
        }


    }
}
