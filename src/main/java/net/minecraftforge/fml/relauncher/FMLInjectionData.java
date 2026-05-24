package net.minecraftforge.fml.relauncher;

/**
 * 互換レイヤー: 1.12.2 の FMLInjectionData のダミー実装。
 *
 * 古いModやライブラリが参照する際、最低限の型解決を行う。
 */
public final class FMLInjectionData {

    public static final boolean deobfuscatedEnvironment = true;
    public static final boolean obfuscationEnabled = false;
    public static final Object[] data = new Object[7];

    static {
        data[0] = Boolean.TRUE; // deobfuscatedEnvironment
        data[1] = Boolean.FALSE; // obfuscationEnabled
        data[2] = null; // launchTarget / gameDir?
        data[3] = null; // version
        data[4] = null; // other launch data
        data[5] = null;
        data[6] = null;
    }

    private FMLInjectionData() {}

    public static Object[] data() {
        return data;
    }

    public static Object[] getData() {
        return data;
    }

    public static boolean isObfuscatedEnvironment() {
        return obfuscationEnabled;
    }
}
