package yad.mineshop.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import yad.mineshop.MineShop;

import java.io.IOException;
import java.util.UUID;

public class OnQuit implements Listener {

    private MineShop mineShop;
    public OnQuit(MineShop mineShop) {
        this.mineShop = mineShop;
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws IOException {
        try {
            Player player = e.getPlayer();
            UUID uuid = player.getUniqueId();

           mineShop.readStringFromURL(mineShop.removePlayer(uuid));
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
