package onimen.anni.hmage.observer.data;

import java.util.EnumMap;
import java.util.Map;

import onimen.anni.hmage.observer.AnniKillType;

public class KillCountData {

  private AnniTeamColor teamColor;

  private final String playerName;

  private Map<AnniTeamColor, Integer> meleeKillCount = new EnumMap<>(AnniTeamColor.class);

  private Map<AnniTeamColor, Integer> bowKillCount = new EnumMap<>(AnniTeamColor.class);

  public KillCountData(String playerName, AnniTeamColor teamColor) {
    this.playerName = playerName;
    this.teamColor = teamColor;
  }

  /** 近接攻撃のキル数 */
  private int meleeCount;

  /** 弓のキル数 */
  private int bowCount;

  public int getMeleeCount() {
    return meleeCount;
  }

  public int getBowCount() {
    return bowCount;
  }

  /**
   * キル数をカウントする。
   *
   * @param killType キル種別
   */
  public void incrementCount(AnniKillType killType, AnniTeamColor teamColor) {
    if (killType == AnniKillType.MELEE) {
      meleeCount++;
      meleeKillCount.compute(teamColor, (k, v) -> v == null ? 1 : v.intValue() + 1);
    } else {
      bowCount++;
      bowKillCount.compute(teamColor, (k, v) -> v == null ? 1 : v.intValue() + 1);
    }
  }

  /**
   * 総キル数を取得する。
   *
   * @return 総キル数
   */
  public int getTotalKillCount() {
    return bowCount + meleeCount;
  }

  public AnniTeamColor getTeamColor() {
    return teamColor;
  }

  public void setTeamColor(AnniTeamColor teamColor) {
    this.teamColor = teamColor;
  }

  public String getPlayerName() {
    return playerName;
  }

}
