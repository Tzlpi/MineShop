using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace MineShop
{
    public static class Hook
    {
        //List<string>
        public static void SendMessage(string content, string username, string image, string url)
        {
                HttpClient client = new HttpClient();
                Dictionary<string, string> contents = new Dictionary<string, string>
                    {
                        { "content", content},
                        { "username", username },
                        { "avatar_url", image }
                    };

                client.PostAsync(url, new FormUrlEncodedContent(contents)).GetAwaiter().GetResult();
        }
    }
}

