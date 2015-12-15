package com.fcmcpe.nuclear.economy.command;

import cn.nukkit.command.Command;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.economy.command in project NuclearPlugins .
 */
public abstract class NuclearEconomyCommand extends Command {
    public NuclearEconomyCommand(String name) {
        super(name);
    }

    public NuclearEconomyCommand(String name, String description) {
        super(name, description);
    }

    public NuclearEconomyCommand(String name, String description, String usageMessage) {
        super(name, description, usageMessage);
    }

    public NuclearEconomyCommand(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
    }
}
