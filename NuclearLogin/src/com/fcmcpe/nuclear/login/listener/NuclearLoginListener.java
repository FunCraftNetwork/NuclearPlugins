package com.fcmcpe.nuclear.login.listener;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.*;
import com.fcmcpe.nuclear.login.NuclearLogin;
import com.fcmcpe.nuclear.login.NuclearLoginPlugin;
import com.fcmcpe.nuclear.login.data.PlayerCheckResult;
import com.fcmcpe.nuclear.core.language.TranslationSender;
import com.fcmcpe.nuclear.login.provider.LoginDataImpl;
import com.fcmcpe.nuclear.login.provider.ProviderException;

import java.util.Objects;

/**
 * Created on 2015/12/9 by xtypr.
 * Package com.fcmcpe.nuclear.login.listener in project NuclearLogin .
 */
public class NuclearLoginListener implements Listener{

    NuclearLoginPlugin plugin;

    public NuclearLoginListener(NuclearLoginPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(PlayerPreLoginEvent event){
        if(!plugin.getConfig().getNestedAs("force-single-auth", Boolean.TYPE)) {
            return;
        }
        Player player = event.getPlayer();
        plugin.getServer().getOnlinePlayers().forEach((s, p) -> {
            if (p != null) {
                if (p != player && Objects.equals(p.getName().toLowerCase().trim(), player.getName().toLowerCase().trim())) {
                    if (NuclearLogin.INSTANCE.isLoggedIn(p)) {
                        event.setCancelled(true);
                        TranslationSender.INSTANCE.sendKick(player, "nuclearlogin.kick.already-logged-in");
                    }
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        boolean isRegistered = false;
        try {
            PlayerCheckResult data = NuclearLogin.INSTANCE.getDataProvider().checkPlayer(new LoginDataImpl(Server.getInstance(), event.getPlayer()));
            isRegistered = data.isExist();
            if(plugin.getConfig().getNestedAs("last-ip-or-uuid", Boolean.TYPE)) {
                if (data.isIPMatch() && data.isUUIDMatch()) {
                    NuclearLogin.INSTANCE.setLogin(event.getPlayer(), true);
                    TranslationSender.INSTANCE.sendMessage(player, "nuclearlogin.msg.login-same-ip");
                    TranslationSender.INSTANCE.sendTip(player, "nuclearlogin.tip.login-success");
                    return;
                }
            }
        } catch (ProviderException e) {
            e.printStackTrace();
            TranslationSender.INSTANCE.sendMessage(player, "nuclearlogin.error");
        }
        if (!NuclearLogin.INSTANCE.isLoggedIn(player)) {
            NuclearLogin.INSTANCE.setLogin(event.getPlayer(), false);
            if (isRegistered) {
                if (!plugin.getConfig().getNestedAs("login.enabled", Boolean.TYPE)) {
                    TranslationSender.INSTANCE.sendMessage(player, "nuclearlogin.error.login-disabled");
                    return;
                }
                TranslationSender.INSTANCE.sendMessage(player, "nuclearlogin.msg.not-logged-in");
            } else {
                if (!plugin.getConfig().getNestedAs("register.enabled", Boolean.TYPE)) {
                    TranslationSender.INSTANCE.sendMessage(player, "nuclearlogin.error.register-disabled");
                    return;
                }
                TranslationSender.INSTANCE.sendMessage(player, "nuclearlogin.msg.not-registered");
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        NuclearLogin.INSTANCE.setLogin(event.getPlayer(), false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!NuclearLogin.INSTANCE.isLoggedIn(event.getPlayer())) {
            String message = event.getMessage();
            if (message.toLowerCase().startsWith("/login") ||
                    message.toLowerCase().startsWith("/register")) {
                String command = message.substring(1);
                this.plugin.getServer().dispatchCommand(event.getPlayer(), command);
            } else if (!message.startsWith("/")) {
                String command = "login "+message;
                this.plugin.getServer().dispatchCommand(event.getPlayer(), command);
            } else {
                TranslationSender.INSTANCE.sendMessage(event.getPlayer(), "nuclearlogin.error.command-login-first");
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!NuclearLogin.INSTANCE.isLoggedIn(event.getPlayer())) {
            //permission
            event.setCancelled(true);
            event.getPlayer().onGround = true;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!NuclearLogin.INSTANCE.isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!NuclearLogin.INSTANCE.isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (!NuclearLogin.INSTANCE.isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (!NuclearLogin.INSTANCE.isLoggedIn((IPlayer) event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer() != null) {
            if (!NuclearLogin.INSTANCE.isLoggedIn(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer() != null) {
            if (!NuclearLogin.INSTANCE.isLoggedIn(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    /*
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof PlayerInventory) {
            if (!NuclearLogin.INSTANCE.isLoggedIn(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }
    */

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickupItem(InventoryPickupItemEvent event) {
        if (event.getInventory().getHolder() instanceof Player) {
            if (!NuclearLogin.INSTANCE.isLoggedIn((IPlayer) event.getInventory().getHolder())) {
                event.setCancelled(true);
            }
        }
    }

}
