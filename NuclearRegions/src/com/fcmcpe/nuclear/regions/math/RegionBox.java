package com.fcmcpe.nuclear.regions.math;

/**
 * Created on 2015/12/18 by xtypr.
 * Package com.fcmcpe.nuclear.regions.math in project NuclearPlugins .
 */
public class RegionBox {
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;

    public static RegionBox of(int x1, int y1, int z1, int x2, int y2, int z2) {
        RegionBox box = new RegionBox();
        box.minX = Math.min(x1, x2);
        box.maxX = Math.max(x1, x2);
        box.minY = Math.min(y1, y2);
        box.maxY = Math.max(y1, y2);
        box.minZ = Math.min(z1, z2);
        box.maxZ = Math.max(z1, z2);
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
}
