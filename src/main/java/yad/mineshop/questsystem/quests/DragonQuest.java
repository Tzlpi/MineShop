package yad.mineshop.questsystem.quests;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.utils.ChatUtils;

public class DragonQuest implements Listener {

    private QuestManager questManager;

    public DragonQuest(QuestManager questManager) {

        this.questManager = questManager;

    }

    @EventHandler
    public void onDragonKill(EntityDeathEvent e) {

        if(e.getEntity() instanceof EnderDragon) {

            for(Player player : Bukkit.getOnlinePlayers()) {

                if(player.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
                    if(questManager.getTask(player).equals("dragon")) {

                        player.sendMessage(ChatUtils.finishedQuest("dragon"));
                        player.sendMessage(ChatUtils.chat(ChatUtils.gotCoins(100)));

                        questManager.addTempCoins(player, 100);
                        questManager.setState(player, false);

                    }


                }

            }

        } else return;
    }

}
