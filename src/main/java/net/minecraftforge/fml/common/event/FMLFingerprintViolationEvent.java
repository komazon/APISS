package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FMLFingerprintViolationEvent extends FMLStateEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLFingerprintViolationEvent.class);
    private final String expectedFingerprint;
    private final String foundFingerprint;

    public FMLFingerprintViolationEvent(String modId, String expectedFingerprint, String foundFingerprint) {
        super(modId);
        this.expectedFingerprint = expectedFingerprint;
        this.foundFingerprint = foundFingerprint;
        LOGGER.debug("[互換レイヤー] FMLFingerprintViolationEvent を生成: modId={}", modId);
    }

    public String getExpectedFingerprint() {
        return expectedFingerprint;
    }

    public String getFoundFingerprint() {
        return foundFingerprint;
    }
}
