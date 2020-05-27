package onimen.anni.hmage.observer;

public enum GamePhase {
  STARTING(0),
  PHASE_1(1),
  PHASE_2(2),
  PHASE_3(3),
  PHASE_4(4),
  PHASE_5(5),
  ENDING(6),
  UNKNOWN(-1);

  private int phase;

  private GamePhase(int phase) {
    this.phase = phase;
  }

  public int getValue() {
    return this.phase;
  }

  public static GamePhase getGamePhasebyText(String text) {
    if (text.startsWith("Starts in ")) {
      return STARTING;
    } else if (text.startsWith("Ending")) { return ENDING; }
    switch (text) {
    case "Phase 1":
      return PHASE_1;
    case "Phase 2":
      return PHASE_2;
    case "Phase 3":
      return PHASE_3;
    case "Phase 4":
      return PHASE_4;
    case "Phase 5":
    case "NEXUS BLEED":
      return PHASE_5;
    }
    return UNKNOWN;
  }
}