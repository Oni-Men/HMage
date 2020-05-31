package onimen.anni.hmage.observer.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import onimen.anni.hmage.observer.AnniKillType;
import onimen.anni.hmage.observer.ClassType;
import onimen.anni.hmage.observer.GamePhase;

public class GameInfo {

  private ClassType usingClassType;

  private GamePhase gamePhase;

  private int nexusAttackCount;

  private Map<String, KillCountData> killCountMap = new HashMap<>();

  private KillCountData meKillCount = new KillCountData(Minecraft.getMinecraft().player.getName(),
      AnniTeamColor.NO_JOIN);

  @Nullable
  private String mapName;

  public GameInfo() {
    this.usingClassType = ClassType.CIVILIAN;
    this.gamePhase = GamePhase.UNKNOWN;
    this.nexusAttackCount = 0;
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

  public void setMapName(String mapName) {
    this.mapName = mapName;
  }

  public int getKillCount() {
    return meKillCount.getTotalKillCount();
  }

  public int getMeleeKillCount() {
    return meKillCount.getMeleeCount();
  }

  public int getShotKillCount() {
    return meKillCount.getBowCount();
  }

  public int getNexusAttackCount() {
    return this.nexusAttackCount;
  }

  public void incrementNexusDamage() {
    this.nexusAttackCount++;
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
    //今は自身のキルログのみカウントする
    if (Minecraft.getMinecraft().player.getName().equals(killer)) {
      meKillCount.setTeamColor(killerTeam);
      meKillCount.incrementCount(killType, deadTeam);
    } else {
      KillCountData countData = killCountMap.computeIfAbsent(killer, k -> new KillCountData(k, killerTeam));
      countData.incrementCount(killType, deadTeam);
    }
  }
}
