package onimen.anni.hmage.observer;

public enum GamePhase {
  STARTING(0, "Starting"),
  PHASE_1(1, "Phase 1"),
  PHASE_2(2, "Phase 2"),
  PHASE_3(3, "Phase 3"),
  PHASE_4(4, "Phase 4"),
  PHASE_5(5, "Phase 5"),
  ENDING(6, "Ending"),
  UNKNOWN(-1, "none");

  private int phase;
  private String text;

  private GamePhase(int phase, String text) {
    this.phase = phase;
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public int getValue() {
    return this.phase;
  }

  public static GamePhase getGamePhasebyText(String text) {
    if (text.startsWith("Starts in ")) {
      return STARTING;
    } else if (text.startsWith("Ending")) {
      return ENDING;
    } else if (text.startsWith("Phase 1")) {
      return PHASE_1;
    } else if (text.startsWith("Phase 2")) {
      return PHASE_2;
    } else if (text.startsWith("Phase 3")) {
      return PHASE_3;
    } else if (text.startsWith("Phase 4")) {
      return PHASE_4;
    } else if (text.startsWith("Phase 5")) {
      return PHASE_5;
    } else if (text.contains("NEXUS BLEED")) { return PHASE_5; }
    return UNKNOWN;
  }
}