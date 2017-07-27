namespace ProjetS4
{   /// <summary>
    ///     form2 photomap class 
    /// </summary>
    partial class photo_map
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(photo_map));
            this.backgroundWorker1 = new System.ComponentModel.BackgroundWorker();
            this.btn_fb = new System.Windows.Forms.Button();
            this.btn_twit = new System.Windows.Forms.Button();
            this.lbl_date = new System.Windows.Forms.Label();
            this.btn_left = new System.Windows.Forms.Button();
            this.btn_right = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btn_fb
            // 
            this.btn_fb.AutoSize = true;
            this.btn_fb.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("btn_fb.BackgroundImage")));
            this.btn_fb.Location = new System.Drawing.Point(150, 525);
            this.btn_fb.Name = "btn_fb";
            this.btn_fb.Size = new System.Drawing.Size(185, 73);
            this.btn_fb.TabIndex = 1;
            this.btn_fb.Text = "-";
            this.btn_fb.UseVisualStyleBackColor = true;
            // 
            // btn_twit
            // 
            this.btn_twit.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("btn_twit.BackgroundImage")));
            this.btn_twit.Location = new System.Drawing.Point(350, 525);
            this.btn_twit.Name = "btn_twit";
            this.btn_twit.Size = new System.Drawing.Size(203, 73);
            this.btn_twit.TabIndex = 2;
            this.btn_twit.UseVisualStyleBackColor = true;
            this.btn_twit.Click += new System.EventHandler(this.btn_twit_Click);
            // 
            // lbl_date
            // 
            this.lbl_date.AutoSize = true;
            this.lbl_date.Location = new System.Drawing.Point(12, 9);
            this.lbl_date.Name = "lbl_date";
            this.lbl_date.Size = new System.Drawing.Size(35, 13);
            this.lbl_date.TabIndex = 3;
            this.lbl_date.Text = "label1";
            // 
            // btn_left
            // 
            this.btn_left.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("btn_left.BackgroundImage")));
            this.btn_left.Location = new System.Drawing.Point(12, 280);
            this.btn_left.Name = "btn_left";
            this.btn_left.Size = new System.Drawing.Size(56, 71);
            this.btn_left.TabIndex = 4;
            this.btn_left.UseVisualStyleBackColor = true;
            this.btn_left.Click += new System.EventHandler(this.btn_left_Click);
            // 
            // btn_right
            // 
            this.btn_right.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("btn_right.BackgroundImage")));
            this.btn_right.Location = new System.Drawing.Point(716, 280);
            this.btn_right.Name = "btn_right";
            this.btn_right.Size = new System.Drawing.Size(56, 71);
            this.btn_right.TabIndex = 5;
            this.btn_right.UseVisualStyleBackColor = true;
            this.btn_right.Click += new System.EventHandler(this.btn_right_Click);
            // 
            // photo_map
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(255)))), ((int)(((byte)(255)))));
            this.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("$this.BackgroundImage")));
            this.ClientSize = new System.Drawing.Size(784, 661);
            this.Controls.Add(this.btn_right);
            this.Controls.Add(this.btn_left);
            this.Controls.Add(this.lbl_date);
            this.Controls.Add(this.btn_twit);
            this.Controls.Add(this.btn_fb);
            this.Name = "photo_map";
            this.Text = "Photo of Map";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.photo_map_FormClosing);
            this.Load += new System.EventHandler(this.photo_map_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion
        private System.ComponentModel.BackgroundWorker backgroundWorker1;
        private System.Windows.Forms.Button btn_fb;
        private System.Windows.Forms.Button btn_twit;
        private System.Windows.Forms.Label lbl_date;
        private System.Windows.Forms.Button btn_left;
        private System.Windows.Forms.Button btn_right;
    }
}