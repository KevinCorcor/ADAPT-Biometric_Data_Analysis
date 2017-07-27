using System;
using System.Windows.Forms;
using MySql.Data.MySqlClient;
using System.Collections.Generic;
using System.Data.SQLite;
using System.Globalization;

namespace ProjetS4
{

    /// <summary>
    ///     information connection and retrieval
    /// </summary>
    public class DBconnect
    {   /// <summary>
        ///      default contructor which does nothing
        /// </summary>
        public DBconnect()
        {
        }

        //Object from the SQLite package
        SQLiteConnection m_dbConnection;

        /// <summary>
        ///     Open connection to database
        /// </summary>
        public bool OpenConnection()
        {
            //Change the path to your Database location
            m_dbConnection = new SQLiteConnection("Data Source=C:\\Users\\Adapt Centre\\Documents\\MyLifeLogging\\Database.db;Version=3;");    //version 3
            try
            {
                m_dbConnection.Open();
                return true;
            }
            catch (MySqlException ex)
            {
                MessageBox.Show("Cannot connect to database, try again");
                return false;
            }
        }

        ///<summary>
        ///     Close connection
        ///     @return boolean
        /// </summary>
        public bool CloseConnection()
        {
            try
            {
                if(m_dbConnection != (null))
                    m_dbConnection.Close();
                //MessageBox.Show("Disconnected from the DataBase");
                return true;
            }
            catch (MySqlException ex)
            {
                MessageBox.Show(ex.Message);
                return false;
            }
        }

        ///<summary>
        ///     Return a list of all the points with id, longitude and latitude
        ///     @return listCoord - double array
        /// </summary>
        public List <double> getPoints() //List<double>[]
        {
            //List that will save results
            List<double> listCoord= new List<double>();
            
            String query = "SELECT id, latitude, longitude FROM gps ";
                // WHERE id<448";// G,sensor S";// WHERE " + // S.id<3 " +
                //" S.creationDate = G.creationDate ";//AND S.***>? OR S.***>?";
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader sqReader = cmd.ExecuteReader();
            try
            {
                while (sqReader.Read()) 
                {
                    //Need to be converted to a int
                    int temp = Convert.ToInt32(sqReader["id"]);
                    listCoord.Add(temp);
                    listCoord.Add((double)sqReader["latitude"]);
                    listCoord.Add((double)sqReader["longitude"]);
                }
            }
            catch
            {
                MessageBox.Show("Error on request getPoints()");

            }
            return listCoord;
        }
        ///<summary>
        ///      @Return res - int - the number of key strokes
        /// </summary>
        public int countKeyS()
        {
            int res=-1;
            //Create the query
            String query = "SELECT count(*) from keystrokes";
            SQLiteCommand sqCommand = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader sqReader = sqCommand.ExecuteReader();
            try
            {
                // Always call Read before accessing data.
                while (sqReader.Read())
                {
                    res = sqReader.GetInt32(0);
                }
            }
            catch
            {
                MessageBox.Show("Error on request countKeyS");
            }
            //Close the data reader
            sqReader.Close();
            return res;
        }

        /// <summary>
        ///     Return a List with all pictures path
        ///     @return path - String rray
        /// </summary>
        public List <string> getGall()
        {
            //Here we selected between two numbers because too much pictures crash the software
            // string query = "SELECT * FROM Photo";// WHERE id BETWEEN 677 AND 900" ;
           
 
            string text = System.IO.File.ReadAllText("C:\\Users\\ADAPT Centre\\Documents\\testMIN_TEMP_STD.txt");                  //kcor modification
            
            string query = "SELECT * FROM Photo WHERE "+text;//kcor - specifies how many rows to look at so I could specify specif pics of interest here if i know their info!!
            query = query.Replace("\r\n", " ");
            
            List<string> path = new List<string> ();
            //Create Command
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            {
                path.Add((string)DR["fileName"]);
            }
            DR.Close();
            return path;
        }
        ///<summary>
        ///     Returns the Date of a given picture path
        ///     @param path String
        ///     @return date DateTime
        /// </summary>
        public DateTime picByDate(string path)
        {
            string query = "SELECT creationDate FROM Photo WHERE fileName=\"" + path + "\"";
            DateTime date= new DateTime();
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            { 
                long c1 = (long) DR["creationDate"];
                DateTime start = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
                date = start.AddMilliseconds(c1).ToLocalTime();
            }
            DR.Close();
            return date;
        }

        ///<summary>
        ///     @param id - int - picture id
        ///     @Return - date - the Datetime of a given picture id
        ///</summary>
        public DateTime getDate(int id)
        {
            string query = "SELECT creationDate FROM Photo WHERE id=" + id ;
            DateTime date = new DateTime();
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            {
                long c1 = (long)DR["creationDate"];
                DateTime start = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
                date = start.AddMilliseconds(c1).ToLocalTime();
            }
            DR.Close();
            return date;
        }

        ///<summary>
        ///     Return ID with a given picture path
        ///     @param path - string
        ///     @return r int
        /// </summary>
        public int getIdPic(string path)
        {
            int r = 0;
            string query = "SELECT id FROM photo WHERE fileName=\"" + path + "\"";
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            {
                r = Convert.ToInt32(DR["id"]); 
            }
            DR.Close();
            return r;
        }

