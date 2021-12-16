package yad.mineshop.questsystem.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import yad.mineshop.MineShop;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.utils.ChatUtils;
import yad.mineshop.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class QuestGUI {

    List<Component> lore = new ArrayList<>();

    private QuestManager questManager;
    private MineShop mineShop;

    public QuestGUI(QuestManager questManager, MineShop mineShop) {
        this.questManager = questManager;
        this.mineShop = mineShop;
    }

    public void openInventory(Player player) {

        Inventory inv;
        inv = Bukkit.createInventory(player, 18, Component.text(ChatUtils.chat("&b&lQuests")));

        ItemStack[] items = new ItemStack[4];

        items[0] = new ItemBuilder(Material.DIAMOND).setName(ChatUtils.chat("&bDiamond Quest")).setLore(ChatUtils.chat(""),
                ChatUtils.chat("&aMine 100 diamond"), ChatUtils.chat("&a50 Coins")).hideItemAttribute().toItemStack();

        items[1] = new ItemBuilder(Material.DRAGON_HEAD).setName(ChatUtils.chat("&5Dragon Quest")).setLore("", ChatUtils.chat("&aSlay the ender dragon!"),
                ChatUtils.chat("&a100 Coins")).hideItemAttribute().toItemStack();

        items[2] = new ItemBuilder(Material.COW_SPAWN_EGG).setName(ChatUtils.chat("&9Cow Quest")).setLore("", ChatUtils.chat("&aKill 100 cows"),
                ChatUtils.chat("&a25 Coins")).hideItemAttribute().toItemStack();

        items[3] = new ItemBuilder(Material.NETHERITE_PICKAXE).setName(ChatUtils.chat("&7Blocks Quest")).setLore("",
                ChatUtils.chat("&aMine 10,000 blocks"), ChatUtils.chat("&6500 coins")).hideItemAttribute().toItemStack();

        for(int i = 0; i < 4; i++) {
            inv.setItem(i, items[i]);
        }

        player.openInventory(inv);

    }

}


