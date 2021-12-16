package yad.mineshop.managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yad.mineshop.MineShop;
import yad.mineshop.managers.subcommands.*;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.questsystem.gui.ProgressGUI;
import yad.mineshop.questsystem.gui.RewardGUI;
import yad.mineshop.utils.ChatUtils;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private MineShop mineShop;
    private QuestManager questManager;

    private ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(MineShop mineShop) {

        this.mineShop = mineShop;
        this.questManager = new QuestManager(mineShop);

        subCommands.add(new RegisterCommand(mineShop));
        subCommands.add(new BalanceCommand(mineShop));
        subCommands.add(new StartQuestCommand(questManager, mineShop));
        subCommands.add(new StopQuestCommand(questManager));
        subCommands.add(new ConfirmCommand(questManager));
        subCommands.add(new HelpCommand(this));
        subCommands.add(new ProgressCommand(new ProgressGUI(questManager), questManager));
        subCommands.add(new RewardCommand(new RewardGUI(mineShop), mineShop));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("mineshop")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You can't use that");
                return false;
            }
        }

        Player player = (Player) sender;

        if(args.length > 0) {
            for(int i = 0; i < getSubCommands().size(); i++) {

                for(String aliases : getSubCommands().get(i).getAliases()) {

                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName()) || args[0].equalsIgnoreCase(aliases)) {

                        getSubCommands().get(i).perform(player, args);
                        break;

                    }
                }

            }

        }else if(args.length == 0) {

            player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cYou missed some arguments! Please use &e/mineshop help &cfor help"));

            return false;

        }
        return false;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }

}
