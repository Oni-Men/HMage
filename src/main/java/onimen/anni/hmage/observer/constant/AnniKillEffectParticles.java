package onimen.anni.hmage.observer.constant;

import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.util.EnumParticleTypes;
import onimen.anni.hmage.observer.particle.AnniParticleData;

/**
 * ANNIのキルエフェクト。
 */
public enum AnniKillEffectParticles {
  /** BLOODY_MESS */
  BLOODY_MESS(
      new AnniParticleData(EnumParticleTypes.BLOCK_CRACK, 0.2f, 0.8f, 0.2f, 0.01f, 500, true, new int[] { 152 })),
  /** CONFETTI */
  CONFETTI(new AnniParticleData(EnumParticleTypes.REDSTONE, 0.4f, 1.0f, 0.4f, 2.0f, 200, true, null)),
  /** CLOUD_HELIX */
  CLOUD_HELIX(new AnniParticleData(EnumParticleTypes.CLOUD, 0.0f, 0.0f, 0.0f, 0.0f, 1, true, null)),
  /** FLAME */
  FLAME(new AnniParticleData(EnumParticleTypes.FLAME, 0.4f, 1.0f, 0.4f, 0.1f, 200, true, null));

  private AnniParticleData data;

  private AnniKillEffectParticles(AnniParticleData data) {
    this.data = data;
  }

  /**
   * 同じパーティクルかどうかをチェックする。
   *
   * @param particle パーティクル
   * @return 同じパーティクルの場合はtrue
   */
  public boolean isSame(SPacketParticles particle) {
    return data.sameParticle(particle);
  }

  /**
   * 指定したパーティクルがキルエフェクトかどうかを調べる。
   *
   * @param particle パーティクル
   * @return キルエフェクトの場合はtrue
   */
  public static boolean isKillEffect(SPacketParticles particle) {
    for (AnniKillEffectParticles effectParticles : values()) {
      if (effectParticles.isSame(particle)) { return true; }
    }
    return false;
  }
}
