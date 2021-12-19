package yad.mineshop.questsystem.quests;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import yad.mineshop.MineShop;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.utils.ChatUtils;

public class CowQuest implements Listener {

    private QuestManager questManager;
    private MineShop mineShop;

    public CowQuest(QuestManager questManager, MineShop mineShop) {

        this.questManager = questManager;
        this.mineShop = mineShop;

    }

    @EventHandler
    public void onCowKill(EntityDeathEvent e) {

        if(e.getEntity() instanceof Cow) {

            Cow cow = (Cow) e.getEntity();

            if(cow.getKiller() instanceof Player) {

                Player player = (Player) cow.getKiller();
                if(questManager.hasTask(player)) {
                    PersistentDataContainer data = player.getPersistentDataContainer();

                    int currentCows = data.get(new NamespacedKey(mineShop, "cowcount"), PersistentDataType.INTEGER);

                    questManager.addProgress(player, "cowcount", 1);

                    if (currentCows >= 100) {

                        player.sendMessage(ChatUtils.finishedQuest("cows"));
                        player.sendMessage(ChatUtils.chat(ChatUtils.gotCoins(25)));

                        questManager.removeTask(player, "cowcount");

                        try {
                            mineShop.readStringFromURL(mineShop.addCoins(player.getUniqueId(), 25));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                        questManager.setState(player, false);

                    }
                }
            }

        }

    }

}
