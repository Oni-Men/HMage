package onimen.anni.hmage.observer.config;

public enum AnniConfigKey {

  ANNI_SCOREBOARD("anni_scoreboard"),
  ACROBAT_CD("acrobat_cd");
  
  private final String key;
  
  AnniConfigKey(String key) {
    this.key  = key;
  }
  
  public String getKey() {
    return this.key;
  }
}
