package com.fcmcpe.nuclear.login.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import com.fcmcpe.nuclear.login.NuclearLogin;
import com.fcmcpe.nuclear.login.NuclearLoginPlugin;
import com.fcmcpe.nuclear.login.data.LoginData;
import com.fcmcpe.nuclear.login.data.PlayerUnregisterResult;
import com.fcmcpe.nuclear.core.language.TranslationSender;
import com.fcmcpe.nuclear.login.provider.LoginDataImpl;
import com.fcmcpe.nuclear.login.provider.ProviderException;
import com.fcmcpe.nuclear.login.security.PasswordUtil;

/**
 * Created on 2015/12/11 by xtypr.
 * Package com.fcmcpe.nuclear.login.command in project NuclearLogin .
 */
public class UnregisterCommand extends NuclearLoginCommand {
    private NuclearLoginPlugin plugin;

    public UnregisterCommand(NuclearLoginPlugin plugin) {
        super("unregister", "Remove the account", "/unregister <password>");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("This command is only used in-game.");
            return false;
        }
        if (args.length == 0){
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.description.unregister");
            return false;
        }
        String password = "";
        for (String arg : args) {
            password += arg + " ";
        }
        password = password.trim();

        try {
            String identifier = sender.getName().trim().toLowerCase();
            LoginData data = new LoginDataImpl(
                    plugin.getServer(),
                    (Player) sender,
                    PasswordUtil.INSTANCE.getHash(identifier, password)
            );
            PlayerUnregisterResult result = NuclearLogin.INSTANCE.getDataProvider().unregisterIfPresent(data);
            if (!result.isPasswordCorrect()) {
                //wrong password
                TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error.incorrect-password");
                return false;
            } else {
                NuclearLogin.INSTANCE.setLogin((Player) sender, false);
                TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.msg.unregister-success");
                TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.msg.not-registered");
                return true;
            }
        } catch (ProviderException | NullPointerException e) {
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error");
            e.printStackTrace();
            return false;
        }
    }
}
