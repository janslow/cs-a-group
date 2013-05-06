using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Microsoft.Samples.Kinect.DepthBasics
{
    class PointsFileReader
    {
        private string filepath;
        private List<List<Vector2D>> pointsToCheck;

        public PointsFileReader(string filepath)
        {
            this.filepath = filepath;
        }

        public void resetRead()
        {
            pointsToCheck = null;
        }

        public List<List<Vector2D>> getPointsToCheck()
        {
            if (pointsToCheck != null)
                return pointsToCheck;

            pointsToCheck = new List<List<Vector2D>>();
            List<Vector2D> listOfCoords = new List<Vector2D>();
            String sLine = "";
            StreamReader objReader = new StreamReader(filepath);
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
                        listOfCoords = new List<Vector2D>();
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
                            Vector2D point = new Vector2D(x, y);
                            listOfCoords.Add(point);
                            even = true;
                        }
                    }
                }
            }
            // convert to millimetres
            foreach (List<Vector2D> l in pointsToCheck)
                foreach (Vector2D v in l)
                    v.scale(10.0);
            objReader.Close();
            return pointsToCheck;
        }
    }
}


