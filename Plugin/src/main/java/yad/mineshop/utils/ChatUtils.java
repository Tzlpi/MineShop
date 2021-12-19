package yad.mineshop.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static String prefix() {
        return ChatUtils.chat("&6&l[MineShop]&r ");
    }

    public static String gotCoins(double amount) {
        return prefix() + "&aAdded " + amount + " coins to your account!";
    }

    public static String finishedQuest(String task) {
        return ChatUtils.chat(ChatUtils.prefix() + "&aFinished the quest&b " + task + "&a!");
    }
}
