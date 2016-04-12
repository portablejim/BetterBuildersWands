package portablejim.bbw.basics;

/**
 * Enum for the directions that the wands can extend.
 */
public enum EnumFluidLock {
    STOPAT(1),
    IGNORE(2);

    public final int mask;

    EnumFluidLock(int mask) {
        this.mask = mask;
    }

    public  static EnumFluidLock fromMask(int inputMask) {
        EnumFluidLock locks[] = {STOPAT, IGNORE };

        int safeMask = inputMask & 3;
        return locks[safeMask - 1];
    }
}
