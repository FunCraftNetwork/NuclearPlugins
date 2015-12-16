package com.fcmcpe.nuclear.economy.command;

import cn.nukkit.command.CommandSender;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.economy.command in project NuclearPlugins .
 */
public class MoneyPayCommand extends NuclearEconomyCommand {
    public MoneyPayCommand() {
        super("$pay", "Pay money to other player", "/$pay <player> <money>", new String[]{"money pay"});
        this.setPermission("nucleareconomy.command.moneypay");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return false;
    }
}
