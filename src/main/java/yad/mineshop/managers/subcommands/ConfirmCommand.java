package yad.mineshop.managers.subcommands;

import org.bukkit.entity.Player;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.managers.SubCommand;
import yad.mineshop.utils.ChatUtils;

public class ConfirmCommand extends SubCommand {

    private QuestManager questManager;

    public ConfirmCommand(QuestManager questManager) {

        this.questManager = questManager;

    }

    @Override
    public String getName() {
        return "confirm";
    }

    @Override
    public String getDescription() {
        return "confirms stopping the quest";
    }

    @Override
    public String getSyntax() {
        return "/mineshop confirm";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "con", "cf" };
    }

    @Override
    public void perform(Player player, String[] args) {

        if(StopQuestCommand.cooldowns.get(player.getName()) == null) {
            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cYou have nothing to stop!"));
        } else if(StopQuestCommand.cooldowns.isEmpty()) {
            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cYou have nothing to stop!"));
        } else if(StopQuestCommand.cooldowns.get(player.getName()) > System.currentTimeMillis()) {
            questManager.setState(player, false);
            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aStopped the quest!"));
        } else {
            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cYou have nothing to stop!"));
        }
    }
}
