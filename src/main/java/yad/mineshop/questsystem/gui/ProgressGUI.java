package yad.mineshop.questsystem.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.utils.ChatUtils;
import yad.mineshop.utils.ItemBuilder;

public class ProgressGUI {

    private QuestManager questManager;

    public ProgressGUI(QuestManager questManager) {
        this.questManager = questManager;
    }


    public void openInv(Player player) {

        Inventory inv;
        inv = Bukkit.createInventory(player, 9, Component.text(ChatUtils.chat("&6Progress")));
        ItemStack glass = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName(" ").hideItemAttribute().toItemStack();

        for(int i = 0; i < 4; i++) {
            inv.setItem(i, glass);
        }

        for(int i = 5; i < 9; i++) {
            inv.setItem(i, glass);
        }

        switch(questManager.getTask(player)) {
            case "cow":
                ItemStack cow = new ItemBuilder(Material.COW_SPAWN_EGG).setName(ChatUtils.chat("&9Cow quest")).setLore("", ChatUtils.chat("&e&lProgress: &d"
                        + questManager.getProgress(player, "cowcount") + "/100")).hideItemAttribute().toItemStack();

                inv.setItem(4, cow);
                break;

            case "dragon":
                ItemStack dragon = new ItemBuilder(Material.DRAGON_HEAD).setName
                        (ChatUtils.chat("&5Dragon Quest")).setLore("", ChatUtils.chat("&e&lProgress: &dHaven't killed the dragon yet")).hideItemAttribute().toItemStack();

                inv.setItem(4, dragon);
                break;

            case "blocks":

                ItemStack pickaxe = new ItemBuilder(Material.NETHERITE_PICKAXE).setName(ChatUtils.chat("&7Blocks Quest")).setLore("",
                        ChatUtils.chat("&e&lProgress: &d" + questManager.getProgress(player, "blockcount") + "/10,000")).hideItemAttribute().toItemStack();

                inv.setItem(4, pickaxe);
                break;

            default:
                break;
        }

        player.openInventory(inv);
    }

}
