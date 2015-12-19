package com.fcmcpe.nuclear.economy.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import com.fcmcpe.nuclear.core.language.TranslationSender;
import com.fcmcpe.nuclear.economy.NuclearEconomy;
import com.fcmcpe.nuclear.economy.NuclearEconomyPlugin;
import com.fcmcpe.nuclear.economy.data.MoneyData;

import java.util.Objects;

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
        if(args.length != 0) {
            TranslationSender.INSTANCE.sendMessage(player, "nucleareconomy.description.see");
            return false;
        }
        try {
            MoneyData data = NuclearEconomy.INSTANCE.getMoney(player.getName());
            Objects.requireNonNull(data);
            TranslationSender.INSTANCE.sendMessage(player, "nucleareconomy.msg.seemoney", String.valueOf(data.getMoney()));
        } catch (Exception e) {
            TranslationSender.INSTANCE.sendMessage(player, "nucleareconomy.error");
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
