using System;
using System.Windows.Forms;

namespace ProjetS4
{
    internal class Picture
    {
        int id;
        DateTime dt;
        //Initial path
        string initPath;
        //Special picture path (picture name)
        string path;
        //Picture size
        const int width = 150;
        const int height = 150;
        //Picture box that will contain the picture
        PictureBox Pic1 = new PictureBox();
        //In case that the picture parameters are given by another way
        public Picture()
        {
        }
        public Picture(DateTime dt, int id, string ipath)
        {
            this.id = id;
            this.dt = dt;
            initPath = ipath+ "\\Photo\\";
        }
        public void setDate(DateTime d)
        {
            dt = d;
        }
        public DateTime getDate()
        {
            return dt;
        }
        public void setID(int id)
        {
            this.id = id;
        }
        public int getID()
        {
            return id;
        }
        public void incID()
        {
            this.id++;
        }
        public void decID()
        {
            this.id--;
        }
        //Draw the picture and define all the its parameters
        public PictureBox drawPicture(int x, int y, string dpath)
        {
            path = dpath;

            Pic1 = new PictureBox();
            Pic1.Size = new System.Drawing.Size(width, height);
            Pic1.Location = new System.Drawing.Point(x, y);
            Pic1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
            Pic1.BorderStyle = BorderStyle.Fixed3D;
            Pic1.ImageLocation = initPath + dpath;
            path = dpath;
            //Create the linked event of clicking on the picture
            Pic1.MouseClick += new MouseEventHandler(display);

            return Pic1;
        }
        //Handle a click on the picture
        public void display(object sender, EventArgs e)
        {
            //Copy the picture information (in that way the gallery picture and the other displayed picture are not in strife)
            PictureBox p = new PictureBox();
            PictureBox s = (PictureBox)sender;
            p.ImageLocation = s.ImageLocation;
            p.SizeMode = s.SizeMode;

            //Open a new window "photo_map" where the picture is displayed
            photo_map pm = new photo_map(p, this.id , this.dt, this.initPath);
             
        }
    }
}