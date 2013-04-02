using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace kinect_mapper
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

        public void rotate(double theta)
        {
            x = Math.Cos(theta) * x - Math.Sin(theta) * y;
            y = Math.Sin(theta) * x + Math.Cos(theta) * y;
        }
    }
}
