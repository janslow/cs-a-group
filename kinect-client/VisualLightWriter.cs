using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Windows;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using Microsoft.Kinect;

namespace Microsoft.Samples.Kinect.DepthBasics
{
    class VisualLightWriter
    {
        private string imageDirectory;
        private string fileSeriesName;
        private byte[] imageData;
        private WriteableBitmap bitmap;
        private int writeNo = 0;
   
        public VisualLightWriter(String imageDirectory, String fileSeriesName, int width, int height, int dataLength)
        {
            this.imageDirectory = imageDirectory;
            this.fileSeriesName = fileSeriesName;
            bitmap = new WriteableBitmap(width, height, 96.0, 96.0, PixelFormats.Bgr32, null);
            imageData = new byte[dataLength];
        }

        public void copyImageFrame(ColorImageFrame f)
        {
            f.CopyPixelDataTo(imageData);
        }

        public void writeImageToFile()
        {
            writeNo++;
            bitmap.WritePixels(
                        new Int32Rect(0, 0, this.bitmap.PixelWidth, this.bitmap.PixelHeight),
                        this.imageData,
                        this.bitmap.PixelWidth * sizeof(int),
                        0);
            // create a png bitmap encoder which knows how to save a .png file
            BitmapEncoder encoder = new PngBitmapEncoder();

            // create frame from the writable bitmap and add to encoder
            encoder.Frames.Add(BitmapFrame.Create(this.bitmap));

            string path =  imageDirectory + "\\" + fileSeriesName + "-" + writeNo + ".png";

            // write the new file to disk
            try
            {
                using (FileStream fs = new FileStream(path, FileMode.Create))
                {
                    encoder.Save(fs);
                }
            }
            catch (IOException)
            {
                Console.WriteLine("*** Problem saving image file on write number " + writeNo);
            }
        }
    }
}
