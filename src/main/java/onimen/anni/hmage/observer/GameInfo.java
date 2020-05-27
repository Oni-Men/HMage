package onimen.anni.hmage.observer;

import javax.annotation.Nullable;

public class GameInfo {

  private ClassType usingClassType;
  private GamePhase gamePhase;
  private int meleeKillCount;
  private int shotKillCount;
  private int nexusAttackCount;

  @Nullable
  private String mapName;

  public GameInfo() {
    this.usingClassType = ClassType.CIVILIAN;
    this.gamePhase = GamePhase.UNKNOWN;
    this.meleeKillCount = 0;
    this.shotKillCount = 0;
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
    return this.meleeKillCount + this.shotKillCount;
  }

  public int getMeleeKillCount() {
    return this.meleeKillCount;
  }

  public void incrementMeleeKill() {
    this.meleeKillCount++;
  }

  public int getShotKillCount() {
    return this.shotKillCount;
  }

  public void incrementShotKill() {
    this.shotKillCount++;
  }

  public int getNexusAttackCount() {
    return this.nexusAttackCount;
  }

  public void incrementNexusDamage() {
    this.nexusAttackCount++;
  }
}
