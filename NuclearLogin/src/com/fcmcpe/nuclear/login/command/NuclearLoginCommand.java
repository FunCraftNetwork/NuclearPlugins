package com.fcmcpe.nuclear.login.command;

import cn.nukkit.command.Command;

/**
 * Created on 2015/12/10 by xtypr.
 * Package com.fcmcpe.nuclear.login.command in project NuclearLogin .
 */
public abstract class NuclearLoginCommand extends Command {
    public NuclearLoginCommand(String name) {
        super(name);
    }

    public NuclearLoginCommand(String name, String description) {
        super(name, description);
    }

    public NuclearLoginCommand(String name, String description, String usageMessage) {
        super(name, description, usageMessage);
    }

    public NuclearLoginCommand(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
    }
}
