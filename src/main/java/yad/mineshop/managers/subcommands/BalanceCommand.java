package yad.mineshop.managers.subcommands;

import org.bukkit.entity.Player;
import yad.mineshop.MineShop;
import yad.mineshop.managers.SubCommand;
import yad.mineshop.utils.ChatUtils;

import java.text.DecimalFormat;
import java.util.UUID;

public class BalanceCommand extends SubCommand {

    private MineShop mineShop;

    public BalanceCommand(MineShop mineShop) {
        this.mineShop = mineShop;
    }

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Returns the current balance you have!";
    }

    @Override
    public String getSyntax() {
        return "/mineshop balance";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "bal", "bl" };
    }

    @Override
    public void perform(Player player, String[] args) {

     try {

         UUID uuid = player.getUniqueId();

         String bal = mineShop.readStringFromURL( mineShop.getCoins(uuid));

         bal = bal.replace("\"", "");

         double balDouble = Double.parseDouble(bal);

         DecimalFormat formatter;

         if(balDouble % 1 == 0) {

             formatter = new DecimalFormat("#,###");
             player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aThe amount of coins you have is &e" + formatter.format(balDouble)));

         } else if(balDouble == 0) {

             player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aYou have 0 Coins! Activate a quest to earn some!"));

         }else if(balDouble % 0.5 == 0) {

             formatter = new DecimalFormat("#,###.0");
             player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aThe amount of coins you have is &e" + formatter.format(balDouble)));


         } else {

             formatter = new DecimalFormat("#,###.00");

             player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aThe amount of coins you have is &e" + formatter.format(balDouble)));

         }

     } catch (Exception e) {
         e.printStackTrace();
     }

    }
}
