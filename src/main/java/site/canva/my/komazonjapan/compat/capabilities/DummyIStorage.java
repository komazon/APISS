package site.canva.my.komazonjapan.compat.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import java.util.Optional;

/**
 * 1.12.2 の Capability$IStorage をエミュレートするダミークラス。
 * 現代の NeoForge では Capability の管理方法が根本的に異なるため、
 * 互換レイヤーとして最小限のインターフェースを提供します。
 */
public interface DummyIStorage<T> {
    /**
     * Capability の値を取得します。
     * @param capability 取得したい Capability
     * @return Capability の値（Optional）
     */
    default Optional<T> getCapability(Capability<T> capability) {
        return Optional.empty();
    }

    /**
     * Capability の値を設定します。
     * @param capability 設定したい Capability
     * @param value 設定する値
     */
    default void setCapability(Capability<T> capability, T value) {
        // エミュレーションのため、実際の設定は行わない
    }
}
