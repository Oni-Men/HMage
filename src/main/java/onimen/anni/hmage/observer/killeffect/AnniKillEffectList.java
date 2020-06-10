package onimen.anni.hmage.observer.killeffect;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import onimen.anni.hmage.observer.killeffect.imple.DefaultKillEffect;
import onimen.anni.hmage.observer.killeffect.imple.StarSparkKillEffect;

public class AnniKillEffectList {

  private static Map<String, AnniKillEffect> killEffectMap = new LinkedHashMap<>();

  static {
    register(DefaultKillEffect.INSTANCE);
    register(new StarSparkKillEffect());
  }

  /**
   * キルエフェクトを登録する。
   *
   * @param effect キルエフェクト
   */
  private static void register(AnniKillEffect effect) {
    killEffectMap.put(effect.getId(), effect);
  }

  /**
   * 全てのキルエフェクトを取得する。
   *
   * @return 全てのキルエフェクト
   */
  public static Collection<AnniKillEffect> getAllKillEffect() {
    return killEffectMap.values();
  }

  /**
   * IDに紐づくキルエフェクトを取得する。
   *
   * @param id ID
   * @return キルエフェクト
   */
  public static AnniKillEffect findById(String id) {
    return killEffectMap.get(id);
  }
}
