package onimen.anni.hmage.observer.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import onimen.anni.hmage.observer.AnniKillType;
import onimen.anni.hmage.observer.ClassType;
import onimen.anni.hmage.observer.GamePhase;

public class GameInfo {

  /** ゲームID */
  private UUID gameInfoId;

  /** ゲーム開始時間 */
  private long gameTimestamp;

  /** プレイヤーが使っている職業 */
  private ClassType usingClassType;

  /** 現在のフェーズ */
  private GamePhase gamePhase;

  /** 他のプレイヤーのキル数 */
  private Map<String, AnniPlayerData> otherPlayerStatsMap = new HashMap<>();

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
    this.gameInfoId = UUID.randomUUID();
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

  public AnniTeamColor getMeTeamColor() {
    return mePlayerData.getTeamColor();
  }

  public AnniPlayerData getMePlayerData() {
    return mePlayerData;
  }

  public UUID getGamenInfoId() {
    return gameInfoId;
  }

  /**
   * ネクサスを削った回数をカウントする
   *
   * @param attacker
   * @param attackerTeam
   * @param damageTeam
   */
  public void addNexusDamageCount(String attacker, AnniTeamColor attackerTeam,
      AnniTeamColor damageTeam) {
    if (Minecraft.getMinecraft().player.getName().equals(attacker)) {
      //自身のデータを更新
      mePlayerData.setTeamColor(attackerTeam);
      mePlayerData.nexusDamage(damageTeam);
    } else {
      //自身以外のデータを更新
      AnniPlayerData countData = otherPlayerStatsMap.computeIfAbsent(attacker,
          k -> new AnniPlayerData(k, attackerTeam));
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

    //殺したプレイヤーと死んだプレイヤーが同じである場合はカウントしない
    if (killer.equals(dead)) { return; }

    //キル数をカウント
    if (Minecraft.getMinecraft().player.getName().equals(killer)) {
      //自身のデータを更新
      mePlayerData.setTeamColor(killerTeam);
      mePlayerData.incrementCount(killType, deadTeam);
    } else {
      //自身以外のデータを更新
      AnniPlayerData countData = otherPlayerStatsMap.computeIfAbsent(killer, k -> new AnniPlayerData(k, killerTeam));
      countData.incrementCount(killType, deadTeam);
    }

    //デス数をカウント
    if (Minecraft.getMinecraft().player.getName().equals(dead)) {
      //自身のデータを更新
      mePlayerData.setTeamColor(deadTeam);
      mePlayerData.incrementDeathCount();
    } else {
      //自身以外のデータを更新
      AnniPlayerData countData = otherPlayerStatsMap.computeIfAbsent(dead, k -> new AnniPlayerData(k, deadTeam));
      countData.incrementDeathCount();
    }

  }

  public Map<String, AnniPlayerData> getOtherPlayerStatsMap() {
    return this.otherPlayerStatsMap;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((gameInfoId == null) ? 0 : gameInfoId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GameInfo other = (GameInfo) obj;
    if (gameInfoId == null) {
      if (other.gameInfoId != null)
        return false;
    } else if (!gameInfoId.equals(other.gameInfoId))
      return false;
    return true;
  }

}
