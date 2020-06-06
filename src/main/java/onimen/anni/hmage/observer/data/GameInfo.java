package onimen.anni.hmage.observer.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

  /** Kill数のランキング */
  private List<AnniPlayerData> totalKillRanking = null;

  /** 近接キル数のランキング */
  private List<AnniPlayerData> meleeKillRanking = null;

  /** 射撃キル数のランキング */
  private List<AnniPlayerData> shotKillRanking = null;

  /** Nexusを削った回数のランキング */
  private List<AnniPlayerData> nexusRanking = null;

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
    nexusRanking = null;
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

    totalKillRanking = null;
  }

  public Collection<AnniPlayerData> getAllPlayerStats() {
    Collection<AnniPlayerData> data = new ArrayList<>(this.otherPlayerStatsMap.values());
    data.add(mePlayerData);
    return data;
  }

  public List<AnniPlayerData> getTotalKillRanking(long limit) {
    if (totalKillRanking == null || limit > totalKillRanking.size()) {
      totalKillRanking = getAllPlayerStats().stream()
          .sorted((a, b) -> b.getTotalKillCount() - a.getTotalKillCount())
          .filter(a -> a.getTotalKillCount() != 0)
          .limit(limit)
          .collect(Collectors.toList());
    }
    if (totalKillRanking.size() > limit) { return totalKillRanking.subList(0, (int) limit); }
    return totalKillRanking;
  }

  public List<AnniPlayerData> getMeleeKillRanking(long limit) {
    if (meleeKillRanking == null || limit > meleeKillRanking.size()) {
      meleeKillRanking = getSortedPlayerData((a, b) -> b.getMeleeCount() - a.getMeleeCount(), limit);
    }
    if (meleeKillRanking.size() > limit) { return meleeKillRanking.subList(0, (int) limit); }
    return meleeKillRanking;
  }

  public List<AnniPlayerData> getShotKillRanking(long limit) {
    if (shotKillRanking == null || limit > shotKillRanking.size()) {
      shotKillRanking = getSortedPlayerData((a, b) -> b.getBowCount() - a.getBowCount(), limit);
    }
    if (shotKillRanking.size() > limit) { return shotKillRanking.subList(0, (int) limit); }
    return shotKillRanking;
  }

  public List<AnniPlayerData> getNexusRanking(long limit) {
    if (nexusRanking == null || limit > nexusRanking.size()) {
      nexusRanking = getSortedPlayerData((a, b) -> b.getNexusDamageCount() - a.getNexusDamageCount(), limit);
    }
    if (nexusRanking.size() > limit) { return .nexusRanking.subList(0, (int) limit); }
    return nexusRanking
  }

  private List<AnniPlayerData> getSortedPlayerData(Comparator<? super AnniPlayerData> comparator, long limit) {
    return getAllPlayerStats().stream()
        .sorted(comparator)
        .limit(limit)
        .collect(Collectors.toList());
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
