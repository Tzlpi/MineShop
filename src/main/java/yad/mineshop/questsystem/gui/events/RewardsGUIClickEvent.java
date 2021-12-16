package yad.mineshop.questsystem.gui.events;

import com.destroystokyo.paper.MaterialTags;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.A;
import yad.mineshop.MineShop;
import yad.mineshop.questsystem.gui.RewardGUI;
import yad.mineshop.utils.ChatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class RewardsGUIClickEvent implements Listener {

    public static HashMap<Player, Integer> confirmed = new HashMap<>();

    private MineShop mineShop;
    private RewardGUI rewardGUI;

    public RewardsGUIClickEvent(MineShop mineShop, RewardGUI rewardGUI) {
        this.mineShop = mineShop;
        this.rewardGUI = rewardGUI;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if(!e.getView().title().equals(Component.text(ChatUtils.chat("&a&lRewards")))) return;

        if(e.getCurrentItem() == null) return;

        if(!(e.getCurrentItem().getType().equals(Material.EMERALD))) {

            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cYou can't take that!"));

            e.setCancelled(true);

        } else if (!e.getInventory().contains(e.getCurrentItem())) {

            e.setCancelled(true);

        } else {
            int playerItems = 0;
            int invItems = 0;

            for(ItemStack item : player.getInventory().getContents()) {
                if(item != null)
                    playerItems++;
            }
            for(ItemStack item : e.getInventory().getContents()) {
                if(item != null)
                    invItems++;
            }

            int added = playerItems + invItems;

            if(added > 36) {

                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&4You don't have space in your inventory!"));

                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                e.setCancelled(true);

            }
            if(added < 36 && confirmed.get(player) == 0 ) {

                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aPlease click the emerald again to confirm claim"));
                confirmed.put(player, 1);

                e.setCancelled(true);
                return;
            }
            if(confirmed.get(player) == 1) {

                ArrayList<ItemStack> rewards = new ArrayList<>(Arrays.asList(e.getInventory().getContents()));

                ItemStack[] rewardsArr = new ItemStack[rewards.size()];

                rewards.toArray(rewardsArr);

                for(ItemStack items : rewardsArr) {
                    if(items == null) continue;
                    if(items.getType().equals(Material.EMERALD)) continue;

                    player.getInventory().addItem(items);
                }

                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aRewards claimed!"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 10);

                e.setCancelled(true);

                confirmed.put(player, 0);
                player.closeInventory();

                try {
                    mineShop.readStringFromURL(mineShop.finishReward(player.getUniqueId()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }


        }
    }
    @EventHandler
    public void onGUIClose(InventoryCloseEvent e) {
        if(!e.getView().title().equals(Component.text(ChatUtils.chat("&a&lRewards")))) return;

        e.getInventory().clear();
    }


}
