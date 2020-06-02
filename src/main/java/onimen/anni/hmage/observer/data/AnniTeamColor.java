package onimen.anni.hmage.observer.data;

import java.util.Arrays;
import java.util.Optional;

public enum AnniTeamColor {

  NO_JOIN("", "No Color"), RED("c", "Red"), GREEN("a", "Green"), BLUE("9", "Blue"), YELLOW("e", "Yellow");

  private String colorCode;
  private String colorName;

  private AnniTeamColor(String colorCode, String colorName) {
    this.colorCode = colorCode;
    this.colorName = colorName;
  }

  public static AnniTeamColor findByColorCode(String colorCode) {
    Optional<AnniTeamColor> optional = Arrays.stream(values()).filter(e -> e.colorCode.equals(colorCode)).findAny();
    return optional.orElse(NO_JOIN);
  }

  public String getColorName() {
    return colorName;
  }
}
