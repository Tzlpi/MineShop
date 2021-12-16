using Microsoft.VisualBasic;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MineShop
{
    public partial class AdminManager : Form
    {
        string[] uuids;
        string[] formatteduuids;
        string[] names;
        string[] quests;
        double[] coins;
        bool firstReload = true;
        public AdminManager()
        {
            InitializeComponent();
        }

        private void AdminManager_FormClosing(object sender, FormClosingEventArgs e)
        {
            
            
        }
        public string formatUUID(string uuid)
        {
            Guid guid = new Guid();
            guid = Guid.Parse(uuid);
            return guid.ToString();
        }

        private void AdminManager_Load(object sender, EventArgs e)
        {
            WebClient webClient = new WebClient();
            string res = webClient.DownloadString(Constants.ipAddress + "api/online_data/getallplayers");
            res = res.Replace(@"\", "");
            res = res.Substring(1);
            res = res.Remove(res.Length - 1);
            if (res == "[]")
            {
                listBox1.Items.Clear();
                return;
            }
            dynamic stuff = JObject.Parse(res);
            uuids = stuff.uuids.ToObject<string[]>();
            names = stuff.names.ToObject<string[]>();

            List<string> list01 = new List<string>();
            foreach (string uuid in uuids)
            {
                list01.Add(formatUUID(uuid));
            }
            formatteduuids = list01.ToArray();

            listBox1.Items.Clear();
            foreach (string name in names)
            {
                listBox1.Items.Add(name);
            }

            if (firstReload)
            {
                pictureBox1.Visible = false;
                foreach (string uuid in uuids)
                {
                    pictureBox1.Load($"https://crafatar.com/renders/body/{uuid}?overlay&size=10");
                }
                pictureBox1.Image = null;
                pictureBox1.Visible = true;
                firstReload = false;
            }
            List<double> list02 = new List<double>();
            foreach (string uuid in formatteduuids)
            {
                WebClient webClient2 = new WebClient();
                string coinsString = webClient2.DownloadString(
                    Constants.ipAddress + $"api/main_data/{uuid}/getcoins").Replace("\"", "");
                double coins = Convert.ToDouble(coinsString);
                list02.Add(coins);
            }
            coins = list02.ToArray();

            List<string> list03 = new List<string>();
            foreach (string uuid in formatteduuids)
            {
                WebClient webClient2 = new WebClient();
                string questString = webClient2.DownloadString(
                    Constants.ipAddress + $"api/main_data/{uuid}/getquest").Replace("\"", "");
                list03.Add(questString);
            }
            quests = list03.ToArray();

            Cursor.Current = Cursors.Default;
        }

        private void listBox1_SelectedValueChanged(object sender, EventArgs e)
        {
            if (listBox1.SelectedIndex != -1)
            {
                button1.Enabled = true;
                button2.Enabled = true;
                button3.Enabled = true;
                button6.Enabled = true;
                button5.Enabled = true;
                button4.Enabled = true;
                button9.Enabled = true;
                button8.Enabled = true;
                button7.Enabled = true;
                //button11.Enabled = true;
                button12.Enabled = true;
                //button13.Enabled = true;
                button14.Visible = true;
                button15.Visible = true;
            }
            else
            {
                return;
            }
            Cursor.Current = Cursors.WaitCursor;
            
            int index = listBox1.SelectedIndex;
            string uuid = formatteduuids[index];
            string name = names[index];
            pictureBox1.Load($"https://crafatar.com/renders/body/{uuid}?overlay&size=10");
            label1.Text = name;
            label2.Text = "UUID: " + uuid;
            string coinString;
            if (coins[index] % 1 == 0)
            {
                coinString = "Coins: " + coins[index].ToString("N0");
            }
            else
            {
                coinString = "Coins: " + coins[index].ToString("N");
            }

            label3.Text = coinString;
            label5.Text = "Active Quest: " + System.Globalization.CultureInfo.CurrentCulture.TextInfo.ToTitleCase(quests[index]);
            Cursor.Current = Cursors.Default;
        }

        private void button10_Click(object sender, EventArgs e)
        {
            if (listBox1.SelectedIndex == -1)
            {
                MessageBox.Show("Please select a player from the list in order to reload.", "Mineshop");
                return;
            }
            Cursor.Current = Cursors.WaitCursor;
            AdminManager_Load("me", EventArgs.Empty);
            listBox1_SelectedValueChanged("me", EventArgs.Empty);
            Cursor.Current = Cursors.Default;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            int index = listBox1.SelectedIndex;
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter a password for this user in order to register him.";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string password = customDiag.result;
            if (customDiag.execute)
            {
                WebClient webClient = new WebClient();
                string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/registerplayer/{formatteduuids[index]}/{password}");
                if (res == "\"true\"")
                {
                    MessageBox.Show("Player registered successfully!", "Mineshop");
                }
                else if (res == "\"false\"")
                {
                    MessageBox.Show("Error!", "Mineshop");
                }
            }
        }

        private void button14_Click(object sender, EventArgs e)
        {
            int index = listBox1.SelectedIndex;
            Clipboard.SetText(formatteduuids[index]);
            MessageBox.Show("Successfully Copied UUID to clipboard!", "Mineshop");
        }

        private void button3_Click(object sender, EventArgs e)
        {
            int index = listBox1.SelectedIndex;
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter a password for this user in order to unregister him.";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string password = customDiag.result;
            if (customDiag.execute)
            {
                WebClient webClient = new WebClient();
                string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/unregisterplayer/{formatteduuids[index]}/{password}");
                if (res == "\"true\"")
                {
                    MessageBox.Show("Player unregistered successfully!", "Mineshop");
                }
                else if (res == "\"false\"")
                {
                    MessageBox.Show("Error!", "Mineshop");
                }
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            int index = listBox1.SelectedIndex;
            WebClient webClient = new WebClient();
            string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/isplayerregistered/{formatteduuids[index]}");
            if (res == "\"true\"")
            {
                MessageBox.Show("Player is registered.", "Mineshop");
            }
            else if (res == "\"false\"")
            {
                MessageBox.Show("Player is not registered.", "Mineshop");
            }
        }

        private void button6_Click(object sender, EventArgs e)
        {
            //log in
            int index = listBox1.SelectedIndex;
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter a password for this user in order to log him in.";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string password = customDiag.result;
            if (customDiag.execute)
            {
                WebClient webClient = new WebClient();
                string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/loginplayer/{formatteduuids[index]}/{password}");
                if (res == "\"true\"")
                {
                    MessageBox.Show("Player successfully logged in!", "Mineshop");
                }
                else if (res == "\"false\"")
                {
                    MessageBox.Show("Error!", "Mineshop");
                }
            }
        }

        private void button5_Click(object sender, EventArgs e)
        {
            //check log in
            int index = listBox1.SelectedIndex;
            WebClient webClient = new WebClient();
            string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/checkplayerloggedin/{formatteduuids[index]}");
            if (res == "\"true\"")
            {
                MessageBox.Show("Player is logged in.", "Mineshop");
            }
            else if (res == "\"false\"")
            {
                MessageBox.Show("Player is logged out.", "Mineshop");
            }
        }

        private void button4_Click(object sender, EventArgs e)
        {
            //log out
            int index = listBox1.SelectedIndex;
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter a password for this user in order to log him out.";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string password = customDiag.result;
            if (customDiag.execute)
            {
                WebClient webClient = new WebClient();
                string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/logoutplayer/{formatteduuids[index]}/{password}");
                if (res == "\"true\"")
                {
                    MessageBox.Show("Player successfully logged out!", "Mineshop");
                }
                else if (res == "\"false\"")
                {
                    MessageBox.Show("Error!", "Mineshop");
                }
            }
        }

        private void button9_Click(object sender, EventArgs e)
        {
            //add coins
            int index = listBox1.SelectedIndex;
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the amount of coins you would like to add to this player:";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string coins = customDiag.result;
            if (customDiag.execute)
            {
                WebClient webClient = new WebClient();
                string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/{formatteduuids[index]}/addcoins/{coins}/");
                if (res == "\"true\"")
                {
                    MessageBox.Show("Successfully added " + Convert.ToInt32(coins).ToString("N0") + " coins to Player.", "Mineshop");
                }
                else if (res == "\"false\"")
                {
                    MessageBox.Show("Error!", "Mineshop");
                }
            }
        }

        private void button8_Click(object sender, EventArgs e)
        {
            //set coins
            int index = listBox1.SelectedIndex;
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the amount of coins you would like to set this player's coins to:";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string coins = customDiag.result;
            if (customDiag.execute)
            {
                WebClient webClient = new WebClient();
                string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/{formatteduuids[index]}/setcoins/{coins}/");
                if (res == "\"true\"")
                {
                    MessageBox.Show("Successfully set player's coins to: " + Convert.ToInt32(coins).ToString("N0"), "Mineshop");
                }
                else if (res == "\"false\"")
                {
                    MessageBox.Show("Error!", "Mineshop");
                }
            }
        }

        private void button7_Click(object sender, EventArgs e)
        {
            //remove coins
            int index = listBox1.SelectedIndex;
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the amout of coins you would like to remove from this player:";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string coins = customDiag.result;
            if (customDiag.execute)
            {
                WebClient webClient = new WebClient();
                string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/{formatteduuids[index]}/removecoins/{coins}/");
                if (res == "\"true\"")
                {
                    MessageBox.Show("Successfully removed " + Convert.ToInt32(coins).ToString("N0") + " coins from Player.", "Mineshop");
                }
                else if (res == "\"false\"")
                {
                    MessageBox.Show("Error!", "Mineshop");
                }
            }
        }

        private void button13_Click(object sender, EventArgs e)
        {
            //add player
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the uuid of the player you would like to make online";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string uuid = customDiag.result;
            if (customDiag.execute)
            {
                try
                {
                    Guid g = new Guid();
                    g = Guid.Parse(uuid);
                }
                catch
                {
                    MessageBox.Show("UUID is not valid.");
                    return;
                }

                WebClient webClient = new WebClient();
                string res = webClient.DownloadString(Constants.ipAddress + $"api/online_data/addplayer/{uuid}");
                if (res == "\"true\"")
                {
                    MessageBox.Show("Successfully made this player online.", "Mineshop");
                }
                else if (res == "\"false\"")
                {
                    MessageBox.Show("Error!", "Mineshop");
                }
            }
        }

        private void button12_Click(object sender, EventArgs e)
        {
            //check if player is online
            int index = listBox1.SelectedIndex;
            WebClient webClient = new WebClient();
            string res = webClient.DownloadString(Constants.ipAddress + $"api/online_data/checkifplayeronline/{formatteduuids[index]}");
            if (res == "\"true\"")
            {
                MessageBox.Show("Player is online.", "Mineshop");
            }
            else if (res == "\"false\"")
            {
                MessageBox.Show("Player is offline.", "Mineshop");
            }
        }

        private void button11_Click(object sender, EventArgs e)
        {
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the uuid of the player you would like to make offline";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string uuid = customDiag.result;
            if (customDiag.execute)
            {
                try
                {
                    Guid g = new Guid();
                    g = Guid.Parse(uuid);
                }
                catch
                {
                    MessageBox.Show("UUID is not valid.");
                    return;
                }

                WebClient webClient = new WebClient();
                string res = webClient.DownloadString(Constants.ipAddress + $"api/online_data/removeplayer/{uuid}");
                if (res == "\"true\"")
                {
                    MessageBox.Show("Successfully made this player offline.", "Mineshop");
                }
                else if (res == "\"false\"")
                {
                    MessageBox.Show("Error!", "Mineshop");
                }
            }
        }

        private void button15_Click(object sender, EventArgs e)
        {
            int index = listBox1.SelectedIndex;
            WebClient webClient = new WebClient();
            string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/getplayerpassword/{formatteduuids[index]}/GHSRTYHJ$$/");
            res = res.Replace("\"", "");
            MessageBox.Show("Player's password is: " + res);
        }
    }
    //show player password on a button
}
