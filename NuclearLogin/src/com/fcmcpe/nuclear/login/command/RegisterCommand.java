package com.fcmcpe.nuclear.login.command;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import com.fcmcpe.nuclear.login.NuclearLogin;
import com.fcmcpe.nuclear.login.NuclearLoginPlugin;
import com.fcmcpe.nuclear.login.data.LoginData;
import com.fcmcpe.nuclear.login.data.PlayerCheckResult;
import com.fcmcpe.nuclear.core.language.TranslationSender;
import com.fcmcpe.nuclear.login.provider.LoginDataImpl;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.login.security.PasswordUtil;

/**
 * Created on 2015/12/10 by xtypr.
 * Package com.fcmcpe.nuclear.login.command in project NuclearLogin .
 */
public class RegisterCommand extends NuclearLoginCommand {
    private NuclearLoginPlugin plugin;

    // /register password
    public RegisterCommand(NuclearLoginPlugin plugin) {
        super("register", "register your account", "/register <password>");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("This command is only used in-game.");
            return false;
        }
        if (!plugin.getConfig().getNestedAs("register.enabled", Boolean.TYPE)) {
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error.register-disabled");
            return false;
        }
        if (args.length == 0){
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.description.register");
            return false;
        }
        String password = "";
        for (String arg : args) {
            password += arg + " ";
        }
        password = password.trim();

        String identifier = sender.getName().trim().toLowerCase();
        try {
            LoginData data = new LoginDataImpl(
                    plugin.getServer(),
                    (Player) sender,
                    PasswordUtil.INSTANCE.getHash(identifier, password)
            );
            PlayerCheckResult playerCheckResult = NuclearLogin.INSTANCE.getDataProvider().checkPlayer(data);
            if (plugin.getConfig().getNestedAs("register.enable-max-ip", Boolean.TYPE)) {
                int maxAccount = plugin.getConfig().getNestedAs("register.max-ip", Integer.TYPE);
                if (playerCheckResult.getIPAccountCount() > maxAccount || playerCheckResult.getUUIDAccountCount() > maxAccount) {
                    TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error.too-much-account");
                    return false;
                }
            }
            if (playerCheckResult.isExist()) {
                TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error.already-registered");
                return false;
            }

            if (password.length() < plugin.getConfig().getNestedAs("minPasswordLength", Integer.TYPE)) {
                TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error.too-short-password");
                return false;
            }
            if (password.length() > plugin.getConfig().getNestedAs("maxPasswordLength", Integer.TYPE)) {
                TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error.too-long-password");
                return false;
            }

            NuclearLogin.INSTANCE.getDataProvider().registerIfAbsent(data);
            NuclearLogin.INSTANCE.setLogin((IPlayer) sender, true);
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.msg.register-success");
            TranslationSender.INSTANCE.sendTip((Player) sender, "nuclearlogin.tip.login-success");
            return true;
        } catch (ProviderException | NullPointerException e) {
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error");
            e.printStackTrace();
            return false;
        }
    }
}
