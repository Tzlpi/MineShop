package yad.mineshop.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import yad.mineshop.MineShop;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.utils.ChatUtils;

public class CoalBreak implements Listener {

    private MineShop mineShop;
    private QuestManager questManager;

    public CoalBreak(MineShop mineShop, QuestManager questManager) {
        this.mineShop = mineShop;
        this.questManager = questManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if(e.getBlock().getType().equals(Material.COAL_ORE) || e.getBlock().getType().equals(Material.DEEPSLATE_COAL_ORE)) {

                questManager.addTempCoins(player, 0.05);
                player.sendMessage(ChatUtils.chat(ChatUtils.gotCoins(0.05)));

        }
    }
}
