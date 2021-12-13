package yad.mineshop.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import yad.mineshop.utils.ChatUtils;

import java.util.Objects;

public class InteractEvent implements Listener {

    @EventHandler
    public void onSkullClick(PlayerInteractEvent e) {

            Player player = e.getPlayer();

            if(player.getInventory().getItemInMainHand().equals(new ItemStack(Material.AIR))) return;
            if(!(player.getInventory().getItemInMainHand().hasItemMeta())) return;
            if(Objects.requireNonNull(player.getInventory().getItemInMainHand().lore()).isEmpty()) return;
            if(!player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Kill")) return;

            String playerName = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            playerName = playerName.replaceAll("Kill ", "");

            Player target = Bukkit.getPlayer(playerName);
            if(Bukkit.getOnlinePlayers().contains(target)) {

                player.getInventory().getItemInMainHand().setAmount(0);
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1,1);
                target.setHealth(0);

                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&9&lKilled " + playerName));
                target.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&4&lYou have been killed by " + player.getName()));

            } else {
                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&4The target isn't connected to the server, maybe try another time"));
            }
    }
}
