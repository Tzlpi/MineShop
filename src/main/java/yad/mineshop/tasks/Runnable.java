package yad.mineshop.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import yad.mineshop.MineShop;
import yad.mineshop.utils.ChatUtils;

import java.util.UUID;

public class Runnable extends BukkitRunnable implements Listener {

    private MineShop mineShop;
    public Runnable( MineShop mineShop) {
        this.mineShop = mineShop;
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            PersistentDataContainer data = player.getPersistentDataContainer();
            double current = 0;

            if (data.has(new NamespacedKey(mineShop, "tempcoins"), PersistentDataType.DOUBLE))
            {
                current = data.get(new NamespacedKey(mineShop, "tempcoins"), PersistentDataType.DOUBLE);
            }
            if (current != 0)
            {
                UUID uuid = player.getUniqueId();

                try
                {
                    mineShop.readStringFromURL(mineShop.addCoins(uuid, current));
                    data.set(new NamespacedKey(mineShop, "tempcoins"), PersistentDataType.DOUBLE, 0.0);
                } catch (Exception ex)
                {

                }
            }


        }

    }
}

