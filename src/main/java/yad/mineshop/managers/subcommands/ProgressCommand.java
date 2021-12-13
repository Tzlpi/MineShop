package yad.mineshop.managers.subcommands;

import org.bukkit.entity.Player;
import yad.mineshop.managers.SubCommand;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.questsystem.gui.ProgressGUI;
import yad.mineshop.utils.ChatUtils;

public class ProgressCommand extends SubCommand {

    private ProgressGUI progressGUI;
    private QuestManager questManager;

    public ProgressCommand(ProgressGUI progressGUI, QuestManager questManager) {
        this.progressGUI = progressGUI;
        this.questManager = questManager;
    }

    @Override
    public String getName() {
        return "progress";
    }

    @Override
    public String getDescription() {
        return "Gets the progress of the current task";
    }

    @Override
    public String getSyntax() {
        return "/mineshop progress";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "prog" };
    }

    @Override
    public void perform(Player player, String[] args) {

        if(!questManager.hasTask(player)) {
            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cYou're not in a task!"));
            return;
        } else {
            progressGUI.openInv(player);
        }

    }
}
