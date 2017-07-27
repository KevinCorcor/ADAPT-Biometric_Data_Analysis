namespace PS4
{
    partial class Form1
    {
        /// <summary>
        /// Variable nécessaire au concepteur.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Nettoyage des ressources utilisées.
        /// </summary>
        /// <param name="disposing">true si les ressources managées doivent être supprimées ; sinon, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Code généré par le Concepteur Windows Form

        /// <summary>
        /// Method required to support the designer - do not modify the contents of this method with the code editor
        /// kcor - seems top design the layout of the main window
        /// </summary>
        private void InitializeComponent()
        {
            this.tab_map = new System.Windows.Forms.TabControl();
            this.tb_map = new System.Windows.Forms.TabPage();
            this.groupBox5 = new System.Windows.Forms.GroupBox();
            this.lbl_pc = new System.Windows.Forms.Label();
            this.lbl_add = new System.Windows.Forms.Label();
            this.groupBox4 = new System.Windows.Forms.GroupBox();
            this.btn_path = new System.Windows.Forms.Button();
            this.lbl_path = new System.Windows.Forms.Label();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.tb_loc = new System.Windows.Forms.TextBox();
            this.btn_search = new System.Windows.Forms.Button();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.btn_type = new System.Windows.Forms.Button();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.btn_date = new System.Windows.Forms.Button();
            this.cb_date = new System.Windows.Forms.ComboBox();
            this.gmap = new GMap.NET.WindowsForms.GMapControl();
            this.splitter1 = new System.Windows.Forms.Splitter();
            this.tb_curves = new System.Windows.Forms.TabPage();
            this.tp_gallery = new System.Windows.Forms.TabPage();
            this.pnl_gal = new System.Windows.Forms.Panel();
            this.groupBox6 = new System.Windows.Forms.GroupBox();
            this.btn_searchGal = new System.Windows.Forms.Button();
            this.cb_location = new System.Windows.Forms.ComboBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.cb_dateP = new System.Windows.Forms.ComboBox();
            this.tab_map.SuspendLayout();
            this.tb_map.SuspendLayout();
            this.groupBox5.SuspendLayout();
            this.groupBox4.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.tp_gallery.SuspendLayout();
            this.pnl_gal.SuspendLayout();
            this.groupBox6.SuspendLayout();
            this.SuspendLayout();
            // 
            // tab_map
            // 
            this.tab_map.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.tab_map.Controls.Add(this.tb_map);
            this.tab_map.Controls.Add(this.tb_curves);
            this.tab_map.Controls.Add(this.tp_gallery);
            this.tab_map.Font = new System.Drawing.Font("Rockwell", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.tab_map.Location = new System.Drawing.Point(1, 0);
            this.tab_map.Name = "tab_map";
            this.tab_map.SelectedIndex = 0;
            this.tab_map.Size = new System.Drawing.Size(983, 733);
            this.tab_map.TabIndex = 5;
            // 
            // tb_map
            // 
            this.tb_map.BackColor = System.Drawing.Color.Black;
            this.tb_map.Controls.Add(this.groupBox5);
            this.tb_map.Controls.Add(this.groupBox4);
            this.tb_map.Controls.Add(this.groupBox3);
            this.tb_map.Controls.Add(this.groupBox2);
            this.tb_map.Controls.Add(this.groupBox1);
            this.tb_map.Controls.Add(this.gmap);
            this.tb_map.Controls.Add(this.splitter1);
            this.tb_map.Location = new System.Drawing.Point(4, 30);
            this.tb_map.Name = "tb_map";
            this.tb_map.Padding = new System.Windows.Forms.Padding(3);
            this.tb_map.Size = new System.Drawing.Size(975, 699);
            this.tb_map.TabIndex = 0;
            this.tb_map.Text = "Maps";
            // 
            // groupBox5
            // 
            this.groupBox5.BackColor = System.Drawing.Color.RoyalBlue;
            this.groupBox5.Controls.Add(this.lbl_pc);
            this.groupBox5.Controls.Add(this.lbl_add);
            this.groupBox5.Location = new System.Drawing.Point(7, 210);
            this.groupBox5.Name = "groupBox5";
            this.groupBox5.Size = new System.Drawing.Size(260, 100);
            this.groupBox5.TabIndex = 22;
            this.groupBox5.TabStop = false;
            this.groupBox5.Text = "Map click informations";
            // 
            // lbl_pc
            // 
            this.lbl_pc.AutoSize = true;
            this.lbl_pc.BackColor = System.Drawing.Color.RoyalBlue;
            this.lbl_pc.Font = new System.Drawing.Font("Rockwell", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lbl_pc.Location = new System.Drawing.Point(6, 61);
            this.lbl_pc.Name = "lbl_pc";
            this.lbl_pc.Size = new System.Drawing.Size(54, 13);
            this.lbl_pc.TabIndex = 14;
            this.lbl_pc.Text = "PostCode";
            // 
            // lbl_add
            // 
            this.lbl_add.AutoSize = true;
            this.lbl_add.BackColor = System.Drawing.Color.RoyalBlue;
            this.lbl_add.Font = new System.Drawing.Font("Rockwell", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lbl_add.Location = new System.Drawing.Point(6, 33);
            this.lbl_add.Name = "lbl_add";
            this.lbl_add.Size = new System.Drawing.Size(50, 13);
            this.lbl_add.TabIndex = 13;
            this.lbl_add.Text = "Address";
            // 
            // groupBox4
            // 
            this.groupBox4.BackColor = System.Drawing.Color.RoyalBlue;
            this.groupBox4.Controls.Add(this.btn_path);
            this.groupBox4.Controls.Add(this.lbl_path);
            this.groupBox4.Location = new System.Drawing.Point(6, 316);
            this.groupBox4.Name = "groupBox4";
            this.groupBox4.Size = new System.Drawing.Size(260, 104);
            this.groupBox4.TabIndex = 21;
            this.groupBox4.TabStop = false;
            this.groupBox4.Text = "Files informations";
            // 
            // btn_path
            // 
            this.btn_path.Location = new System.Drawing.Point(57, 60);
            this.btn_path.Name = "btn_path";
            this.btn_path.Size = new System.Drawing.Size(95, 29);
            this.btn_path.TabIndex = 15;
            this.btn_path.Text = "Change path";
            this.btn_path.UseVisualStyleBackColor = true;
            this.btn_path.Click += new System.EventHandler(this.btn_path_Click);
            // 
            // lbl_path
            // 
            this.lbl_path.AutoSize = true;
            this.lbl_path.BackColor = System.Drawing.Color.RoyalBlue;
            this.lbl_path.Font = new System.Drawing.Font("Rockwell", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lbl_path.Location = new System.Drawing.Point(9, 32);
            this.lbl_path.Name = "lbl_path";
            this.lbl_path.Size = new System.Drawing.Size(43, 16);
            this.lbl_path.TabIndex = 16;
            this.lbl_path.Text = "label1";
            // 
            // groupBox3
            // 
            this.groupBox3.BackColor = System.Drawing.Color.RoyalBlue;
            this.groupBox3.Controls.Add(this.tb_loc);
            this.groupBox3.Controls.Add(this.btn_search);
            this.groupBox3.Location = new System.Drawing.Point(7, 85);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Size = new System.Drawing.Size(260, 119);
            this.groupBox3.TabIndex = 20;
            this.groupBox3.TabStop = false;
            this.groupBox3.Text = "Map Research";
            // 
            // tb_loc
            // 
            this.tb_loc.BackColor = System.Drawing.Color.DarkOrange;
            this.tb_loc.Location = new System.Drawing.Point(9, 42);
            this.tb_loc.Name = "tb_loc";
            this.tb_loc.Size = new System.Drawing.Size(167, 30);
            this.tb_loc.TabIndex = 10;
            this.tb_loc.Text = "Dublin";
            // 
            // btn_search
            // 
            this.btn_search.Location = new System.Drawing.Point(56, 78);
            this.btn_search.Name = "btn_search";
            this.btn_search.Size = new System.Drawing.Size(95, 28);
            this.btn_search.TabIndex = 11;
            this.btn_search.Text = "Search Location";
            this.btn_search.UseVisualStyleBackColor = true;
            this.btn_search.Click += new System.EventHandler(this.btn_search_Click);
            // 
            // groupBox2
            // 
            this.groupBox2.BackColor = System.Drawing.Color.RoyalBlue;
            this.groupBox2.Controls.Add(this.btn_type);
            this.groupBox2.Font = new System.Drawing.Font("Rockwell", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.groupBox2.Location = new System.Drawing.Point(7, 10);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(260, 69);
            this.groupBox2.TabIndex = 19;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Map type";
            // 
            // btn_type
            // 
            this.btn_type.Location = new System.Drawing.Point(56, 29);
            this.btn_type.Name = "btn_type";
            this.btn_type.Size = new System.Drawing.Size(95, 28);
            this.btn_type.TabIndex = 9;
            this.btn_type.Text = "Change Type";
            this.btn_type.UseVisualStyleBackColor = true;
            this.btn_type.Click += new System.EventHandler(this.btn_type_Click_1);
            // 
            // groupBox1
            // 
            this.groupBox1.BackColor = System.Drawing.Color.RoyalBlue;
            this.groupBox1.Controls.Add(this.btn_date);
            this.groupBox1.Controls.Add(this.cb_date);
            this.groupBox1.Location = new System.Drawing.Point(6, 426);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(260, 121);
            this.groupBox1.TabIndex = 18;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Markers manager";
            // 
            // btn_date
            // 
            this.btn_date.Location = new System.Drawing.Point(64, 74);
            this.btn_date.Name = "btn_date";
            this.btn_date.Size = new System.Drawing.Size(88, 30);
            this.btn_date.TabIndex = 1;
            this.btn_date.Text = "Search";
            this.btn_date.UseVisualStyleBackColor = true;
            this.btn_date.Click += new System.EventHandler(this.btn_date_Click);
            // 
            // cb_date
            // 
            this.cb_date.AutoCompleteCustomSource.AddRange(new string[] {
            "All"});
            this.cb_date.BackColor = System.Drawing.Color.DarkOrange;
            this.cb_date.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.cb_date.FormattingEnabled = true;
            this.cb_date.Items.AddRange(new object[] {
            "All"});
            this.cb_date.Location = new System.Drawing.Point(12, 29);
            this.cb_date.Name = "cb_date";
            this.cb_date.Size = new System.Drawing.Size(228, 29);
            this.cb_date.TabIndex = 1;
            this.cb_date.Text = "All";
            // 
            // gmap
            // 
            this.gmap.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.gmap.Bearing = 0F;
            this.gmap.CanDragMap = true;
            this.gmap.EmptyTileColor = System.Drawing.Color.Navy;
            this.gmap.GrayScaleMode = false;
            this.gmap.HelperLineOption = GMap.NET.WindowsForms.HelperLineOptions.DontShow;
            this.gmap.LevelsKeepInMemmory = 5;
            this.gmap.Location = new System.Drawing.Point(273, 6);
            this.gmap.MarkersEnabled = true;
            this.gmap.MaxZoom = 17;
            this.gmap.MinZoom = 0;
            this.gmap.MouseWheelZoomEnabled = true;
            this.gmap.MouseWheelZoomType = GMap.NET.MouseWheelZoomType.MousePositionAndCenter;
            this.gmap.Name = "gmap";
            this.gmap.NegativeMode = false;
            this.gmap.PolygonsEnabled = true;
            this.gmap.RetryLoadTile = 0;
            this.gmap.RoutesEnabled = true;
            this.gmap.ScaleMode = GMap.NET.WindowsForms.ScaleModes.Integer;
            this.gmap.SelectedAreaFillColor = System.Drawing.Color.FromArgb(((int)(((byte)(33)))), ((int)(((byte)(65)))), ((int)(((byte)(105)))), ((int)(((byte)(225)))));
            this.gmap.ShowTileGridLines = false;
            this.gmap.Size = new System.Drawing.Size(699, 693);
            this.gmap.TabIndex = 17;
            this.gmap.Zoom = 12D;
            this.gmap.OnMarkerClick += new GMap.NET.WindowsForms.MarkerClick(this.gmap_OnMarkerClick);
            this.gmap.KeyUp += new System.Windows.Forms.KeyEventHandler(this.gmap_KeyUp);
            this.gmap.MouseClick += new System.Windows.Forms.MouseEventHandler(this.gmap_MouseClick);
            // 
            // splitter1
            // 
            this.splitter1.BackColor = System.Drawing.Color.Navy;
            this.splitter1.Location = new System.Drawing.Point(3, 3);
            this.splitter1.Name = "splitter1";
            this.splitter1.Size = new System.Drawing.Size(264, 693);
            this.splitter1.TabIndex = 7;
            this.splitter1.TabStop = false;
            // 
            // tb_curves
            // 
            this.tb_curves.Location = new System.Drawing.Point(4, 30);
            this.tb_curves.Name = "tb_curves";
            this.tb_curves.Padding = new System.Windows.Forms.Padding(3);
            this.tb_curves.Size = new System.Drawing.Size(975, 699);
            this.tb_curves.TabIndex = 1;
            this.tb_curves.Text = "Curves";
            this.tb_curves.UseVisualStyleBackColor = true;
            // 
            // tp_gallery
            // 
            this.tp_gallery.BackColor = System.Drawing.SystemColors.WindowFrame;
            this.tp_gallery.Controls.Add(this.pnl_gal);
            this.tp_gallery.Location = new System.Drawing.Point(4, 30);
            this.tp_gallery.Name = "tp_gallery";
            this.tp_gallery.Padding = new System.Windows.Forms.Padding(3);
            this.tp_gallery.Size = new System.Drawing.Size(975, 699);
            this.tp_gallery.TabIndex = 2;
            this.tp_gallery.Text = "Gallery";
            // 
            // pnl_gal
            // 
            this.pnl_gal.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.pnl_gal.AutoScroll = true;
            this.pnl_gal.BackColor = System.Drawing.SystemColors.WindowFrame;
            this.pnl_gal.Controls.Add(this.groupBox6);
            this.pnl_gal.Location = new System.Drawing.Point(8, 6);
            this.pnl_gal.Name = "pnl_gal";
            this.pnl_gal.Size = new System.Drawing.Size(960, 687);
            this.pnl_gal.TabIndex = 0;
            // 
            // groupBox6
            // 
            this.groupBox6.BackColor = System.Drawing.Color.RoyalBlue;
            this.groupBox6.Controls.Add(this.btn_searchGal);
            this.groupBox6.Controls.Add(this.cb_location);
            this.groupBox6.Controls.Add(this.label2);
            this.groupBox6.Controls.Add(this.label1);
            this.groupBox6.Controls.Add(this.cb_dateP);
            this.groupBox6.Font = new System.Drawing.Font("Rockwell", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.groupBox6.Location = new System.Drawing.Point(0, 3);
            this.groupBox6.Name = "groupBox6";
            this.groupBox6.Size = new System.Drawing.Size(907, 70);
            this.groupBox6.TabIndex = 1;
            this.groupBox6.TabStop = false;
            this.groupBox6.Text = "Search Box";
            // 
            // btn_searchGal
            // 
            this.btn_searchGal.Font = new System.Drawing.Font("Rockwell", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btn_searchGal.Location = new System.Drawing.Point(562, 26);
            this.btn_searchGal.Name = "btn_searchGal";
            this.btn_searchGal.Size = new System.Drawing.Size(84, 25);
            this.btn_searchGal.TabIndex = 5;
            this.btn_searchGal.Text = "Search";
            this.btn_searchGal.UseVisualStyleBackColor = true;
            this.btn_searchGal.Click += new System.EventHandler(this.btn_searchGal_Click);
            // 
            // cb_location
            // 
            this.cb_location.BackColor = System.Drawing.Color.DarkOrange;
            this.cb_location.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.cb_location.FormattingEnabled = true;
            this.cb_location.Location = new System.Drawing.Point(368, 28);
            this.cb_location.Name = "cb_location";
            this.cb_location.Size = new System.Drawing.Size(177, 29);
            this.cb_location.TabIndex = 4;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Rockwell", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(276, 28);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(85, 21);
            this.label2.TabIndex = 3;
            this.label2.Text = "Location";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Rockwell", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(3, 28);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(51, 21);
            this.label1.TabIndex = 2;
            this.label1.Text = "Date";
            // 
            // cb_dateP
            // 
            this.cb_dateP.BackColor = System.Drawing.Color.DarkOrange;
            this.cb_dateP.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.cb_dateP.FormattingEnabled = true;
            this.cb_dateP.Items.AddRange(new object[] {
            "All"});
            this.cb_dateP.Location = new System.Drawing.Point(60, 28);
            this.cb_dateP.Name = "cb_dateP";
            this.cb_dateP.Size = new System.Drawing.Size(210, 29);
            this.cb_dateP.TabIndex = 0;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.DimGray;
            this.ClientSize = new System.Drawing.Size(984, 733);
            this.Controls.Add(this.tab_map);
            this.Name = "Form1";
            this.Text = "Form1";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.Form1_FormClosed);
            this.Load += new System.EventHandler(this.Form1_Load);
            this.tab_map.ResumeLayout(false);
            this.tb_map.ResumeLayout(false);
            this.groupBox5.ResumeLayout(false);
            this.groupBox5.PerformLayout();
            this.groupBox4.ResumeLayout(false);
            this.groupBox4.PerformLayout();
            this.groupBox3.ResumeLayout(false);
            this.groupBox3.PerformLayout();
            this.groupBox2.ResumeLayout(false);
            this.groupBox1.ResumeLayout(false);
            this.tp_gallery.ResumeLayout(false);
            this.pnl_gal.ResumeLayout(false);
            this.groupBox6.ResumeLayout(false);
            this.groupBox6.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TabControl tab_map;
        private System.Windows.Forms.TabPage tb_map;
        private System.Windows.Forms.Label lbl_path;
        private System.Windows.Forms.Button btn_path;
        private System.Windows.Forms.Label lbl_pc;
        private System.Windows.Forms.Label lbl_add;
        private System.Windows.Forms.Button btn_search;
        private System.Windows.Forms.TextBox tb_loc;
        private System.Windows.Forms.Button btn_type;
        private System.Windows.Forms.Splitter splitter1;
        private System.Windows.Forms.TabPage tb_curves;
        private System.Windows.Forms.TabPage tp_gallery;
        private System.Windows.Forms.Panel pnl_gal;
        private GMap.NET.WindowsForms.GMapControl gmap;
        private System.Windows.Forms.ComboBox cb_dateP;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Button btn_date;
        private System.Windows.Forms.ComboBox cb_date;
        private System.Windows.Forms.GroupBox groupBox4;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.GroupBox groupBox5;
        private System.Windows.Forms.Button btn_searchGal;
        private System.Windows.Forms.ComboBox cb_location;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.GroupBox groupBox6;
    }
}

