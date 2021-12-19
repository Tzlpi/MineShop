using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Runtime.ExceptionServices;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MineShop
{
    public partial class Main : Form
    {
        Dictionary<string, double> cart = new Dictionary<string, double>();
        List<string> toKill = new List<string>();
        List<Button> buttons = new List<Button>();
        public string announcement = "";
        public string username;
        public string uuid;
        public string password;
        public double coins;
        bool isCrashed = false;
        public class checkout
        {
            public int price { get; set; }
            public List<string> toKill { get; set; }
            public List<string> items { get; set; }
        }
        checkout CheckOutObj = new checkout();
        public Main()
        {
            InitializeComponent();
            //AppDomain.CurrentDomain.FirstChanceException += OnFirstChanceException;
            AppDomain.CurrentDomain.UnhandledException += UnhandledException;
        }

        private void UnhandledException(object sender, UnhandledExceptionEventArgs e)
        {
            if (!isCrashed)
            {
                WebClient webClient = new WebClient();
                webClient.DownloadString(Constants.ipAddress + $"api/main_data/logoutplayer/{uuid}/{password}");
                isCrashed = true;
            }
        }

        //private void OnFirstChanceException(object sender, FirstChanceExceptionEventArgs e)
        //{
        //    if (!isCrashed)
        //    {
        //        WebClient webClient = new WebClient();
        //        webClient.DownloadString(Constants.ipAddress + $"api/main_data/logoutplayer/{uuid}/{password}");
        //        isCrashed = true;
        //    }
        //}

        private void Main_Load(object sender, EventArgs e)
        {
            label4.Text = "User: " + username;
            WebClient webClient = new WebClient();
            string coinString = webClient.DownloadString(Constants.ipAddress + $"api/main_data/{uuid}/getcoins").Replace("\"", "");
            if (coinString == "error" || coinString.ToLower().Contains("incomplete"))
            {
                label3.Text = "Coins: error";
                return;
            }
            //MessageBox.Show("Errorrrrrrr");
            coins = Convert.ToDouble(coinString);
            if (coins % 1 == 0)
            {
                label3.Text = "Coins: " + coins.ToString("N0");
            }
            else
            {
                label3.Text = "Coins: " + coins.ToString("N");
            }
            
            timer1.Start();
            if (uuid == "82f78116-f0af-4162-bb28-77efd800bab4" || uuid == "0372a88a-2a02-4a75-9f51-e3bd93a23d96" || uuid == "5f4bbf74-3ef5-4034-82ca-4c585ddb46e3")
            {
                button11.Visible = true;
            }
            
            //MessageBox.Show("Username: " + username + "\nUUID: " + uuid + "\nPassword: " + password);
        }

        private void Main_FormClosing(object sender, FormClosingEventArgs e)
        {
            e.Cancel = true;
            WebClient webClient = new WebClient();
            webClient.DownloadString(Constants.ipAddress + $"api/main_data/logoutplayer/{uuid}/{password}");
            Application.Exit();
            Environment.Exit(0);
        }

        private void button10_Click(object sender, EventArgs e)
        {
            WebClient webClient = new WebClient();
            webClient.DownloadString(Constants.ipAddress + $"api/main_data/logoutplayer/{uuid}/{password}");
            Login login = new Login();
            login.Show();
            Hide();
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            new Thread(() =>
            {
                Thread.CurrentThread.IsBackground = true;
                WebClient webClient = new WebClient();
                string coinString = webClient.DownloadString(Constants.ipAddress + $"api/main_data/{uuid}/getcoins").Replace("\"", "");
                if (coinString == "error" || coinString.ToLower().Contains("incomplete"))
                {
                    label3.Invoke((MethodInvoker)delegate {
                        label3.Text = "Coins: error";
                    });
                    return;
                }
                coins = Convert.ToDouble(coinString);

                label3.Invoke((MethodInvoker) delegate {
                    if (coins % 1 == 0)
                    {
                        label3.Text = "Coins: " + coins.ToString("N0");
                    }
                    else
                    {
                        label3.Text = "Coins: " + coins.ToString("N");
                    }
                });

            }).Start();
            
        }

        private void button11_Click(object sender, EventArgs e)
        {
            Cursor.Current = Cursors.WaitCursor;
            AdminManager mgr = new AdminManager();
            mgr.ShowDialog();

        }
        public string sendReward(string contentString)
        {
            string json = @"{'number1': 'hello','number2': 'world'}";

            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.TryAddWithoutValidation("Content-Type", "application/json; charset=utf-8");
            
                var content = new StringContent(json, Encoding.UTF8, "application/json");
                //var response = client.PostAsync("https://api.jsonstorage.net/v1/json", content);
                var response = client.PostAsync("https://localhost:44307", content);
                var responseContent = response.Result.Content.ReadAsStringAsync();
                string responseString = responseContent.Result;
                return responseString;
            }


            //HttpClient client = new HttpClient();
            //
            //client.DefaultRequestHeaders.TryAddWithoutValidation("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            //
            //var values = new Dictionary<string, string>
            //{
            //    { "api_dev_key", "73373e3a72824096c39230f07290ef69" },
            //    { "api_option", "paste" },
            //    { "api_paste_code", contentString}
            //};
            //
            //var content = new FormUrlEncodedContent(values);
            //
            //var response = await client.PostAsync("https://pastebin.com/api/api_post.php", content);
            //
            //var responseString = await response.Content.ReadAsStringAsync();
            //string response = responseString.Result;




        }

        private void button1_Click(object sender, EventArgs e)
        {
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the amount of diamonds you would like to add to the cart:";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();

            int amount = 0;
            try
            {
                amount = Convert.ToInt32(customDiag.result);
            }
            catch (Exception)
            {
                MessageBox.Show("Amount invalid!", "Mineshop");
                return;
            }
            if (customDiag.execute)
            {
                if (cart.ContainsKey("264"))
                {
                    double existingKey = cart["264"];
                    double newKey = (amount + existingKey);
                    cart.Remove("264");
                    cart.Add("264", newKey);
                }
                else
                {
                    cart.Add("264", amount);
                }
                button1.Enabled = false;
                button1.Text = "IN CART";
                buttons.Add(button1);
                MessageBox.Show("Successfully added to cart!", "Mineshop");
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the amount of diamond swords you would like to add to the cart:";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();

            int amount = 0;
            try
            {
                amount = Convert.ToInt32(customDiag.result);
            }
            catch (Exception)
            {
                MessageBox.Show("Amount invalid!", "Mineshop");
                return;
            }
            if (customDiag.execute)
            {
                if (cart.ContainsKey("276"))
                {
                    double existingKey = cart["276"];
                    double newKey = (amount + existingKey);
                    cart.Remove("276");
                    cart.Add("276", newKey);
                }
                else
                {
                    cart.Add("276", amount);
                }
                button2.Enabled = false;
                button2.Text = "IN CART";
                buttons.Add(button2);
                MessageBox.Show("Successfully added to cart!", "Mineshop");
            }
        }

        private void label5_Click(object sender, EventArgs e)
        {

        }

        private void button14_Click(object sender, EventArgs e)
        {
            bool isAutomated = false;
            if (sender.ToString() == "noCheck")
            {
                isAutomated = true;
            }

            DialogResult digres;
            if (!isAutomated)
            {
                digres = MessageBox.Show("Are you sure you want to clear the cart?", "Mineshop", MessageBoxButtons.YesNo);
                if (digres != DialogResult.Yes)
                {
                    return;
                }
            }

            cart.Clear();
            toKill.Clear();
            announcement = "";
            foreach (Button button in buttons)
            {
                button.Enabled = true;
                button.Text = "ADD TO CART";
            }
            buttons.Clear();
            if (!isAutomated)
            {
                MessageBox.Show("Successfully cleared your cart!", "Mineshop");
            }
        }

        private void button5_Click(object sender, EventArgs e)
        {
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the amount of crafting tables you would like to add to the cart:";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();

            int amount = 0;
            try
            {
                amount = Convert.ToInt32(customDiag.result);
            }
            catch (Exception)
            {
                MessageBox.Show("Amount invalid!", "Mineshop");
                return;
            }
            if (customDiag.execute)
            {
                if (cart.ContainsKey("58"))
                {
                    double existingKey = cart["58"];
                    double newKey = (amount + existingKey);
                    cart.Remove("58");
                    cart.Add("58", newKey);
                }
                else
                {
                    cart.Add("58", amount);
                }
                button5.Enabled = false;
                button5.Text = "IN CART";
                buttons.Add(button5);
                MessageBox.Show("Successfully added to cart!", "Mineshop");
            }
        }

        private void button6_Click(object sender, EventArgs e)
        {
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the amount of furnaces you would like to add to the cart:";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();

            int amount = 0;
            try
            {
                amount = Convert.ToInt32(customDiag.result);
            }
            catch (Exception)
            {
                MessageBox.Show("Amount invalid!", "Mineshop");
                return;
            }
            if (customDiag.execute)
            {
                if (cart.ContainsKey("61"))
                {
                    double existingKey = cart["61"];
                    double newKey = (amount + existingKey);
                    cart.Remove("61");
                    cart.Add("61", newKey);
                }
                else
                {
                    cart.Add("61", amount);
                }
                button6.Enabled = false;
                button6.Text = "IN CART";
                buttons.Add(button6);
                MessageBox.Show("Successfully added to cart!", "Mineshop");
            }
        }

        private void button7_Click(object sender, EventArgs e)
        {
            if (cart.ContainsKey("3"))
            {
                double existingKey = cart["3"];
                double newKey = (32 + existingKey);
                cart.Remove("3");
                cart.Add("3", newKey);
            }
            else
            {
                cart.Add("3", 32);
            }
            button7.Enabled = false;
            button7.Text = "IN CART";
            buttons.Add(button7);
            MessageBox.Show("Successfully added to cart!", "Mineshop");
        }

        private void button8_Click(object sender, EventArgs e)
        {
            if (cart.ContainsKey("4"))
            {
                double existingKey = cart["4"];
                double newKey = (32 + existingKey);
                cart.Remove("4");
                cart.Add("4", newKey);
            }
            else
            {
                cart.Add("4", 32);
            }
            button8.Enabled = false;
            button8.Text = "IN CART";
            buttons.Add(button8);
            MessageBox.Show("Successfully added to cart!", "Mineshop");
        }

        private void button9_Click(object sender, EventArgs e)
        {
            if (cart.ContainsKey("265"))
            {
                double existingKey = cart["265"];
                double newKey = (15 + existingKey);
                cart.Remove("265");
                cart.Add("265", newKey);
            }
            else
            {
                cart.Add("265", 15);
            }
            button9.Enabled = false;
            button9.Text = "IN CART";
            buttons.Add(button9);
            MessageBox.Show("Successfully added to cart!", "Mineshop");
        }

        private void button12_Click(object sender, EventArgs e)
        {
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the name of the person you would like to kill\nNote: entering the wrong name will waste your coins!";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string res = customDiag.result;

            if (customDiag.execute)
            {
                string uuid = nameToUUID(res).Replace("\"", "");
                if (uuid == "404")
                {
                    MessageBox.Show("Name Invalid!", "Mineshop");
                    return;
                }
                if (res.ToLower() == username.ToLower())
                {
                    MessageBox.Show("You can't buy a Kill Person Item on yourself!", "Mineshop");
                    return;
                }
                toKill.Add(formatUUID(uuid));
                //button12.Enabled = false;
                button12.Text = "ADD ANOTHER";
                buttons.Add(button12);
                MessageBox.Show("Successfully added to cart!", "Mineshop");
            }
        }

        private void button13_Click(object sender, EventArgs e)
        {
            if (cart.ContainsKey("49"))
            {
                double existingKey = cart["49"];
                double newKey = (2 + existingKey);
                cart.Remove("49");
                cart.Add("49", newKey);
            }
            else
            {
                cart.Add("49", 2);
            }
            button13.Enabled = false;
            button13.Text = "IN CART";
            buttons.Add(button13);
            MessageBox.Show("Successfully added to cart!", "Mineshop");
        }

        private void button4_Click(object sender, EventArgs e)
        {
            if (toKill.Count == 0 && cart.Count == 0 && announcement == "")
            {
                MessageBox.Show("Your cart is empty!", "Mineshop");
                return;
            }

            cart Cart = new cart();
            Cart.cartDict = cart;
            Cart.toKill = toKill;
            Cart.MainForm = this;
            Cart.playeruuid = uuid;
            Cart.announcement = announcement;
            Cart.ShowDialog();
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
        public void afterPurchase()
        {
            timer1_Tick("me", EventArgs.Empty);
            button14_Click("noCheck", EventArgs.Empty);
            announcement = "";
        }

        private void button3_Click(object sender, EventArgs e)
        {
            CustomDialog customDiag = new CustomDialog();
            dialogConfig diag = new dialogConfig();
            diag.title = "Mineshop";
            diag.message = "Please enter the content of the message that will be shown in the annoucements:";
            diag.withConfirm = false;
            customDiag.diagConf = diag;
            customDiag.ShowDialog();
            string res = customDiag.result;

            if (customDiag.execute)
            {
                if (res == "")
                {
                    MessageBox.Show("Text field is empty!", "Mineshop");
                    return;
                }
                announcement = res;
                button3.Enabled = false;
                button3.Text = "IN CART";
                buttons.Add(button3);
                MessageBox.Show("Successfully added to cart!", "Mineshop");
            }
        }
    }
}

