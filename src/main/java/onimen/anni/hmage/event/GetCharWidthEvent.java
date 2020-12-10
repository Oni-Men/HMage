package onimen.anni.hmage.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class GetCharWidthEvent extends Event {

  private final char ch;
  private int width;

  public GetCharWidthEvent(char ch, int width) {
    this.ch = ch;
    this.width = width;
  }

  @Override
  public boolean isCancelable() {
    return true;
  }

  public char getChar() {
    return this.ch;
  }

  public int getWidth() {
    return this.width;
  }

  public float getWidthFloat() {
    return (float) this.width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

}
