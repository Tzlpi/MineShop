package yad.mineshop.questsystem.gui.events;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import yad.mineshop.utils.ChatUtils;

public class ProgressGUIClickEvent implements Listener {

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {

        if(!event.getView().title().equals(Component.text(ChatUtils.chat("&6Progress")))) return;

         event.getInventory().close();

    }

}
