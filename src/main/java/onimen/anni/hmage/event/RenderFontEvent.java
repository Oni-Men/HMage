package onimen.anni.hmage.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderFontEvent extends Event {

  private char ch;
  private boolean italic;
  private float width = 0.0F;

  public RenderFontEvent(char ch, boolean italic) {
    this.ch = ch;
    this.italic = italic;
  }

  public void setWidth(float width) {
    this.width = width;
  }

  public float getWidth() {
    return this.width;
  }

  public char getCh() {
    return this.ch;
  }

  public void setCh(char ch) {
    this.ch = ch;
  }

  public boolean isItalic() {
    return this.italic;
  }

  public void setItalic(boolean italic) {
    this.italic = italic;
  }

  @Override
  public boolean isCancelable() {
    return true;
  }
}
