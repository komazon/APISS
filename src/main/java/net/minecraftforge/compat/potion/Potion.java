package net.minecraftforge.compat.potion;

/**
 * Legacy Forge 1.12.2 Potion compatibility stub.
 */
public class Potion {
    public static final Potion empty = new Potion();

    public String getName() {
        return "potion";
    }

    public boolean isInstant() {
        return false;
    }
}
