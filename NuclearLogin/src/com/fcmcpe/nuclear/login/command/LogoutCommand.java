package com.fcmcpe.nuclear.login.command;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import com.fcmcpe.nuclear.login.NuclearLogin;
import com.fcmcpe.nuclear.login.NuclearLoginPlugin;
import com.fcmcpe.nuclear.core.language.TranslationSender;

/**
 * Created on 2015/12/11 by xtypr.
 * Package com.fcmcpe.nuclear.login.command in project NuclearLogin .
 */
public class LogoutCommand extends NuclearLoginCommand {

    private NuclearLoginPlugin plugin;

    // /logout
    public LogoutCommand(NuclearLoginPlugin plugin) {
        super("logout", "log out", "/logout");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("This command is only used in-game.");
            return false;
        }
        if (args.length > 0){
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.description.logout");
            return false;
        }
        //Because a player can only use command after login, there's no need to check if player already logged in.
        NuclearLogin.INSTANCE.setLogin((IPlayer) sender, false);
        TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.msg.logout-success");
        TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.msg.not-logged-in");

        return false;
    }
}
