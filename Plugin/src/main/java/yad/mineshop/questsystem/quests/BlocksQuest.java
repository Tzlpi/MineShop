package yad.mineshop.questsystem.quests;

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

public class BlocksQuest implements Listener {

    private MineShop mineShop;
    private QuestManager questManager;

    public BlocksQuest(MineShop mineShop, QuestManager questManager) {

        this.questManager = questManager;
        this.mineShop = mineShop;

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        Player player = e.getPlayer();

        if(questManager.hasTask(player)) {
            if(questManager.getTask(player).equalsIgnoreCase("blocks")) {

                questManager.addProgress(player, "blockcount", 1);

                if(questManager.getProgress(player, "blockcount") >= 10000) {

                    player.sendMessage(ChatUtils.chat(ChatUtils.gotCoins(500)));
                    player.sendMessage(ChatUtils.finishedQuest("blocks"));

                    questManager.setState(player, false);
                    questManager.removeTask(player, "blockcount");

                    try {
                        mineShop.readStringFromURL(mineShop.addCoins(player.getUniqueId(), 500));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    return;
                }

            }
        } else {
            return;
        }

    }

}
