package onimen.anni.hmage.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PositionHelper {

  public enum PositionType {
    LEFT("left", 1),
    RIGHT("right", 1 << 1),
    TOP("top", 1 << 2),
    BOTTOM("bottom", 1 << 3),
    CENTER_X("centerx", PositionType.LEFT.getFlag() & PositionType.RIGHT.getFlag()),
    CENTER_Y("centery", PositionType.TOP.getFlag() & PositionType.BOTTOM.getFlag()),
    HORIZONTAL("horizontal", 1 << 4);

    private final String text;
    private final int flag;

    PositionType(String s, int i) {
      this.text = s;
      this.flag = i;
    }

    public String getText() {
      return this.text;
    }

    public int getFlag() {
      return this.flag;
    }
  }

  public static class Position {
    public final boolean left;
    public final boolean right;
    public final boolean top;
    public final boolean bottom;
    public final boolean centerx;
    public final boolean centery;
    public final boolean horizontal;

    public Position(int positionBitIn) {
      this.left = (positionBitIn & 1) == 1;
      this.right = (positionBitIn >> 1 & 1) == 1;
      this.top = (positionBitIn >> 2 & 1) == 1;
      this.bottom = (positionBitIn >> 3 & 1) == 1;

      this.centerx = this.left && this.right;
      this.centery = this.top && this.bottom;

      this.horizontal = (positionBitIn >> 4 & 1) == 1;
    }

    public boolean isHorizontal() {
      return this.horizontal;
    }

    public boolean isVertical() {
      return !this.horizontal;
    }
  }

  public static int getPositionBitFromString(String str) {
    int position = 0;
    List<String> list = Arrays.stream(str.toLowerCase().split("_")).collect(Collectors.toList());

    if (list.contains("centerx")) {
      list.add("left");
      list.add("right");
    }

    if (list.contains("centery")) {
      list.add("top");
      list.add("bottom");
    }

    for (PositionType type : PositionType.values()) {
      if (list.contains(type.getText())) {
        position |= type.getFlag();
      }
    }

    return position;
  }

  public static String getStringFromPositionBit(int position) {
    String[] list = new String[PositionType.values().length];

    int i = 0;
    for (PositionType type : PositionType.values()) {
      if ((position & type.getFlag()) > 0) {
        list[i] = type.getText();
      } else {
        list[i] = "";
      }
      i++;
    }

    return String.join("_", list);
  }
}
