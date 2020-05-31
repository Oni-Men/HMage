package onimen.anni.hmage.observer.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import onimen.anni.hmage.observer.AnniKillType;
import onimen.anni.hmage.observer.ClassType;
import onimen.anni.hmage.observer.GamePhase;

public class GameInfo {

  /** ゲーム開始時間 */
  private long gameTimestamp;

  /** プレイヤーが使っている職業 */
  private ClassType usingClassType;

  /** 現在のフェーズ */
  private GamePhase gamePhase;

  /** 他のプレイヤーのキル数 */
  private Map<String, AnniPlayerData> killCountMap = new HashMap<>();

  /** 自身のキル数 */
  private AnniPlayerData mePlayerData = new AnniPlayerData(Minecraft.getMinecraft().player.getName(),
      AnniTeamColor.NO_JOIN);

  /** Map名 */
  @Nullable
  private String mapName;

  public GameInfo() {
    this.usingClassType = ClassType.CIVILIAN;
    this.gamePhase = GamePhase.UNKNOWN;
    this.gameTimestamp = System.currentTimeMillis();
  }

  public ClassType getClassType() {
    return this.usingClassType;
  }

  public void setClassType(ClassType classType) {
    if (classType != null)
      this.usingClassType = classType;
  }

  public GamePhase getGamePhase() {
    return this.gamePhase;
  }

  public void setGamePhase(GamePhase gamePhase) {
    if (gamePhase != null)
      this.gamePhase = gamePhase;
  }

  public String getMapName() {
    return this.mapName;
  }

  public long getGameTimestamp() {
    return gameTimestamp;
  }

  public void setMapName(String mapName) {
    this.mapName = mapName;
  }

  public int getKillCount() {
    return mePlayerData.getTotalKillCount();
  }

  public int getMeleeKillCount() {
    return mePlayerData.getMeleeCount();
  }

  public int getShotKillCount() {
    return mePlayerData.getBowCount();
  }

  public int getNexusAttackCount() {
    return mePlayerData.getNexusDamageCount();
  }

  /**
   * キル数をカウントする。
   *
   * @param killer 倒したプレイヤー
   * @param dead 倒されたプレイヤー
   * @param killType キルの方法
   */
  public void addNexusDamageCount(String attacker, AnniTeamColor attackerTeam,
      AnniTeamColor damageTeam) {
    if (Minecraft.getMinecraft().player.getName().equals(attacker)) {
      //自身のデータを更新
      mePlayerData.setTeamColor(attackerTeam);
      mePlayerData.nexusDamage(damageTeam);
    } else {
      //自身以外のデータを更新
      AnniPlayerData countData = killCountMap.computeIfAbsent(attacker, k -> new AnniPlayerData(k, attackerTeam));
      countData.nexusDamage(damageTeam);
    }
  }

  /**
   * キル数をカウントする。
   *
   * @param killer 倒したプレイヤー
   * @param dead 倒されたプレイヤー
   * @param killType キルの方法
   */
  public void addKillCount(String killer, AnniTeamColor killerTeam,
      String dead, AnniTeamColor deadTeam, AnniKillType killType) {
    if (Minecraft.getMinecraft().player.getName().equals(killer)) {
      //自身のデータを更新
      mePlayerData.setTeamColor(killerTeam);
      mePlayerData.incrementCount(killType, deadTeam);
    } else {
      //自身以外のデータを更新
      AnniPlayerData countData = killCountMap.computeIfAbsent(killer, k -> new AnniPlayerData(k, killerTeam));
      countData.incrementCount(killType, deadTeam);
    }
  }
}
