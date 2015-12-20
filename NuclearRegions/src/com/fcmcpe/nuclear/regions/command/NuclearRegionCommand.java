package com.fcmcpe.nuclear.regions.command;

import cn.nukkit.command.Command;

/**
 * Created on 2015/12/20 by xtypr.
 * Package com.fcmcpe.nuclear.regions.command in project NuclearPlugins .
 */
public abstract class NuclearRegionCommand extends Command {
    public NuclearRegionCommand(String name) {
        super(name);
    }

    public NuclearRegionCommand(String name, String description) {
        super(name, description);
    }

    public NuclearRegionCommand(String name, String description, String usageMessage) {
        super(name, description, usageMessage);
    }

    public NuclearRegionCommand(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
    }
}
