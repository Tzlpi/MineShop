package yad.mineshop.questsystem;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import yad.mineshop.MineShop;
import yad.mineshop.managers.QuestManager;
import yad.mineshop.utils.ChatUtils;
import yad.mineshop.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class GUI implements Listener {

    List<Component> lore = new ArrayList<>();

    private QuestManager questManager;
    private MineShop mineShop;

    public GUI(QuestManager questManager, MineShop mineShop) {
        this.questManager = questManager;
        this.mineShop = mineShop;
    }

    public void openInventory(Player player) {

        Inventory inv;
        inv = Bukkit.createInventory(player, 18, Component.text(ChatUtils.chat("&b&lQuests")));

        ItemStack[] items = new ItemStack[3];

        items[0] = new ItemBuilder(Material.DIAMOND).setName(ChatUtils.chat("&bDiamond Quest")).setLore(ChatUtils.chat(""),
                ChatUtils.chat("&aMine 100 diamond"), ChatUtils.chat("&a50 Coins")).toItemStack();

        items[1] = new ItemBuilder(Material.DRAGON_HEAD).setName(ChatUtils.chat("&5Dragon advancement")).setLore("", ChatUtils.chat("&aGet the dragon advancement"),
                ChatUtils.chat("&a100 Coins")).toItemStack();

        items[2] = new ItemBuilder(Material.COW_SPAWN_EGG).setName(ChatUtils.chat("&9Cow Quest")).setLore("", ChatUtils.chat("&aKill 100 cows"),
                ChatUtils.chat("&a25 Coins")).toItemStack();

        for(int i = 0; i < 3; i++) {
            inv.setItem(i, items[i]);
        }
        player.openInventory(inv);

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {


        if (!e.getView().title().equals(Component.text(ChatUtils.chat("&b&lQuests")))) return;
        Inventory inv = e.getClickedInventory();

        Player player = (Player) e.getView().getPlayer();

        PersistentDataContainer data = player.getPersistentDataContainer();

       switch(e.getCurrentItem().getType()) {
           case DIAMOND:
               questManager.createDataType(player, "diamondcount", PersistentDataType.INTEGER, null);
               questManager.setTask(player, "diamonds");
               break;
           case DRAGON_HEAD:
               questManager.setTask(player, "dragon");
               break;
           case COW_SPAWN_EGG:
               questManager.createDataType(player, "cowcount", PersistentDataType.INTEGER, null);
               questManager.setTask(player, "cow");
               break;

       }

        inv.close();
        player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aStarted the quest " + questManager.getTask(player)));

        questManager.setState(player, true);

        e.setCancelled(true);
    }
}


