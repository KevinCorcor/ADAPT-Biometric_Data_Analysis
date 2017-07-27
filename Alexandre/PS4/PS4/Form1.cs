using GMap.NET.WindowsForms;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using GMap.NET;
using GMap.NET.WindowsForms.Markers;
using GMap.NET.MapProviders;
using ProjetS4;
using GoogleMaps.LocationServices;
using Geocoding;
using System.Device.Location;
using Geocoding.Google;
using System.Linq;
using System.Globalization;
using System.Diagnostics;

namespace PS4
{
    public partial class Form1 : Form
    {
        /// <summary>
        ///     Parameter to change the map type
        /// </summary>
        int type = 0;
        /// <summary>
        ///     DataBase object
        /// </summary>
        DBconnect db = new DBconnect();
        /// <summary>
        ///     Files Path
        /// </summary>
        String fpath = "C:\\Users\\ADAPT Centre\\Documents\\MyLifeLogging";
        /// <summary>
        /// Markers managers 
        /// </summary>
        GMapOverlay markers = new GMapOverlay("all markers");
        GMapMarker[] markersArr = new GMapMarker[100];
        GMapMarker[] markerSearch = new GMapMarker[100];
        int nbPDate;
        GMapOverlay search=null;
        /// <summary>
        ///     Pictures managers
        /// </summary>
        PictureBox[] pbGall;
        Label[] dateArr;
        Label lbl_dateSearch = new Label();

        //EXEMPLE TO ADD ROUTES ON THE MAP
        /*GMapOverlay routes = new GMapOverlay("routes");
        List<PointLatLng> points = new List<PointLatLng>();
        placePoints(points, nbPoints, pointsLat, pointsLong);
        points.Add(new PointLatLng(48.8113635, 2.2981513));
        points.Add(new PointLatLng(48.8224445, 2.3121703));
        points.Add(new PointLatLng(48.8209825, 2.3189943));
        GMapRoute route = new GMapRoute(points, "A walk in the park");
        route.Stroke = new Pen(Color.Red, 2);
        routes.Routes.Add(route);
        gmap.Overlays.Add(routes);

        GMapOverlay routesOverlay = new GMapOverlay("routes");
        PointLatLng start = new PointLatLng(48.8113635, 2.2981513);
        PointLatLng end = new PointLatLng(48.8224445, 2.3121703);
        String name = "route";
        MapRoute route2 = GoogleMapProvider.Instance.GetRoute(new PointLatLng(48.8113635, 2.2981513), new PointLatLng(48.8224445, 2.3121703), false, false, 15);
        GMapRoute r = new GMapRoute(route2.points,name);
        routesOverlay.Routes.Add(r);
        gmap.Overlays.Add(routesOverlay);*/
        
            /// <summary>
        ///     default contructor call InitializeComponent();
        /// </summary>
        public Form1()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Starts a the form launching
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void Form1_Load(object sender, EventArgs e)
        {
            //Launch connection to database
            connectDB();
            //Creating and adding the map to the Map tab
            GMapControl gmap = new GMapControl();
            tb_map.Controls.Add(gmap);

            //Define where will the center of the map be
            initLoc();

            
            //Initialize a location on the map (polygon that defines an important place as work or home)
            initPlaces();

            //Place GPS coordinates points on the map
            placePoints();
            
            //Ask where the files are located
            askFilesPath();
            //Display the files path
            putPath();

            //Initialize the search combobox with all the points dates
            initPointsDate();

            //Initialize the search combobox with all the pictures countries
            initPicCountry();

            //Draw pictures by date and initialize the search combobox with all the pictures dates
            initGal();
            
        }

