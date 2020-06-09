package onimen.anni.hmage.module.hud.layout;

public class Layout {

  public enum LayoutType {
    LEFT(0),
    RIGHT(1),
    TOP(0),
    BOTTOM(1),
    CENTERX(2),
    CENTERY(2),
    VERTICAL(0),
    HORIZONTAL(1);

    public final int flg;

    private LayoutType(int flg) {
      this.flg = flg;
    }

  }

  private LayoutType xAxisLayout;
  private LayoutType yAxisLayout;
  private LayoutType direction;

  public static Layout getLayout() {
    return getLayout(0);
  }

  public static Layout getLayout(int code) {
    return new Layout(code);
  }

  private Layout(int code) {
    int x = code & 3;
    int y = code >> 3 & 3;
    int d = code >> 6 & 1;

    if (x == 0)
      this.left();
    else if (x == 1)
      this.right();
    else if (x == 2)
      this.centerx();

    if (y == 0)
      this.top();
    else if (y == 1)
      this.bottom();
    else if (y == 2)
      this.centery();

    if (d == 0)
      this.vertical();
    else if (d == 1)
      this.horizontal();
  }

  public int getCode() {
    return xAxisLayout.flg | yAxisLayout.flg << 3 | direction.flg << 6;
  }

  public Layout left() {
    this.xAxisLayout = LayoutType.LEFT;
    return this;
  }

  public Layout right() {
    this.xAxisLayout = LayoutType.RIGHT;
    return this;
  }

  public Layout top() {
    this.yAxisLayout = LayoutType.TOP;
    return this;
  }

  public Layout bottom() {
    this.yAxisLayout = LayoutType.BOTTOM;
    return this;
  }

  public Layout centerx() {
    this.xAxisLayout = LayoutType.CENTERX;
    return this;
  }

  public Layout centery() {
    this.yAxisLayout = LayoutType.CENTERY;
    return this;
  }

  public Layout vertical() {
    this.direction = LayoutType.VERTICAL;
    return this;
  }

  public Layout horizontal() {
    this.direction = LayoutType.HORIZONTAL;
    return this;
  }

  public Layout toggleDirection() {
    if (isHorizontal()) {
      this.direction = LayoutType.VERTICAL;
    } else {
      this.direction = LayoutType.HORIZONTAL;
    }
    return this;
  }

  public LayoutType getLayoutX() {
    return this.xAxisLayout;
  }

  public LayoutType getLayoutY() {
    return this.yAxisLayout;
  }

  public LayoutType getDirection() {
    return this.direction;
  }

  public boolean isRight() {
    return this.xAxisLayout == LayoutType.RIGHT;
  }

  public boolean isBottom() {
    return this.yAxisLayout == LayoutType.BOTTOM;
  }

  public boolean isHorizontal() {
    return this.direction == LayoutType.HORIZONTAL;
  }
}
