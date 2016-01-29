package com.fcmcpe.nuclear.regions.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.level.Location;
import com.fcmcpe.nuclear.regions.NuclearRegions;
import com.fcmcpe.nuclear.regions.NuclearRegionsPlugin;
import com.fcmcpe.nuclear.regions.data.RegionData;
import com.fcmcpe.nuclear.regions.permission.RegionPermission;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2015/12/21 by xtypr.
 * Package com.fcmcpe.nuclear.regions.listener in project NuclearPlugins .
 */
public class NuclearRegionListener implements Listener {
    NuclearRegionsPlugin plugin;
    private Map<Player, Long> lastCalcTime = new HashMap<>();
    private Map<Player, Location> lastValidLoc = new HashMap<>();
    public NuclearRegionListener(NuclearRegionsPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        lastCalcTime.putIfAbsent(player, System.currentTimeMillis());
        if ((lastCalcTime.get(player) + 200) < System.currentTimeMillis()) {
            RegionData inRegion = NuclearRegions.INSTANCE.getInRegion(player);
            if (inRegion != null) {
                if (!(RegionPermission.of(inRegion, player).hasPermission(RegionPermission.ENTER))) {
                    Location location = lastValidLoc.getOrDefault(player, event.getFrom());
                    if (location.getLevel() == player.getLevel()) player.teleport(location);
                    player.sendTip("CAN NOT MOVE INTO"); // TODO: 2016/1/29 LANGUAGE
                    event.setCancelled();
                }
            } else {
                if (player.onGround) lastValidLoc.put(player, event.getFrom());
            }
            lastCalcTime.put(player, System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        RegionData inRegion = NuclearRegions.INSTANCE.getInRegion(event.getBlock());
        if (inRegion != null) {
            if (!(RegionPermission.of(inRegion, player).hasPermission(RegionPermission.BLOCK_BREAK))) {
                player.sendTip("BLOCK CAN NOT BREAK");//test //language
                event.setCancelled();
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        RegionData inRegion = NuclearRegions.INSTANCE.getInRegion(event.getBlock());
        if (inRegion != null) {
            if (!(RegionPermission.of(inRegion, player).hasPermission(RegionPermission.BLOCK_PLACE))) {
                player.sendTip("BLOCK CAN NOT PLACE");//test //language
                event.setCancelled();
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Location location = NuclearRegions.INSTANCE.getNearestNoRegion(event.getPlayer());
        lastValidLoc.put(event.getPlayer(), location);
        event.setRespawnPosition(location);
    }
}