        /// <summary>
        /// Place GPS coordinates points on the map
        /// </summary>
        private void placePoints()
        {
            int i = 0;
            double lat = 0;
            double lon = 0;
            
            List<double> l = db.getPoints();
            int[] arrID = new int[l.Count / 3];

            GMapMarker[] marker = new GMapMarker[l.Count / 3];

            if (l != null)
            {
                //Retrieve the data from the list
                foreach (double coord in l)
                {
                    if (lon == 0 && lat == 0)
                    {
                        arrID[i] = (int)coord;
                        lon = 1;
                    }
                    else if (lon == 1)
                    {
                        lon = coord;
                    }
                    else if (lat == 0)
                    {
                        lat = coord;
                        //Convert the GPS coordinates into addresses
                        List<Address> lAddresses = new List<Address>();
                        if (db.selectCountry(arrID[i]).Equals(""))
                        {
                            string[] words;
                            string address = "";
                            string street = "";
                            string city = "";
                            string cp = "";
                            string country="";
                            List<Placemark> plc = null;
                            var st = GMapProviders.GoogleMap.GetPlacemarks(new PointLatLng(lon, lat), out plc);
                            if (st == GeoCoderStatusCode.G_GEO_SUCCESS && plc != null)
                            {
                                foreach (var pl in plc)
                                {
                                    //if (!string.IsNullOrEmpty(pl.Address))
                                    if(!pl.CountryName.Equals(""))
                                    {
                                        address = pl.Address;
                                        char delim1 = ',';
                                        //Split the address in 4 fields
                                        words = address.Split(delim1);
                                        //PARSE IN CASE OF "'" IN THE ADDRESS
                                        for(int k=0; k< words.Length;k++)
                                        {
                                            char delim2 = '\'';
                                            string[] tempWord = words[k].Split(delim2);
                                            if (tempWord.Length > 1)
                                            {
                                                words[k] = "";
                                                for(int k2=0;k2< tempWord.Length;k2++)
                                                {
                                                    words[k] = words[k] + " " + tempWord[k2];
                                                }
                                            }
                                        }
                                        if(words.Length==4)
                                        {
                                            street = words[0];
                                            city = words[1];
                                            cp = words[2];
                                            country = words[3];
                                        }
                                        //Inserting address into the data base
                                        db.insertPlace(street, city, cp, country, arrID[i]);
                                    }
                                }
                            }
                        }
                        //Create the marker
                        marker[i] = new GMarkerGoogle(
                            new PointLatLng(lon, lat),
                            GMarkerGoogleType.orange);
                        //TAG will be the id of the marker
                        marker[i].Tag = arrID[i].ToString();
                        //Add the marker to the marker overlay
                        markers.Markers.Add(marker[i]);
                        //Increment the id counter 
                        i++;
                        lat = 0;
                        lon = 0;
                    }
                }
            }
            else
            {
                MessageBox.Show("No point on the Map, be sure to add your GPS Data.");
            }
            //Copy the marker array
            markersArr = marker;
            //Add the marker overlay to the map
            gmap.Overlays.Add(markers);
        }

        /// <summary>
        /// Handle the event of clicking on a map marker
        /// </summary>
        /// <param name="marker">GMapMarker</param>
        /// <param name="e"> MouseEventArgs</param>
        private void gmap_OnMarkerClick(GMapMarker marker, MouseEventArgs e)
        {
            //Get the marker id
            string id = marker.Tag.ToString();
            //Get the marker date
            DateTime dt = db.getPDate(id);
            //Display a new window with the picture
            photo_map pm = new photo_map(id, dt);
        }

        /// <summary>
        /// Initialize the current position of the map
        /// </summary>
        private void initLoc()
        {   
            //Choose the address
            var address = "Dublin, Ireland";

            var locationService = new GoogleLocationService();
            var point = locationService.GetLatLongFromAddress(address);

            var latitude = point.Latitude;
            var longitude = point.Longitude;

            gmap.MapProvider = BingHybridMapProvider.Instance;
            GMaps.Instance.Mode = AccessMode.ServerOnly;
            gmap.Position = new PointLatLng(latitude, longitude);
            gmap.ShowCenter = false;
        }

        /// <summary>
        /// Initialize known places on the map
        /// </summary>
        private void initPlaces()
        {
            //Here it is 416 collins avenue
            GMapOverlay polygons = new GMapOverlay("polygons");
            List<PointLatLng> points = new List<PointLatLng>();
            points.Add(new PointLatLng(53.381814, -6.242130));
            points.Add(new PointLatLng(53.382124, -6.241911));
            points.Add(new PointLatLng(53.382108, -6.241816));
            points.Add(new PointLatLng(53.381770, -6.242015));
            GMapPolygon polygon = new GMapPolygon(points, "HOME");
            polygons.Polygons.Add(polygon);
            gmap.Overlays.Add(polygons);
        }

        /// <summary>
        /// Connection to DataBase
        /// </summary>
        private void connectDB()
        {
            db.OpenConnection();
        }

        /// <summary>
        /// Close connection of the DataBase when the windows is closed
        /// </summary>
        /// <param name="sender">obnject</param>
        /// <param name="e">FormClosedEventArgs</param>
        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            db.CloseConnection();
        }

        /// <summary>
        /// Allow to zoom by clicking a key
        /// </summary>
        /// <param name="sender">onject</param>
        /// <param name="e">KeyEventArgs</param>
        private void gmap_KeyUp(object sender, KeyEventArgs e)
        {
            if (gmap.Zoom < 18)
                gmap.Zoom++;
        }

