package portablejim.bbw.basics;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * Point to represent 3D space. Functions to move in that space.
 */
public class Point3d {
    public static int UP = 1;
    public static int DOWN = -1;
    public static int EAST = 1;
    public static int WEST = -1;
    public static int SOUTH = 1;
    public static int NORTH = -1;

    public int x, y, z;

    public Point3d(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3d move(EnumFacing direction) {
        int newX = x, newY = y, newZ = z;
        switch(direction) {
            case UP:
                newY += 1;
                break;
            case DOWN:
                newY -= 1;
                break;
            case NORTH:
                newZ -= 1;
                break;
            case SOUTH:
                newZ += 1;
                break;
            case EAST:
                newX += 1;
                break;
            case WEST:
                newX -= 1;
                break;
        }
        return new Point3d(newX, newY, newZ);
    }

    public Point3d move(int dx, int dy, int dz) {
        int newX = x, newY = y, newZ = z;
        newX += dx;
        newY += dy;
        newZ += dz;

        return new Point3d(newX, newY, newZ);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point3d that = (Point3d) o;
        return x == that.x && y == that.y && z == that.z;

    }

    @Override
    public int hashCode() {
        int hashX = (x << 22);
        int hashY = (y << 22) >> 10;
        int hashZ = (z << 22) >> 20;
        return hashX + hashY + hashZ;
    }

    public BlockPos toBlockPos() {
        return new BlockPos(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d)", x, y, z);
    }

}
