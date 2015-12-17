package com.fcmcpe.nuclear.economy.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import com.fcmcpe.nuclear.core.language.TranslationSender;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.economy.NuclearEconomy;
import com.fcmcpe.nuclear.economy.NuclearEconomyPlugin;
import com.fcmcpe.nuclear.economy.data.MoneyPayResult;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.economy.command in project NuclearPlugins .
 */
public class MoneyPayCommand extends NuclearEconomyCommand {
    private NuclearEconomyPlugin plugin;

    public MoneyPayCommand(NuclearEconomyPlugin plugin) {
        super("$pay", "Pay money to other player", "/$pay <player> <money>", new String[]{"money pay"});
        this.plugin = plugin;
        this.setPermission("nucleareconomy.command.moneypay");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only used in-game.");
            return false;
        }
        Player player = (Player) sender;
        String targetName;
        long money;
        if (args.length != 2) {
            TranslationSender.INSTANCE.sendMessage(player, "nucleareconomy.description.pay");
            return false;
        }
        try {
            targetName = args[0];
            money = Long.parseLong(args[1]);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            TranslationSender.INSTANCE.sendMessage(player, "nucleareconomy.description.pay");
            return false;
        }
        try {
            MoneyPayResult result = NuclearEconomy.INSTANCE.getDataProvider().payMoney(player.getName(), targetName, money);
            if (!result.isFromEnough()) {
                TranslationSender.INSTANCE.sendMessage(player, "nucleareconomy.error.not-enough-money");
                return false;
            }
            if (!result.isToExist()) {
                TranslationSender.INSTANCE.sendMessage(player, "nucleareconomy.error.player-not-found", targetName);
                return false;
            }
            TranslationSender.INSTANCE.sendMessage(player, "nucleareconomy.msg.pay-succeed", targetName, String.valueOf(money));
            return true;
        } catch (ProviderException | NullPointerException e) {
            TranslationSender.INSTANCE.sendMessage(player, "nucleareconomy.error");
            e.printStackTrace();
            return false;
        }
    }
}