        /// <summary>
        /// Ask where the files are located kcor - select lifelog directory
        /// </summary>
        private void askFilesPath()
        {
            string message = "The current path of your lifelogging folder is \"" + fpath + "\", do you want to change it?";
            string caption = "Files Path";
            MessageBoxButtons buttons = MessageBoxButtons.YesNo;
            DialogResult result1;
            FolderBrowserDialog folderBrowserDialog1 = new FolderBrowserDialog();
            // Displays the MessageBox.
            result1 = MessageBox.Show(message, caption, buttons);
            String folderName = "";
            if (result1 == System.Windows.Forms.DialogResult.Yes)
            {
                OpenFileDialog openFileDialog1;
                openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
                DialogResult result = folderBrowserDialog1.ShowDialog();
                bool fileOpened = false;
                MenuItem openMenuItem;
                openMenuItem = new System.Windows.Forms.MenuItem();

                if (result == DialogResult.OK)
                {
                    folderName = folderBrowserDialog1.SelectedPath;
                    if (!fileOpened)
                    {
                        // No file is opened, bring up openFileDialog in selected path.
                        openFileDialog1.InitialDirectory = folderName;
                        openFileDialog1.FileName = null;
                        openMenuItem.PerformClick();
                    }
                }
                MessageBox.Show("The new path is \"" + folderName + "\".");
            }
            //Store the file path in global
            fpath = folderName;
        }
        /// <summary>
        /// Initilize Combo box with Countries
        /// </summary>
        private void initPicCountry()
        {
            cb_location.Items.Add("All");
            //Get all the pictures country
            List<string> countries = db.getCountry();
            foreach (string country in countries)
            {
                if(country!="")
                    cb_location.Items.Add(country); //Adding the country in the combobox
            }
            //Put the pointer of the combobox on "all"
            cb_location.SelectedIndex = cb_location.Items.IndexOf("All");
        }
        /// <summary>
        /// Draw pictures by date and initialize the search combobox with all the pictures dates
        /// </summary>
        private void initGal()
        {
            int nbPic = 0;
            int cpt_lbl = 0;
            int x = 0;
            int y = 100;
            List<string> galpath = db.getGall();
            pbGall = new PictureBox[galpath.Count];
            dateArr = new Label[galpath.Count];
            Picture[] picArr = new Picture[galpath.Count];
            DateTime dt = new DateTime();
            int nb = galpath.Count;
            for (int i = 0; i < nb; i++)
            {
                string s = dt.ToString("dd'/'MM'/'yyyy"); //-> empty datetime is converted to 01/01/0001
                if (s == "01/01/0001")//first loop (verify)
                {
                    //Get the picture date
                    dt = db.picByDate(galpath[i]);

                    string sDate = dt.ToString("dd'/'MM'/'yyyy");
                    //Design of the date label
                    dateArr[cpt_lbl] = new Label();
                    dateArr[cpt_lbl].Text = sDate;
                    dateArr[cpt_lbl].AutoSize = true;
                    dateArr[cpt_lbl].BackColor = Color.DarkBlue;
                    dateArr[cpt_lbl].Font = new Font(FontFamily.GenericSansSerif, 20, FontStyle.Bold);
                    dateArr[cpt_lbl].Location = new Point(x, y);
                    pnl_gal.Controls.Add(dateArr[cpt_lbl]);
                   
                    cpt_lbl++;
                    //Adding the date to the picture search combobox
                    cb_dateP.Items.Add(sDate);
                    //Let the space for the label
                    y = y + 35;
                }
                else
                {
                    //For every photo, check the DATE
                    string s1 = dt/*.AddDays(1)*/.ToString("dd'/'MM'/'yyyy");
                    DateTime dt2nd = db.picByDate(galpath[i]);
                    string s2 = dt2nd.ToString("dd'/'MM'/'yyyy");
                    //If the date changes, add a new label on the gallery
                    if (s1 != s2)
                    {
                        if (x != 0)
                        {
                            x = 0;
                            y = y + 155;
                        }
                        //Label design
                        dateArr[cpt_lbl] = new Label();
                        dateArr[cpt_lbl].Text = s2;
                        dateArr[cpt_lbl].BackColor = Color.DarkBlue;
                        dateArr[cpt_lbl].Location = new Point(x, y);
                        dateArr[cpt_lbl].AutoSize = true;
                        dateArr[cpt_lbl].Font = new Font(FontFamily.GenericSansSerif, 20, FontStyle.Bold);
                        pnl_gal.Controls.Add(dateArr[cpt_lbl]);
                        //Check that the date is not already in the combobox
                        bool find = false;
                        for(int c=0; c<cb_location.Items.Count;c++)
                        {
                            if (cb_location.Items[c].ToString() == s2)
                                find = true;
                        }
                        //Adding the date in the combobox
                        if(!find)
                            cb_dateP.Items.Add(s2);
                        cpt_lbl++;
                        //Let the space for the label
                        y = y + 35;
                    }
                    //store the date to compare it with the next date
                    dt = dt2nd;
                }
                //Get the id and the path of the picture
                int id = db.getIdPic(galpath[i]);
                string pth = galpath[i];
                //Initiliaze picture
                picArr[i] = new Picture(dt, id, fpath);
                pbGall[i] = picArr[i].drawPicture(x, y, pth);
                pbGall[i].Name = "pic" + i;
                //Add the photo to the panel
                pnl_gal.Controls.Add(pbGall[i]);                    //kcor - does this append or can i insert at a specific location 
                nbPic++;
                x = x + 150;
                //Size of a line = 800
                if (x > 800)
                {
                    x = 0;
                    y = y + 150;
                }
            }
            //Put the pointer of the combobox on "all"
            cb_dateP.SelectedIndex = cb_dateP.Items.IndexOf("All");
        }

