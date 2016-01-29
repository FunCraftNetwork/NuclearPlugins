package com.fcmcpe.nuclear.regions.math;

import cn.nukkit.level.Position;
import com.fcmcpe.nuclear.regions.data.RegionBox;

import java.util.Objects;

/**
 * Created on 2015/12/18 by xtypr.
 * Package com.fcmcpe.nuclear.regions.math in project NuclearPlugins .
 */
public class ZonedRegionBox implements RegionBox {
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;

    private String levelName;

    public static ZonedRegionBox of(Position pos1, Position pos2) {
        if (pos1 == null || pos2 == null) return null;
        if (pos1.getLevel() != pos2.getLevel()) return null;
        return of(pos1.getFloorX(), pos1.getFloorY(), pos1.getFloorZ(),
                pos2.getFloorX(), pos2.getFloorY(), pos2.getFloorZ(), pos1.getLevel().getName());
    }

    public static ZonedRegionBox of(int x1, int y1, int z1, int x2, int y2, int z2, String levelName) {
        ZonedRegionBox box = new ZonedRegionBox();
        box.minX = Math.min(x1, x2);
        box.maxX = Math.max(x1, x2);
        box.minY = Math.min(y1, y2);
        box.maxY = Math.max(y1, y2);
        box.minZ = Math.min(z1, z2);
        box.maxZ = Math.max(z1, z2);
        box.levelName = levelName;
        return box;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public String getLevelName() {
        return levelName;
    }

    public boolean isInBox(Position position) {
        return ((position.getX() >= minX) && (position.getX() <= maxX)) &&
                ((position.getY() >= minY) && (position.getY() <= maxY)) &&
                ((position.getZ() >= minZ) && (position.getZ() <= maxZ)) &&
                Objects.equals(position.getLevel().getName(), getLevelName());
    }

    @Override
    public double getSize() {
        return ((double) getMaxX() - (double) getMinX()) *
                ((double) getMaxY() - (double) getMinY()) *
                ((double) getMaxZ() - (double) getMinZ());
    }

    @Override
    public String toString() {
        return "RegionBox["+levelName+":("+minX+","+minY+","+minZ+") to ("+maxX+","+maxY+","+maxZ+")]";
    }
}
