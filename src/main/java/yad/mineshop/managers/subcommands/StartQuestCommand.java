package yad.mineshop.managers.subcommands;

import org.bukkit.entity.Player;
import yad.mineshop.MineShop;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.managers.SubCommand;
import yad.mineshop.questsystem.gui.QuestGUI;
import yad.mineshop.utils.ChatUtils;

public class StartQuestCommand extends SubCommand {

    private QuestManager questManager;
    private MineShop mineShop;

    private QuestGUI questGui;

    public StartQuestCommand(QuestManager questManager, MineShop mineShop) {

        this.questManager = questManager;
        this.mineShop = mineShop;

        this.questGui = new QuestGUI(questManager, mineShop);

    }

    @Override
    public String getName() {
        return "startquest";
    }

    @Override
    public String getDescription() {
        return "Opens a QuestGUI that you can select quests from";
    }

    @Override
    public String getSyntax() {
        return "/mineshop startquest";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"stq", "startq"};
    }

    @Override
    public void perform(Player player, String[] args) {
        if(questManager.getState(player)) {
            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cPlease finish the ongoing quest!"));
            return;
        } else {
            questGui.openInventory(player);
            return;
        }
    }
}