        ///<summary>
        ///     Return the picture path with the given id and the option to navigate
        ///     @param id int   
        ///     @param option int
        ///</summary>
        public string getPth(int id, int option)
        {
            //Id +1 -> navigate on the right
            if(option ==1)
            {
                id++;
            }
            //Id -1 -> navigate on the left
            else if (option ==0)
            {
                id--;
            }
            string res = "";
            string query = "SELECT fileName FROM Photo WHERE id=" + id ;
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            {
                res = (string)(DR["fileName"]);
            }
            DR.Close();
            return res;
        }

        ///<summary>
        ///     Return the number of Pictures in the DataBase
        ///     @return res int
        ///</summary>
        public int getNbPic()
        {
            int res = 0;
            string query = "SELECT count(*) FROM Photo ";
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while(DR.Read())
            {
                res = DR.GetInt32(0);
            }
            DR.Close();
            return res;
        }

        ///<summary>
        ///     Update the country of a selected gps data (given by the id)
        ///     @param string street
        ///     @param string city
        ///     @param string cp
        ///     @param string country
        ///     @param int id
        /// </summary>
        public void insertPlace(string street, string city, string cp,string country, int id)
        {
            string query = "UPDATE gps SET street='"+street+"', city='"+
                city+"', district='"+ cp + "', country ='"+country+"' WHERE id="+ id;
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            cmd.ExecuteNonQuery();
        }

        ///<summary>
        ///     Return the country information from a given Gps coordinare (defined by the id)
        ///     @param - id int
        ///     @return - res String
        ///</summary>
        public string selectCountry(int id)
        {
            string res="";
            string query = "SELECT country FROM gps WHERE id=" + id;
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            {
                res = DR["country"].ToString();
            }
            DR.Close();
            return res;
        }

        ///<summary>
        ///     Return a countries list
        ///     @return res - String array of countries
        ///</summary>
        public List <string> getCountry()
        {
            string query = "SELECT DISTINCT country FROM GPS ";
            List<string> res = new List<string>();
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            {
                if(!DR["country"].ToString().Equals(null))
                    res.Add((string)DR["country"]);
            }
            DR.Close();
            return res;
        }
       
        ///<summary>Return points date list</summary>
        public List<string> getPDate()
        {
            bool first = true;
            string query = "SELECT DISTINCT creationDate FROM gps  ";
            List<string> res = new List<string>();
            string tempD="";
            DateTime date = new DateTime();
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            {
                long c = (long)DR["creationDate"];

                //KEVIN REMOVE THESE 4 LINES 
                //c = c - 59949936000000;//1901 years
                //long m3 = 2678400000 ;
                //c = c - (m3 *3);//3 months
                //c = c - 172800000;//2days

                DateTime start = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
                date = start.AddMilliseconds(c).ToLocalTime();
                string sDate = date.ToString("dd'/'MM'/'yyyy");
                //Kind of distinct: check for the date to not appear more than 1 time
                if(first)
                {
                   res.Add(sDate);
                   res.Add(date.ToString("dddd", new CultureInfo("en-EN")));
                   tempD = sDate;
                   first = false;
                }
                else if(tempD != sDate)
                { 
                    res.Add(sDate);
                    res.Add(date.ToString("dddd", new CultureInfo("en-EN")));
                    tempD = sDate;
                } 
            }
            DR.Close();
            return res;
        }



        ///<summary>
        ///     Return the date for the given point (with id)
        ///     id - String
        ///</summary>
        public DateTime getPDate(string id)
        {
            string query = "SELECT creationDate FROM gps WHERE id="+id;
            DateTime date = new DateTime();
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            {
                long c = (long)DR["creationDate"];
                DateTime start = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
                date = start.AddMilliseconds(c).ToLocalTime();
            }
            DR.Close();
            return date;
        }


        ///<summary>
        ///     Return the picture path taken between the given date and the given date -30 seconds. 
        ///     @param date - long
        ///     @return res - String
        ///</summary>
        public string getPath(long date)
        {
            //Problem with null string result
            string res= "";
            string query = "SELECT fileName FROM photo WHERE creationDate BETWEEN " + (date - 8.64e7) + " AND " + (date + 8.64e7);//date +1sec et date -30 sec
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            {
                res = (string)DR["fileName"];
            }
            DR.Close();
            return res;
        }

        ///<summary>
        ///     Return a list a points (id, longitude and latitude) for a given date
        ///      @param date - double
        ///      @return res
        ///</summary> 
        public List <double> getPointsByDate(double date)
        {
            string query = "SELECT id, latitude, longitude FROM GPS WHERE creationDate BETWEEN "+ date + " AND " + (date+ 8.64e7);
            List<double> res = new List<double>();
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            try
            {
                while (DR.Read())
                {
                    int temp = Convert.ToInt32(DR["id"]);
                    res.Add(temp);
                    res.Add((double)DR["latitude"]);
                    res.Add((double)DR["longitude"]);
                }
            }
            catch
            {
                MessageBox.Show("Error on request getPoints()");
            }
            DR.Close();
            return res;
        }

         ///<summary>
         ///    Return a list of picture's path for a given date
         ///    @param date - double
         ///    @Return path - String Array of photo paths
         ///</summary>
        public List<string> getPic(double date)
        {
            //why 8.64e7
            string query = "SELECT * FROM Photo WHERE creationDate BETWEEN " + date + " AND " + (date + 8.64e7);
            List<string> path = new List<string>();
            int i = 0;
            //Create Command
            SQLiteCommand cmd = new SQLiteCommand(query, m_dbConnection);
            SQLiteDataReader DR = cmd.ExecuteReader();
            while (DR.Read())
            {
                path.Add((string)DR["fileName"]);
                i++;
            }
            DR.Close();
            return path;
        }
    }
}