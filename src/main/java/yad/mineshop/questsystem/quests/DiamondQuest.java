package yad.mineshop.questsystem.quests;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import yad.mineshop.MineShop;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.utils.ChatUtils;

public class DiamondQuest implements Listener {

    private MineShop mineShop;
    private QuestManager questManager;

    public DiamondQuest(MineShop mineShop, QuestManager questManager) {
        this.mineShop = mineShop;
        this.questManager = questManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        Player player = e.getPlayer();

        if(e.getBlock().getType().equals(Material.DIAMOND_ORE)) {

            PersistentDataContainer data = player.getPersistentDataContainer();

            if(!questManager.hasTask(player)) return;
            if(questManager.getTask(player).equalsIgnoreCase("diamonds")) {

                int currentDiamond = data.get(new NamespacedKey(mineShop, "diamondcount"), PersistentDataType.INTEGER);

                data.set(new NamespacedKey(mineShop, "diamondcount"), PersistentDataType.INTEGER, currentDiamond + 1);


                if (currentDiamond == 100) {

                    try {
                        mineShop.readStringFromURL(mineShop.addCoins(player.getUniqueId(), 100));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    data.remove(new NamespacedKey(mineShop, "diamondcount"));

                    player.sendMessage(ChatUtils.finishedQuest("diamonds"));
                    player.sendMessage(ChatUtils.chat(ChatUtils.gotCoins(100)));

                    questManager.setState(player, false);
                }
            }
        }

    }

}
