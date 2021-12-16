package yad.mineshop.managers.subcommands;

import org.bukkit.entity.Player;
import yad.mineshop.MineShop;
import yad.mineshop.managers.SubCommand;
import yad.mineshop.utils.ChatUtils;

import java.util.UUID;

public class RegisterCommand extends SubCommand {

    private MineShop mineShop;
    public RegisterCommand(MineShop mineShop) {
        this.mineShop = mineShop;
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "Register to the mineshop software";
    }

    @Override
    public String getSyntax() {
        return "/mineshop register <password> <password>";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "reg", "rg" };
    }

    @Override
    public void perform(Player player, String[] args) {

        UUID uuid = player.getUniqueId();

        try {
            if (mineShop.readStringFromURL( mineShop.checkRegistered(uuid)).contains("false")) {
                if (args.length == 3) {
                    if (!(args[1]).equals(args[2])) {
                        player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cPasswords does not match"));
                        return;
                    }
                    if (args[1].length() <= 7) {
                        player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cPassword has to be 8 characters or more!"));
                        return;
                    }
                    mineShop.readStringFromURL( mineShop.register(uuid, args[1]));
                    player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aRegistered!"));
                } else { // args are more than 2 or less
                    player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cMissing arguments!\n/mineshop register <password> <password>"));
                }

            } else { // Player is already registered
                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aYou're already registered!"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
