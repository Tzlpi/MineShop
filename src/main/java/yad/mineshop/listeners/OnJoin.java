package yad.mineshop.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import yad.mineshop.MineShop;
import yad.mineshop.questsystem.gui.events.RewardsGUIClickEvent;
import yad.mineshop.utils.ChatUtils;

import java.io.IOException;
import java.util.UUID;

public class OnJoin implements Listener {

    private MineShop mineShop;
    public OnJoin(MineShop mineShop) {
        this.mineShop  = mineShop;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {

        try {

            Player player = event.getPlayer();
             UUID uuid = player.getUniqueId();
             PersistentDataContainer data = player.getPersistentDataContainer();

            RewardsGUIClickEvent.confirmed.put(player, 0);

            if (!(data.has(new NamespacedKey(mineShop, "tempcoins"), PersistentDataType.DOUBLE)))
             {
                 data.set(new NamespacedKey(mineShop, "tempcoins"), PersistentDataType.DOUBLE, 0.0);
             }

            if(!(data.has(new NamespacedKey(mineShop, "intask"), PersistentDataType.STRING))) {
                data.set(new NamespacedKey(mineShop, "intask"), PersistentDataType.STRING, "false");
            }


             mineShop.readStringFromURL(mineShop.addPlayer(uuid));

             if(mineShop.readStringFromURL( mineShop.checkRegistered(uuid)).contains("false")) {
                 player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aPlease make sure to register using /mineshop register <password> <password>"));
             }

             } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
}
