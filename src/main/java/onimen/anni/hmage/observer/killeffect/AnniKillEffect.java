package onimen.anni.hmage.observer.killeffect;

import net.minecraft.world.World;

public interface AnniKillEffect {

  /**
   * IDを取得する。
   *
   * @return id
   */
  String getId();

  /**
   * キルエフェクト名を取得する。
   *
   * @return キルエフェクト
   */
  String getName();

  /**
   * キルエフェクトを実行する。
   *
   * @param w ワールド
   * @param x x
   * @param y y
   * @param z z
   */
  void execute(World w, float x, float y, float z);

}
