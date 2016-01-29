package com.fcmcpe.nuclear.rgcommands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import com.fcmcpe.nuclear.core.language.NuclearDictionary;
import com.fcmcpe.nuclear.core.language.TranslationSender;
import com.fcmcpe.nuclear.regions.NuclearRegions;
import com.fcmcpe.nuclear.regions.data.RegionAddResult;
import com.fcmcpe.nuclear.regions.data.RegionData;
import com.fcmcpe.nuclear.regions.math.ZonedRegionBox;
import com.fcmcpe.nuclear.regions.permission.RegionPermission;

import java.util.Locale;
import java.util.Objects;

/**
 * Created by Snake1999 on 2016/1/29.
 * Package com.fcmcpe.nuclear.rgcommands in project NuclearPlugins.
 */
public class RGCommand extends Command {
    public RGCommand(String name) {
        super(name, "", "");
    }

    /*
        /rg pos1
        /rg pos2
        /rg create
        /rg remove [id region]
        /rg flag [id region] [flag] [allow/deny]
        /rg info - info region where you stand
        /rg info [id region]
        /rg help
    */
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Request request = Request.of(args);
        Player player;
        if (!(sender instanceof Player)) player = null;
        else player = (Player) sender;
        if (request.type == RequestType.POS1) {
            if (player == null) {
                sender.sendMessage("This command is only used in-game.");
                return false;
            }
            RGCommands.INSTANCE.setPos1(player, player.getPosition());
            Position pos1 = RGCommands.INSTANCE.getPos1(player);
            TranslationSender.INSTANCE.sendMessage((Player) sender, "rgcommands.msg.pos1",
                    pos1.getLevel().getName(), pos1.getFloorX() + "", pos1.getFloorY() + "", pos1.getFloorZ() + "");
            return true;
        } else if (request.type == RequestType.POS2) {
            if (player == null) {
                sender.sendMessage("This command is only used in-game.");
                return false;
            }
            RGCommands.INSTANCE.setPos2(player, player.getPosition());
            Position pos2 = RGCommands.INSTANCE.getPos2(player);
            TranslationSender.INSTANCE.sendMessage((Player) sender, "rgcommands.msg.pos2",
                    pos2.getLevel().getName(), pos2.getFloorX() + "", pos2.getFloorY() + "", pos2.getFloorZ() + "");
            return true;
        } else if (request.type == RequestType.INFO) {
            RegionData data;
            if (request.regionID == -1) {
                if (player == null) {
                    sender.sendMessage("This command is only used in-game.");
                    return false;
                } else data = NuclearRegions.INSTANCE.getInRegion(player.getPosition());
            } else data = NuclearRegions.INSTANCE.getByID(request.regionID);
            if (data == null && request.regionID != -1) {
                if (!(sender instanceof Player))
                    sender.sendMessage(NuclearDictionary.get(Locale.ENGLISH, "rgcommands.msg.no-region-id", "" + request.regionID));
                else
                    TranslationSender.INSTANCE.sendMessage((Player) sender, "rgcommands.msg.no-region-id", request.regionID + "");
            } else if (data == null) {
                if (!(sender instanceof Player))
                    sender.sendMessage(NuclearDictionary.get(Locale.ENGLISH, "rgcommands.msg.no-region-here"));
                else TranslationSender.INSTANCE.sendMessage((Player) sender, "rgcommands.msg.no-region-here");
            } else {
                String ownerName = data.getOwnerName();
                if (ownerName == null) ownerName = "(No owner)";
                String[] params = new String[]{
                        String.valueOf(data.getID()),
                        ownerName,
                        String.valueOf(data.getBox().getSize())
                };
                if (!(sender instanceof Player))
                    sender.sendMessage(NuclearDictionary.get(Locale.ENGLISH, "rgcommands.msg.rg-info", params));
                else TranslationSender.INSTANCE.sendMessage((Player) sender, "rgcommands.msg.rg-info", params);
            }
        } else if (request.type == RequestType.CREATE) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command is only used in-game.");
                return false;
            }
            Position pos1 = RGCommands.INSTANCE.getPos1(player);
            Position pos2 = RGCommands.INSTANCE.getPos2(player);
            if (pos1 == null || pos2 == null) {
                TranslationSender.INSTANCE.sendMessage(player, "rgcommands.error.pos-not-set");
                return true;
            }
            if (!Objects.equals(pos1.getLevel().getName(), pos2.getLevel().getName())) {
                TranslationSender.INSTANCE.sendMessage(player, "rgcommands.error.not-same-level");
                return true;
            }
            ZonedRegionBox box = ZonedRegionBox.of(pos1, pos2);
            RegionAddResult result;
            try {
                result = NuclearRegions.INSTANCE.getDataProvider().addRegion($LimitedRegionData$.of(box));
                if (result == null) throw new Exception();
                NuclearRegions.INSTANCE.getDataProvider().updatePerm(result.getData().getID(), player.getName(), RegionPermission.OWNER);
            } catch (Exception e) {
                TranslationSender.INSTANCE.sendMessage(player, "rgcommands.error");
                e.printStackTrace();
                return true;
            }
            if (result.isConflict()) {
                TranslationSender.INSTANCE.sendMessage(player, "rgcommands.error.conflict");
                return true;
            }
            RegionData data = result.getData();
            TranslationSender.INSTANCE.sendMessage(player, "rgcommands.msg.rg-create",
                    data.getID() + "", data.getBox().getSize() + "");
            NuclearRegions.INSTANCE.reloadRegionData();
            return true;
        } else {
            // TODO: 2016/1/29 MESSAGE
            sender.sendMessage("help");
        }
        return true;
    }

    enum RequestType {
        POS1, POS2, CREATE, REMOVE, INFO, INVALID
    }

    static class Request {
        RequestType type = RequestType.INVALID;
        int regionID = -1;
        RegionPermission perm;
        boolean allowOrDeny;

        static Request of(String[] args) {
            Request request = new Request();
            if (args.length == 0) {
                request.type = RequestType.INVALID;
                return request;
            }
            switch (args[0]) {
                case "pos1":
                    request.type = RequestType.POS1;
                    return request;
                case "pos2":
                    request.type = RequestType.POS2;
                    return request;
                case "create":
                    request.type = RequestType.CREATE;
                    return request;
                case "remove":
                    try {
                        request.type = RequestType.REMOVE;
                        request.regionID = Integer.parseInt(args[1]);
                    } catch (Exception e) {
                        return request;
                    }
                    return request;
                case "info":
                    if (args.length == 2) {
                        try {
                            request.type = RequestType.INFO;
                            request.regionID = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                            return request;
                        }
                        return request;
                    } else if (args.length == 1) {
                        request.type = RequestType.INFO;
                        return request;
                    }
                    return request;
                case "flag":
                    if (args.length != 4) return request;
                    try {
                        request.regionID = Integer.parseInt(args[1]);
                        request.perm = getPermissionFrom(args[2]);
                        request.allowOrDeny = getAllowOrDeny(args[3]);
                    } catch (Exception e) {
                        return request;
                    }
                default:
                    request.type = RequestType.INVALID;
                    return request;
            }
        }

        static RegionPermission getPermissionFrom(String name) {
            int perm = RegionPermission.NONE;
            if (name == null) return RegionPermission.of(perm);
            switch (name) {
                case "enter":
                    perm = RegionPermission.ENTER;
                    break;
                // TODO: 2016/1/29 FINISH
            }
            return RegionPermission.of(perm);
        }

        static boolean getAllowOrDeny(String string) {
            if (string == null) return false;
            switch (string) {
                case "allow":
                case "true":
                case "1":
                    return true;
                default:
                    return false;
            }
        }
    }
}


