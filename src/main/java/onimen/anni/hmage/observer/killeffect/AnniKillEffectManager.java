package onimen.anni.hmage.observer.killeffect;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketParticles;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.observer.constant.AnniKillEffectParticles;
import onimen.anni.hmage.observer.killeffect.imple.DefaultKillEffect;

public class AnniKillEffectManager {

  private final static AnniKillEffectManager INSTANCE = new AnniKillEffectManager();

  public static AnniKillEffectManager getInstance() {
    return INSTANCE;
  }

  private AnniKillEffectManager() {
    String id = Preferences.getString("anni.kill-effect-id", null);
    if (id != null) {
      useKillEffect = AnniKillEffectList.findById(id);
    }
  }

  private boolean executeKillEffect = false;

  private long lastOriginalExecuteTime = -1;

  private AnniKillEffect useKillEffect = null;

  /**
   * キルエフェクトを実行する。
   *
   * @param particles パーティクル
   * @return 実行した場合はtrue
   */
  public boolean executeKillEffect(SPacketParticles particle) {
    if (useKillEffect == null || useKillEffect == DefaultKillEffect.INSTANCE) { return false; }

    //実行フラグがfalseなら実行しない
    if (!executeKillEffect) { return false; }

    //anni上でキルエフェクトが実行されたから1秒以内であることを確認
    if (lastOriginalExecuteTime > System.currentTimeMillis() - 1000) {
      executeKillEffect = false;
    }

    //パーティクルがkilleffectかどうかを確認
    boolean isKillEffect = false;
    AnniKillEffectParticles[] values = AnniKillEffectParticles.values();
    for (AnniKillEffectParticles anniKillEffectParticle : values) {
      if (anniKillEffectParticle.isSame(particle)) {
        isKillEffect = true;
        break;
      }
    }

    //kill effectのパーティクルでない場合は何もしない
    if (!isKillEffect) { return false; }

    //キルエフェクトを実行
    useKillEffect.execute(Minecraft.getMinecraft().player.getEntityWorld(), (float) particle.getXCoordinate(),
        (float) particle.getYCoordinate(), (float) particle.getZCoordinate());
    executeKillEffect = false;

    return true;
  }

  /**
   * キルエフェクトを設定する。
   *
   * @param anniKillEffect キルエフェクト
   */
  public void setKillEffect(AnniKillEffect anniKillEffect) {
    this.useKillEffect = anniKillEffect;
    Preferences.setString("anni.kill-effect-id", anniKillEffect.getId());
  }

  public void onKillPlayer() {
    lastOriginalExecuteTime = System.currentTimeMillis();
    executeKillEffect = true;
  }

  public AnniKillEffect getUseKillEffect() {
    return useKillEffect;
  }
}
