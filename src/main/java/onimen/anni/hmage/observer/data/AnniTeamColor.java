package onimen.anni.hmage.observer.data;

import java.util.Arrays;
import java.util.Optional;

public enum AnniTeamColor {

  NO_JOIN(""), RED("c"), GREEN("a"), BLUE("9"), YELLOW("e");

  private String colorCode;

  private AnniTeamColor(String colorCode) {
    this.colorCode = colorCode;
  }

  public static AnniTeamColor findByColorCode(String colorCode) {
    Optional<AnniTeamColor> optional = Arrays.stream(values()).filter(e -> e.colorCode.equals(colorCode)).findAny();
    return optional.orElse(NO_JOIN);
  }
}
