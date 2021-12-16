using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MineShop
{
    public partial class cart : Form
    {
        public Dictionary<string, double> cartDict { get; set; }
        public List<string> toKill { get; set; }
        public Main MainForm { get; set; }
        public string playeruuid { get; set; }

        public double totalPrice = 0;
        public string announcement { get; set; }
        public cart()
        {
            InitializeComponent();
        }

        private void cart_Load(object sender, EventArgs e)
        {
            Dictionary<string, double> dictionary = new Dictionary<string, double>();
            WebClient client = new WebClient();
            string json = client.DownloadString(Constants.ipAddress + "api/main_data/getprices");
            json = json.Remove(0, 1);
            json = json.Remove(json.Length - 1);
            json = json.Replace("\\", "");
            dynamic res = JsonConvert.DeserializeObject<dynamic>(json);
            foreach (var result in res)
            {
                //dynamic stuff = JsonConvert.DeserializeObject(result.ToString());
                string id = result.Name;
                string price = result.Value;
                dictionary.Add(id, Convert.ToDouble(price));
            }
            foreach (KeyValuePair<string, double> kvp in cartDict)
            {
                double val = dictionary[kvp.Key] * kvp.Value;
                string valString = val.ToString();
                if (valString.Contains(".000"))
                {
                    val = Math.Round(val);
                }
                totalPrice = totalPrice + val;
                listBox1.Items.Add("Item: " + getNameByID(kvp.Key) + ", Count: " + kvp.Value + ", Total: " + val);
            }
            foreach (string target in toKill)
            {
                totalPrice = totalPrice + 200;
                listBox1.Items.Add("Person To Kill: " + getNameByUUID(target) + ", Total: " + 200);
            }
            if (announcement != "")
            {
                totalPrice = totalPrice + 20;
                string message;
                if (announcement.Length > 28)
                {
                    message = announcement.Substring(0, 28) + "...";
                }
                else
                {
                    message = announcement;
                }
                listBox1.Items.Add("Discord Announcement: " + message + ", Total: " + 20.ToString());
            }
            if (totalPrice % 1 == 0)
            {
                label3.Text = "Price: " + totalPrice.ToString("N0");
            }
            else
            {
                label3.Text = "Price: " + totalPrice.ToString("N");
            }
        }
        public int sendWebHook(string content)
        {
            using (WebClient webClient = new WebClient())
            {
                try
                { 
                    string json = webClient.DownloadString(Constants.ipAddress + "api/main_data/getwebhookdata/");
                    json = json.Remove(0, 1);
                    json = json.Remove(json.Length - 1);
                    json = json.Replace("\\", "");
                    if (json.Contains("error"))
                    {
                        return 1;
                    }
                    dynamic jsonObj = JsonConvert.DeserializeObject<dynamic>(json);


                    Hook.SendMessage(
                        "**Message that was bought with MineShop:**\n" + "`" + content + "`" + "\n@everyone",
                        "MineShop Announcer",
                        jsonObj.image.ToString(),
                        jsonObj.url.ToString());
                    return 0;
                }
                catch (Exception)
                {
                    return 1;
                }
                
            }
        }
        public string getNameByID(string id)
        {
            WebClient webClient = new WebClient();
            string json = webClient.DownloadString(@"https://minecraft-ids.grahamedgecombe.com/items.json");

            dynamic results = JsonConvert.DeserializeObject<dynamic>(json);
            foreach (var result in results)
            {
                dynamic stuff = JsonConvert.DeserializeObject(result.ToString());
                string type = stuff.type;
                string name = stuff.name;
                if (id == type)
                {
                    return name;
                }
            }
            return "404";
        }
        public string getNameByUUID(string uuid)
        {
            WebClient webClient = new WebClient();
            string json = webClient.DownloadString($"https://api.ashcon.app/mojang/v2/user/{uuid}");
            dynamic res = JsonConvert.DeserializeObject<dynamic>(json);
            string name = res.username;
            return name;
        }

        private void button3_Click(object sender, EventArgs e)
        {
            //buy
            string[] tokill = toKill.ToArray();
            int[] items;
            List<int> vs = new List<int>();
            foreach (var item in cartDict)
            {
                for (int i = 0; i < item.Value; i++)
                {
                    vs.Add(Convert.ToInt32(item.Key));
                }
            }
            items = vs.ToArray();
            DialogResult digres = MessageBox.Show("Are you sure you want to buy these items for " + totalPrice.ToString("N") + " Coins?",
                "Mineshop", MessageBoxButtons.YesNo);
            if (digres == DialogResult.Yes)
            {
                //check if balance is enough
                WebClient webClient = new WebClient();
                string result = webClient.DownloadString(Constants.ipAddress + $"api/main_data/{playeruuid}/getcoins");
                double coins = Convert.ToDouble(result.Replace("\"", ""));
                if (totalPrice > coins)
                {
                    MessageBox.Show("You don't have enough coins!", "Mineshop", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                Reward reward = new Reward();
                reward.toKill = tokill;
                reward.items = items;
                string jsonString = JsonConvert.SerializeObject(reward);
                Cursor.Current = Cursors.WaitCursor;
                int hookresult = 0;
                if (announcement != "")
                {
                    hookresult = sendWebHook(announcement);
                    if (totalPrice == 20 && hookresult == 0)
                    {
                        using (WebClient client = new WebClient())
                        {
                            string clresult = client.DownloadString(Constants.ipAddress + $"api/main_data/{playeruuid}/removecoins/20/");
                        }
                    }
                    if (hookresult == 1)
                    {
                        totalPrice = totalPrice - 20;
                        MessageBox.Show("Annoucements are unavailable at the moment!\nWe will remove the announcement from your cart!",
                            "Mineshop");
                        if (totalPrice == 0)
                        {
                            MessageBox.Show("Other than the announcement, which is unavailable, your cart is empty!", "Mineshop");
                            Hide();
                            return;
                        }
                    }
                    if (totalPrice == 20)
                    {
                        using (WebClient client = new WebClient())
                        {
                            string clresult = client.DownloadString(Constants.ipAddress + $"api/main_data/{playeruuid}/removecoins/20/");
                            if (clresult.Contains("false"))
                            {
                                MessageBox.Show("Internal Error!", "Mineshop");
                            }
                        }
                        Cursor.Current = Cursors.Default;
                        MessageBox.Show("Bought items successfully!\n" +
                        "Thank you for buying at the Mineshop!", "Mineshop", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        Hide();
                        Main main = MainForm;
                        main.afterPurchase();
                        announcement = "";
                        
                        //charge
                        return;
                    }
                }
                string res = postRequest(jsonString);
                Cursor.Current = Cursors.Default;
                if (res.Contains("true"))
                {
                    MessageBox.Show("Bought items successfully!\n" +
                    "Please claim these in the server using /mineshop claimrewards\n" +
                    "Thank you for buying at the Mineshop!", "Mineshop", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    Hide();
                    Main main = MainForm;
                    main.afterPurchase();
                }
                else if (res.Contains("taken"))
                {
                    MessageBox.Show("You already bought something!\n" +
                        "in order to make a new order, you have to claim the one you already did.\n" +
                        "use /mineshop claimrewards to claim it.", "Mineshop", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    if (hookresult == 1)
                    {
                        totalPrice = totalPrice + 20;
                    }
                    return;
                }
                else if (res.Contains("false"))
                {
                    MessageBox.Show("Internal Error!\nPlease try again later!",
                        "Mineshop", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    if (hookresult == 1)
                    {
                        totalPrice = totalPrice + 20;
                    }
                    return;
                }
            }
        }
        public string postRequest(string payload)
        {
            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.TryAddWithoutValidation("Content-Type", "application/json; charset=utf-8");
                var content = new StringContent(payload, Encoding.UTF8, "application/json");
                var response = client.PostAsync(Constants.ipAddress + $"api/main_data/{playeruuid}/setreward/{totalPrice}/", content);
                var responseContent = response.Result.Content.ReadAsStringAsync();
                string responseString = responseContent.Result;
                return responseString;
            }
        }
    }
    public class Reward
    {
        public int[] items { get; set; }
        public string[] toKill { get; set; }
    }
}

