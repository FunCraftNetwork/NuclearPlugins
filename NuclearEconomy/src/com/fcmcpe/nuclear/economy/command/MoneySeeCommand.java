package com.fcmcpe.nuclear.economy.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import com.fcmcpe.nuclear.core.language.TranslationSender;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.economy.NuclearEconomy;
import com.fcmcpe.nuclear.economy.NuclearEconomyPlugin;
import com.fcmcpe.nuclear.economy.data.MoneyData;
import com.fcmcpe.nuclear.economy.provider.MoneyDataImpl;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.economy.command in project NuclearPlugins .
 */
public class MoneySeeCommand extends NuclearEconomyCommand {

    private NuclearEconomyPlugin plugin;

    public MoneySeeCommand(NuclearEconomyPlugin plugin) {
        super("$see", "See your money", "/$see", new String[]{"money see"});
        this.plugin = plugin;
        this.setPermission("nucleareconomy.command.moneysee");
    }
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only used in-game.");
            return false;
        }
        Player player = (Player) sender;
        try {
            MoneyData data = NuclearEconomy.INSTANCE.getDataProvider().getMoney(new MoneyDataImpl(plugin.getServer(), player.getName()));
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nucleareconomy.msg.seemoney", String.valueOf(data.getMoney()));
        } catch (ProviderException | NullPointerException e) {
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nucleareconomy.error");
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