        /// <summary>
        /// Handle a click on the map and show information about the GPS coordinates
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">MouseEventArgs</param>
        private void gmap_MouseClick(object sender, MouseEventArgs e)
        {
            if (e.Button == System.Windows.Forms.MouseButtons.Left)
            {
                double lat = gmap.FromLocalToLatLng(e.X, e.Y).Lat;
                double lng = gmap.FromLocalToLatLng(e.X, e.Y).Lng;
            }
            List<Placemark> plc = null;
            var st = GMapProviders.GoogleMap.GetPlacemarks(gmap.FromLocalToLatLng(e.X, e.Y), out plc);
            if (st == GeoCoderStatusCode.G_GEO_SUCCESS && plc != null)
            {
                foreach (var pl in plc)
                {
                    if (!string.IsNullOrEmpty(pl.PostalCodeNumber))
                    {
                        //"Accuracy: " + pl.Accuracy + ", " +
                        lbl_add.Text = pl.Address;
                        lbl_pc.Text = pl.PostalCodeNumber;
                    }
                }
            }
        }

        /// <summary>
        /// Display the files path
        /// </summary>
        private void putPath()
        {
            lbl_path.Text = fpath;
        }

        /// <summary>
        /// Need to modify it in order to actualize the gallery if the files path changed
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void tp_gallery_Click(object sender, EventArgs e)
        {
            initGal();
        }

        /// <summary>
        /// Handle the click on the files path change button
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void btn_path_Click(object sender, EventArgs e)
        {
            askFilesPath();
            putPath();
            initGal();
        }

        /// <summary>
        /// Handle the action of searching places on the map
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void btn_search_Click(object sender, EventArgs e)
        {
            if (tb_loc.ToString() != null)
            {
               // dataFile
                var address = tb_loc.Text.ToString(); 
                //Using the geocoding of the GMAP Package
                var locationService = new GoogleLocationService();
                var point = locationService.GetLatLongFromAddress(address);
                try
                {
                    var latitude = point.Latitude;
                    var longitude = point.Longitude;
                    gmap.Position = new PointLatLng(latitude, longitude);
                }
                catch (NullReferenceException o)
                {
                    MessageBox.Show("Can't find entered address");
                }
            }
        }

        /// <summary>
        /// Handle the button that allows to change the map style
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void btn_type_Click_1(object sender, EventArgs e)
        {
            if (type == 0)
            {
                gmap.MapProvider = BingMapProvider.Instance; //First type (by default)
                type = 1;
            }
            else if (type == 1)
            {
                gmap.MapProvider = OpenCycleMapProvider.Instance; //Second type
                type = 2;
            }
            else if (type == 2)
            {
                gmap.MapProvider = BingHybridMapProvider.Instance; //Third type
                type = 0;
            }
        }

        /// <summary>
        /// Initialize the search combobox with all the points dates
        /// kcor - gps coord creation date
        /// </summary>
        private void initPointsDate()
        {
            //Get all the markers date
            List<string> dates = db.getPDate();
            nbPDate = dates.Count;
            for(int i=0; i<nbPDate;i=i+2)
            {
                string date = dates[i + 1] + "," + dates[i];
                //Adding the date in the combobox
                cb_date.Items.Add(date);
            }   
        }

