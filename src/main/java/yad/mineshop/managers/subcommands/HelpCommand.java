package yad.mineshop.managers.subcommands;

import org.bukkit.entity.Player;
import yad.mineshop.managers.CommandManager;
import yad.mineshop.managers.SubCommand;
import yad.mineshop.utils.ChatUtils;

import java.util.Arrays;

public class HelpCommand extends SubCommand {

    private CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {

        this.commandManager = commandManager;

    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "The help command!";
    }

    @Override
    public String getSyntax() {
        return "/mineshop help";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "hp", "hel", "hl" };
    }

    @Override
    public void perform(Player player, String[] args) {

        player.sendMessage(ChatUtils.chat("&e-------------------------"));

        for(int i =0; i < commandManager.getSubCommands().size(); i++) {

                player.sendMessage(commandManager.getSubCommands().get(i).getSyntax() + " "
                        + Arrays.toString(commandManager.getSubCommands().get(i).getAliases())
                        + " - "  + commandManager.getSubCommands().get(i).getDescription());

        }

        player.sendMessage(ChatUtils.chat("&e-------------------------"));
    }
}
