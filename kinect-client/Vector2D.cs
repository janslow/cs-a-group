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
            x = Math.Cos(thetaRadians) * x - Math.Sin(thetaRadians) * y;
            y = Math.Sin(thetaRadians) * x + Math.Cos(thetaRadians) * y;
        }
    }
}
