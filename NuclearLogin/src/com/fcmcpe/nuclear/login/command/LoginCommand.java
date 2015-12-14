package com.fcmcpe.nuclear.login.command;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import com.fcmcpe.nuclear.login.NuclearLogin;
import com.fcmcpe.nuclear.login.data.PlayerCheckResult;
import com.fcmcpe.nuclear.login.provider.LoginDataImpl;
import com.fcmcpe.nuclear.login.provider.ProviderException;
import com.fcmcpe.nuclear.login.data.LoginData;
import com.fcmcpe.nuclear.login.security.PasswordUtil;
import com.fcmcpe.nuclear.login.NuclearLoginPlugin;
import com.fcmcpe.nuclear.login.data.PlayerLoginResult;
import com.fcmcpe.nuclear.login.language.TranslationSender;

/**
 * Created on 2015/12/10 by xtypr.
 * Package com.fcmcpe.nuclear.login.command in project NuclearLogin .
 */
public class LoginCommand extends NuclearLoginCommand {
    private NuclearLoginPlugin plugin;

    public LoginCommand(NuclearLoginPlugin plugin) {
        super("login", "Login", "/login <password>");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("This command is only used in-game.");
            return false;
        }

        if (!plugin.getConfig().getNestedAs("login.enabled", Boolean.TYPE)) {
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error.login-disabled");
            return false;
        }
        if (plugin.getConfig().getNestedAs("login.enable-failed-logins-kick", Boolean.TYPE)) {
            if (NuclearLogin.INSTANCE.getLoginAttempts((IPlayer) sender) >=
                    plugin.getConfig().getNestedAs("login.max-login-attempts", Integer.TYPE)) {
                TranslationSender.INSTANCE.sendKick((Player) sender, "nuclearlogin.kick.too-much-attempts");
            }
        }
        if (args.length == 0){
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.description.login");
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
                    Server.getInstance(),
                    (Player) sender,
                    PasswordUtil.INSTANCE.getHash(identifier, password)
            );
            PlayerCheckResult playerCheckResult = NuclearLogin.INSTANCE.getDataProvider().checkPlayer(data);
            if (!playerCheckResult.isExist()) {
                TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error.register-first");
                return false;
            }
            if (NuclearLogin.INSTANCE.isLoggedIn((IPlayer) sender)) {
                TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error.already-logged-in");
                return false;
            }
            PlayerLoginResult result = NuclearLogin.INSTANCE.getDataProvider().login(data);
            if (!result.isPasswordCorrect()) {
                //wrong password
                TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error.incorrect-password");
                NuclearLogin.INSTANCE.loginAttemptPlusOne((IPlayer) sender);
                return false;
            } else {
                NuclearLogin.INSTANCE.getDataProvider().login(new LoginDataImpl(
                        Server.getInstance(),
                        (Player) sender,
                        data.getHash()
                ));
                NuclearLogin.INSTANCE.setLogin((Player) sender, true);
                TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.msg.login-success");
                TranslationSender.INSTANCE.sendTip((Player) sender, "nuclearlogin.tip.login-success");
                return true;
            }
        } catch (ProviderException | NullPointerException e) {
            TranslationSender.INSTANCE.sendMessage((Player) sender, "nuclearlogin.error");
            e.printStackTrace();
            return false;
        }
    }
}
