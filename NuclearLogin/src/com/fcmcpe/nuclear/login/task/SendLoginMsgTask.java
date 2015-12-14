package com.fcmcpe.nuclear.login.task;

import cn.nukkit.scheduler.*;
import com.fcmcpe.nuclear.login.NuclearLogin;
import com.fcmcpe.nuclear.login.provider.LoginDataImpl;
import com.fcmcpe.nuclear.login.NuclearLogin;
import com.fcmcpe.nuclear.login.NuclearLoginPlugin;
import com.fcmcpe.nuclear.login.data.PlayerCheckResult;
import com.fcmcpe.nuclear.login.provider.ProviderException;
import com.fcmcpe.nuclear.login.language.TranslationSender;
import com.fcmcpe.nuclear.login.provider.LoginDataImpl;

/**
 * Created on 2015/12/10 by xtypr.
 * Package com.fcmcpe.nuclear.login.task in project NuclearLogin .
 */
public class SendLoginMsgTask extends PluginTask<NuclearLoginPlugin> {

    public SendLoginMsgTask(NuclearLoginPlugin owner) {
        super(owner);
    }

    @Override
    public void onRun(int currentTick) {
        //for(xxx : xxx)
        getOwner().getServer().getOnlinePlayers().forEach((s, p) -> {
            if (p != null) {
                if (p.isOnline() && !NuclearLogin.INSTANCE.isLoggedIn(p)) {
                    PlayerCheckResult result = null;
                    try {
                        result = NuclearLogin.INSTANCE.getDataProvider().checkPlayer(new LoginDataImpl(
                                getOwner().getServer(),
                                p
                        ));
                        if (result.isExist()) {
                            if (!owner.getConfig().getNestedAs("login.enabled", Boolean.TYPE)) {
                                TranslationSender.INSTANCE.sendTip(p, "nuclearlogin.tip.login-disabled");
                                return;
                            }
                            TranslationSender.INSTANCE.sendTip(p, "nuclearlogin.tip.login");
                        } else {
                            if (!owner.getConfig().getNestedAs("register.enabled", Boolean.TYPE)) {
                                TranslationSender.INSTANCE.sendTip(p, "nuclearlogin.tip.register-disabled");
                                return;
                            }
                            TranslationSender.INSTANCE.sendTip(p, "nuclearlogin.tip.register");
                        }
                    } catch (ProviderException | NullPointerException e) {
                        e.printStackTrace();
                        TranslationSender.INSTANCE.sendMessage(p, "nuclearlogin.error");
                    }
                }
            }
        });
    }
}