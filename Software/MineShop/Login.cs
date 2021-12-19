using Newtonsoft.Json.Linq;
using System;
using System.Net;
using System.Windows.Forms;

namespace MineShop
{
    public partial class Login : Form
    {
        public Login()
        {
            InitializeComponent();
        }
        
        private void Login_Load(object sender, EventArgs e)
        {
            
        }
        private void button1_Click(object sender, EventArgs e)
        {
            // check if all fields are filled
            // check if player is registered
            // check if player is online
            // login


            if (textBox1.Text == "")
            {
                MessageBox.Show("Username is empty!", "Error!", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            if (textBox2.Text == "")
            {
                MessageBox.Show("Password is empty!", "Error!", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
            string username = textBox1.Text;
            string password = textBox2.Text;
            Cursor.Current = Cursors.WaitCursor;
            string tempuuid = nameToUUID(username);
            if (tempuuid == "404")
            {
                Cursor.Current = Cursors.Default;
                MessageBox.Show("Minecraft Player: " + username + " does not exist.", "Error!", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
            string uuid = formatUUID(tempuuid);
            

            WebClient webClient = new WebClient();
            string res = webClient.DownloadString(Constants.ipAddress + $"api/main_data/isplayerregistered/{uuid}");
            if (res.Contains("false"))
            {
                Cursor.Current = Cursors.Default;
                MessageBox.Show("You are not registered!\nPlease register through the game using:\n\n/register <password> <password>"
                    , "Error!", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
            
            string res2 = webClient.DownloadString(Constants.ipAddress + $"api/online_data/checkifplayeronline/{uuid}");
            if (res2.Contains("false"))
            {
                Cursor.Current = Cursors.Default;
                MessageBox.Show("You are not logged in to the server!"
                    , "Error!", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            string res3 = webClient.DownloadString(Constants.ipAddress + $"api/main_data/checkplayerloggedin/{uuid}");
            if (res3.Contains("true"))
            {
                Cursor.Current = Cursors.Default;
                MessageBox.Show("Player is already logged from another location!"
                    , "Error!", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            string res4 = webClient.DownloadString(Constants.ipAddress + $"api/main_data/loginplayer/{uuid}/{password}");
            if (res4.Contains("true"))
            {
                //MessageBox.Show("Successfully logged in!\nUsername is: " + username + "\nUUID is: " + uuid);
                Main main = new Main();
                main.uuid = uuid;
                main.username = username;
                main.password = password;
                main.Show();
                this.Hide();
                //logged in
            }
            else
            {
                MessageBox.Show("Username or password is incorrect!", "Error!", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            Cursor.Current = Cursors.Default;
        }

        private void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            MessageBox.Show("In order to register, you have to do it through the server using the following command:\n\n" +
                "/register <password> <password>\n\n" +
                "Note: password must be at least 8 characters!", "Registration", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
        public string nameToUUID(string username)
        {
            WebClient webClient = new WebClient();
            long unix = getUnixTimeStamp();
            string json = webClient.DownloadString($"https://api.mojang.com/users/profiles/minecraft/{username}?at={unix}");
            if (json == "")
            {
                return "404";
            }
            dynamic stuff = JObject.Parse(json);
            string uuid = stuff.id;
            return uuid;
        }
        public long getUnixTimeStamp()
        {
            return DateTimeOffset.Now.ToUnixTimeSeconds();
        }
        public string formatUUID(string uuid)
        {
            Guid guid = new Guid();
            guid = Guid.Parse(uuid);
            return guid.ToString();
        }

        private void Login_FormClosing(object sender, FormClosingEventArgs e)
        {
            Application.Exit();
            Environment.Exit(0);
        }
    }
}
