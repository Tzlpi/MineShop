package yad.mineshop.questsystem.gui.events;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.utils.ChatUtils;

public class QuestGUIClickEvent implements Listener {

    private QuestManager questManager;
    public QuestGUIClickEvent(QuestManager questManager) {
        this.questManager = questManager;
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
            case NETHERITE_PICKAXE:
                questManager.createDataType(player, "blockcount", PersistentDataType.INTEGER, null);
                questManager.setTask(player,"blocks");
                break;

        }

        inv.close();
        player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aStarted the quest " + questManager.getTask(player)));

        questManager.setState(player, true);

        e.setCancelled(true);
    }
}
