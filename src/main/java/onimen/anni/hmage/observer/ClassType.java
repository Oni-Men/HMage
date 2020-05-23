package onimen.anni.hmage.observer;

public enum ClassType {
  ACROBAT("Acrobat"),
  ALCHEMIST("Alchemist"),
  ARCHER("Archer"),
  ASSASSIN("Assassin"),
  BARD("Bard"),
  BERSEKER("Berserker"),
  BLOODMAGE("Bloodmage"),
  BUILDER("Builder"),
  CIVILIAN("Civilian"),
  DASHER("Dasher"),
  DEFENDER("Defender"),
  ENCHANTER("Enchanter"),
  ENGINEER("Engineer"),
  FARMER("Farmer"),
  HANDYMAN("Handyman"),
  HEALER("Healer"),
  HUNTER("Hunter"),
  ICEMAN("Iceman"),
  IMMOBILIZER("Immobilizer"),
  LUMBERJACK("Lumberjack"),
  MERCENARY("Mercenary"),
  MINER("Miner"),
  NINJA("Ninja"),
  PYRO("Pyro"),
  RIFT_WALKER("Rift Walker"),
  ROBIN_HOOD("Robin Hood"),
  SCORPIO("Scorpio"),
  SCOUT("Scout"),
  SNIPER("Sniper"),
  SPIDER("Spider"),
  SPY("Spy"),
  SUCCUBUS("Succubus"),
  SWAPPER("Swapper"),
  THOR("Thor"),
  TINKERER("Tinkerer"),
  TRANSPORTER("Transporter"),
  VAMPIRE("Vampire"),
  WARRIOR("Warrior"),
  WIZARD("Wizard");

  private String name;

  private ClassType(String name) {
    this.name = name;
  }

  public String getDisplayName() {
    return this.name;
  }

  public static ClassType getClassTypeFromName(String name) {
    for (ClassType classType : ClassType.values()) {
      if (classType.getDisplayName().equals(name)) { return classType; }
    }
    return ClassType.CIVILIAN;
  }
}