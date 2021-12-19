package yad.mineshop.managers.subcommands;

import org.bukkit.entity.Player;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.managers.SubCommand;
import yad.mineshop.utils.ChatUtils;

import java.util.HashMap;
import java.util.Map;

public class StopQuestCommand extends SubCommand {

    private QuestManager questManager;

    public static Map<String, Long> cooldowns = new HashMap<String, Long>();

    public StopQuestCommand(QuestManager questManager) {

        this.questManager = questManager;

    }

    @Override
    public String getName() {
        return "stopquest";
    }

    @Override
    public String getDescription() {
        return "Stops the current quest";
    }

    @Override
    public String getSyntax() {
        return "/mineshop stopquest";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "stopq", "stoq" };
    }

    @Override
    public void perform(Player player, String[] args) {
        if(cooldowns.containsKey(player.getName()) && cooldowns.get(player.getName()) > System.currentTimeMillis()) {
            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cUse /mineshop confirm to stop the quest!"));
            return;
        }

        if(!questManager.getState(player)) {
            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cYou have nothing to stop!"));
            return;
        } else {
            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cAre you sure that you want to stop the quest?" +
                    " Any progress will be lost!" +
                    " Use /mineshop confirm in 5 seconds stop the quest!"));

            cooldowns.put(player.getName(),System.currentTimeMillis() + 5 * 1000);
        }
    }
}
