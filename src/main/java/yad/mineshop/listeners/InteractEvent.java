package yad.mineshop.listeners;

import com.sun.tools.javac.util.Names;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import yad.mineshop.MineShop;
import yad.mineshop.utils.ChatUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class InteractEvent implements Listener {

    private MineShop mineShop;
    public InteractEvent(MineShop mineShop) {
        this.mineShop = mineShop;
    }

    @EventHandler
    public void onSkullClick(PlayerInteractEvent e) {

            Player player = e.getPlayer();

            if(!player.getInventory().getItemInMainHand().hasItemMeta()) return;

            if(!(player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()
                    .has(new NamespacedKey(mineShop, "uuid"), PersistentDataType.STRING))) return;

            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {e.setCancelled(true); return;}
            if(!e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;

            String playerUUIDStr = player.getInventory().getItemInMainHand()
                    .getItemMeta().getPersistentDataContainer().get(new NamespacedKey(mineShop, "uuid"), PersistentDataType.STRING);

            String playerName = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            playerName = playerName.replaceAll("Kill ", "");

            UUID playerUUID = UUID.fromString(playerUUIDStr);

            Player target = Bukkit.getPlayer(playerUUID);

            if(player == target) {
                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&4You can't kill yourself!"));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                return;
            }

            if(Bukkit.getOnlinePlayers().contains(target)) {

                player.getInventory().getItemInMainHand().add(-1);
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1,1);

                target.playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                target.setHealth(0);

                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&9&lKilled " + playerName));
                target.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&4&lYou have been killed by " + player.getName()));

            } else {
                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&4The target isn't connected to the server, maybe try another time"));
            }
    }
}
