package net.minecraftforge.compat.potion;

/**
 * Legacy Forge 1.12.2 PotionEffect compatibility stub.
 */
public class PotionEffect {
    private final Potion potion;
    private int duration;
    private int amplifier;
    private boolean ambient;

    public PotionEffect(Potion potion, int duration) {
        this(potion, duration, 0, false);
    }

    public PotionEffect(Potion potion, int duration, int amplifier) {
        this(potion, duration, amplifier, false);
    }

    public PotionEffect(Potion potion, int duration, int amplifier, boolean ambient) {
        this.potion = potion;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
    }

    public Potion getPotion() {
        return potion;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public boolean getIsAmbient() {
        return ambient;
    }

    public boolean doesShowParticles() {
        return true;
    }

    public void setPotionDuration(int duration) {
        this.duration = duration;
    }
}