        /// <summary>
        /// Handle a click on the marker search button
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void btn_date_Click(object sender, EventArgs e)
        {
            //If the selected item is not "all"
            if (cb_date.SelectedItem.ToString() != "All")
            {
                //Remove the marker overlay of the map
                gmap.Overlays.Remove(markers);
                //If the marker overlay has been used before, remove it from the map
                if (search != null)
                    gmap.Overlays.Remove(search);

                List<double> coord = new List<double>() ;
                string selectDate = cb_date.SelectedItem.ToString();
                //These lines remove the day's date before converting it
                char delim = ',';
                string[] words = selectDate.Split(delim);
                int nb = words.Length;
                search = new GMapOverlay();
                DateTime enteredDate = DateTime.Parse(words[nb-1]);
                double c = enteredDate.ToUniversalTime().Subtract(new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc)).TotalMilliseconds;

                //KEVIN REMOVE THESE 4 LINES
               // c = c + 59949936000000;//1901 years
                //long m3 = 2678400000;
                //c = c + (m3 * 3);//3 months
                //c = c + 172800000;//2days

                coord = db.getPointsByDate(c);
                markerSearch = new GMapMarker[coord.Count];
                int j = 0;
                for (int i = 0; i < (coord.Count - 1) / 3; i++)
                {
                    markerSearch[i] = new GMarkerGoogle(
                            new PointLatLng(coord[j + 1], coord[j + 2]),
                            GMarkerGoogleType.orange);
                    search.Markers.Add(markerSearch[i]);
                    markerSearch[i].Tag = coord[j];
                    j = j + 3;
                }
                gmap.Overlays.Add(search);     
            }
            //If the selected item is "all"
            else
            {
                gmap.Overlays.Remove(markers);
                if (search != null)
                    gmap.Overlays.Remove(search);
                placePoints();
            }

        }


        //THE  LOCATION SEARCH NEEDS TO BE IMPLEMENTED
        /// <summary>
        /// Handle a click on the search button of the gallery
        /// </summary>
        /// <param name="sender">object</param>
        /// <param name="e">EventArgs</param>
        private void btn_searchGal_Click(object sender, EventArgs e)
        {
            if(cb_dateP.SelectedIndex.ToString() != null && cb_location.SelectedIndex.ToString() != null)
            {
               int nbPic = pbGall.Count();
               //Removing all the previous displayed pictures
               for (int i = 0; i < nbPic; i++)
               {
                    pnl_gal.Controls.Remove(pbGall[i]);
               }
               //Removing all the previous displayed label
               for (int j = 0; j < dateArr.Count(); j++)
               {
                    pnl_gal.Controls.Remove(dateArr[j]);
                    //If previous researches have been done, remove it
                    if (lbl_dateSearch.Text != null)
                    {
                        pnl_gal.Controls.Remove(lbl_dateSearch);
                    }
                }
               //"All" case
               if (cb_dateP.SelectedItem.ToString() == "All")
               {
                   initGal();
               }
               //Specific search case
               else
               {
                    string date = cb_dateP.SelectedItem.ToString();
                    DateTime dt = DateTime.Parse(date);
                    double c = dt.ToUniversalTime().Subtract(new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc)).TotalMilliseconds;
                    List<string> pic = db.getPic(c);
                    pbGall = new PictureBox[pic.Count()];
                    //Clear all the picture from the picturebox array
                    Array.Clear(pbGall, 0, pbGall.Count());

                    Picture[] picSearch = new Picture[pic.Count()];
                    int x = 0;
                    int y = 100;

                    //Label design
                    lbl_dateSearch.Text = date;
                    lbl_dateSearch.BackColor = Color.DarkBlue;
                    lbl_dateSearch.Location = new Point(x, y);
                    lbl_dateSearch.AutoSize = true;
                    lbl_dateSearch.Font = new Font(FontFamily.GenericSansSerif, 20, FontStyle.Bold);
                    pnl_gal.Controls.Add(lbl_dateSearch);
                    //Let space for the label
                    y = y + 30;
                    //Adding picture to the panel
                    for (int k = 0; k < pic.Count; k++)
                    {
                        int id = db.getIdPic(pic[k]);
                        string pth = pic[k];
                        //ADD PICTURES
                        picSearch[k] = new Picture(dt, id, fpath);

                        pbGall[k] = picSearch[k].drawPicture(x, y, pth);
                        pbGall[k].Name = "pic" + k;
                        pnl_gal.Controls.Add(pbGall[k]);
                        x = x + 150;
                        //Size of a line=800
                        if (x > 800)
                        {
                            x = 0;
                            y = y + 150;
                        }
                    }
                }
                
            }

        }

        private void tb_loc_TextChanged(object sender, EventArgs e)
        {

        }
    }
}

