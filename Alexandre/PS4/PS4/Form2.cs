using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;


namespace ProjetS4
{   
    public partial class photo_map : Form
    {
        String initPath;
        PictureBox pb;
        Picture pic = new Picture();
        //Picture pic = new Picture();
        DBconnect db = new DBconnect();
        ///<summary>
        ///     Constructor calls initializeComponent();
        /// </summary>
        public photo_map()
        {
            InitializeComponent();
        }

        ///<summary>
        ///     Case of a marker click contructor with
        /// </summary>
        public photo_map(string id, DateTime dt)
        {
            InitializeComponent();
            //Connection to the database
            db.OpenConnection();
            string date = dt.ToString("dd'/'MM'/'yyyy");
            long d = (long)(dt - new DateTime(1970, 1, 1)).TotalMilliseconds;
            string path =db.getPath(d);
            int idP = Int32.Parse(id);
            lbl_date.Text =  date;
            pic = new Picture(dt, idP, path);
            pb = pic.drawPicture(100,100,path);
            pb.Show();
            //Can not navigate when the event is a click on a marker
            btn_right.Visible = false;
            btn_left.Visible = false;
            this.Show();
        }
        ///<summary>
        ///     Case of a picture click
        ///</summary>
        public photo_map(PictureBox p,int id, DateTime d, string path)
        {
            InitializeComponent();
            pic.setID(id) ;
            initPath = path;
            //PICTURE
            p.Size = new System.Drawing.Size(600, 400);
            p.Location = new System.Drawing.Point(100, 100);
            string sDate = d.ToString("dddd, MMMM dd, yyyy h:mm tt");
            //FORM
            string name = p.ImageLocation.ToString();
            this.Text = name;
            this.Controls.Add(p);
            this.Show();
            lbl_date.Text = sDate;
            pb = p;
            //BUTTONS
            this.btn_fb.Location = new System.Drawing.Point(150, 525);
            btn_fb.Click += new EventHandler(fbShare);
            this.btn_twit.Location = new System.Drawing.Point(350, 525);
        }

        /// <summary>
        ///     load
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void photo_map_Load(object sender, EventArgs e)
        {
            
        }

        /// <summary>
        ///     Generate the facebook button 
        /// </summary>
        private void generateBtn()
        {
            Button fbShare = new Button();
            fbShare.Text = "";
            
        }

       /// <summary>
        ///     Navigate between the picture (display the previous picture of the database)
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void btn_left_Click(object sender, EventArgs e)
        {
            db.OpenConnection();
            if(this.pic.getID() > 2 )
            {
                //get the picture filename
                string res = db.getPth(pic.getID(), 0); // 0 is the option to say that it will go to the previous pic
                //Change the displayed parameter with the new displayed picture
                DateTime currDT = db.picByDate(res);
                lbl_date.Text = currDT.ToString();
                this.Text = res;
                //Decrement the pic id
                pic.decID();
                //Change the pic location
                pb.ImageLocation = initPath + res;
            }
        }
        
        /// <summary>
        ///     Navigate between the picture (display the next picture of the database)
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void btn_right_Click(object sender, EventArgs e)
        {
            db.OpenConnection();
            if ( pic.getID()<db.getNbPic()-1)
            {
                //get the picture filename
                string res = db.getPth(pic.getID(), 1); // 1 is the option to say that it will go to the next pic
                //Change the displayed parameter with the new displayed picture
                DateTime currDT = db.picByDate(res);
                lbl_date.Text = currDT.ToString();
                this.Text = res;
                //Increment the pic id
                pic.incID();
                //Change the pic location
                pb.ImageLocation = initPath + res;
            }
        }
        /// <summary>
        ///     Facebook share button needs to be implemented
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void fbShare(Object sender, EventArgs e)
        {
            //change to object(lowercase)
        }
        /// <summary>
        ///     Twitter share button needs to be implemented
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void btn_twit_Click(object sender, EventArgs e)
        {
            
        }

        /// <summary>
        ///     Close the database connection when closing the form
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void photo_map_FormClosing(object sender, FormClosingEventArgs e)
        {
            db.CloseConnection();
        }
    }
}
