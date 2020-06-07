package onimen.anni.hmage.observer.constant;

import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import onimen.anni.hmage.observer.particle.AnniParticleData;

/**
 * HMage ModeでカスタマイズしたKillEffect。
 */
public enum CustomKillEffectParticles {
  /** BLOODY_MESS */
  PARTICLE1("Star Spark",
      new AnniParticleData(EnumParticleTypes.FIREWORKS_SPARK, 1, 2, 1, 0, 250, true, new int[] {}));

  private AnniParticleData data;

  private CustomKillEffectParticles(String name, AnniParticleData data) {
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
    for (CustomKillEffectParticles effectParticles : values()) {
      if (effectParticles.isSame(particle)) { return true; }
    }
    return false;
  }

  public void playParticle(World w, float x, float y, float z) {
    data.playParticle(w, x, y, z);
  }
}
