package yad.mineshop.managers.subcommands;

import org.bukkit.entity.Player;
import yad.mineshop.MineShop;
import yad.mineshop.managers.SubCommand;
import yad.mineshop.questsystem.gui.RewardGUI;
import yad.mineshop.utils.ChatUtils;

import java.util.UUID;

public class RewardCommand extends SubCommand {

    private RewardGUI gui;
    private MineShop mineShop;

    public RewardCommand(RewardGUI gui, MineShop mineShop) {
        this.gui = gui;
        this.mineShop = mineShop;
    }

    @Override
    public String getName() {
        return "claimrewards";
    }

    @Override
    public String getDescription() {
        return "Claim the rewards you bought from the mineshop store!";
    }

    @Override
    public String getSyntax() {
        return "/mineshop claimrewards";
    }

    @Override
    public String[] getAliases() {
        return new String[]{ "cr", "rewards" };
    }

    @Override
    public void perform(Player player, String[] args) {
        UUID uuid = player.getUniqueId();
        player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&aLoading your rewards..."));
        try {
            if(mineShop.readStringFromURL(mineShop.getReward(uuid)).contains("false")) {
                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&cYou don't have any reward!\nBuy rewards with earned coins at the mineshop software!"));
            }else {
                gui.createGUI(player);
                player.sendMessage(ChatUtils.chat(ChatUtils.prefix() + "&2&lRewards Loaded!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
