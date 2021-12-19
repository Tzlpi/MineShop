using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MineShop
{
    public partial class CustomDialog : Form
    {
        public dialogConfig diagConf;
        public bool execute = false;
        public string result;
        public CustomDialog()
        {
            InitializeComponent();
        }

        private void Custom_Dialog_Load(object sender, EventArgs e)
        {
            Text = diagConf.title;
            label1.Text = diagConf.message;
        }

        private void button10_Click(object sender, EventArgs e)
        {
            if (diagConf.withConfirm)
            {
                DialogResult digres = MessageBox.Show(diagConf.confirmMessage, diagConf.confirmTitle, MessageBoxButtons.YesNo);
                if (digres == DialogResult.Yes)
                {
                    execute = true;
                    result = textBox1.Text;
                    Hide();
                }
            }
            else
            {
                execute = true;
                result = textBox1.Text;
                Hide();
            }
        }
    }
    public class dialogConfig
    {
        public string title { get; set; }
        public string message { get; set; }
        public bool withConfirm { get; set; }
        public string confirmTitle { get; set; }
        public string confirmMessage { get; set; }
    }
}
