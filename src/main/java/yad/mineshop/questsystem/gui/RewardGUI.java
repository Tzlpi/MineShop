package yad.mineshop.questsystem.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import yad.mineshop.MineShop;
import yad.mineshop.utils.ChatUtils;
import yad.mineshop.utils.ItemBuilder;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class RewardGUI {

    private final MineShop mineShop;
    public RewardGUI(MineShop mineShop) {
        this.mineShop = mineShop;
    }

    Inventory inv;
    ItemStack itemReward = new ItemStack(Material.EMERALD);

    public void createGUI(Player player) {

        inv = Bukkit.createInventory(player, 27, Component.text(ChatUtils.chat("&a&lRewards")));

        UUID uuid = player.getUniqueId();

        for(String kills : mineShop.readKills(uuid)) {

                String playerName = mineShop.getNameFromUniqueId(kills);

                ItemStack skull = new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(playerName).setName("Kill " + playerName)
                        .setLore(ChatUtils.chat("&4&lRight Click me to kill the player!")).hideItemAttribute().setPersistentDataContainer("uuid", kills)
                        .toItemStack();

                PersistentDataContainer data = skull.getItemMeta().getPersistentDataContainer();

                 data.set(new NamespacedKey(mineShop, "uuid"), PersistentDataType.STRING, "" + kills);

                inv.addItem(skull);
        }

        for(Long rewards : mineShop.getItems(uuid)) {

            String itemName = mineShop.idToName(rewards.toString());
            itemReward.setType(Material.getMaterial(itemName.toUpperCase()));

            inv.addItem(itemReward);
        }


        ItemStack claim = new ItemStack(Material.EMERALD);
        ItemMeta meta = claim.getItemMeta();

        meta.displayName(Component.text(ChatUtils.chat("&2&lClaim Rewards!")));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        claim.setItemMeta(meta);

        inv.setItem(22, claim);

        player.openInventory(inv);
    }


}
