package yad.mineshop.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import yad.mineshop.MineShop;
import yad.mineshop.utils.ChatUtils;

public class DiamondBreak implements Listener {

    private MineShop mineShop;
    public DiamondBreak(MineShop mineShop) {
        this.mineShop = mineShop;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        Player player = e.getPlayer();

        if(e.getBlock().getType().equals(Material.DIAMOND_ORE) || e.getBlock().getType().equals(Material.DEEPSLATE_DIAMOND_ORE)) {
            PersistentDataContainer data = player.getPersistentDataContainer();

            //diamond broken

            double currentTemp = data.get(new NamespacedKey(mineShop, "tempcoins"), PersistentDataType.DOUBLE);

            data.set(new NamespacedKey(mineShop, "tempcoins"), PersistentDataType.DOUBLE, currentTemp + 0.5);

            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aAdded 0.5 coins to your account!"));
            double current = data.get(new NamespacedKey(mineShop, "tempcoins"), PersistentDataType.DOUBLE);

        }
    }
}
