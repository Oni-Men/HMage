package onimen.anni.hmage.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderFontEvent extends Event {

  private char ch;
  private boolean italic;
  private float width = 0.0F;
  private float posX, posY;


  public RenderFontEvent(char ch, boolean italic, float posX, float posY) {
    this.ch = ch;
    this.italic = italic;
    this.posX = posX;
    this.posY = posY;
  }

  public float getPosX() {
    return this.posX;
  }

  public void setPosX(float posX) {
    this.posX = posX;
  }

  public float getPosY() {
    return this.posY;
  }

  public void setPosY(float posY) {
    this.posY = posY;
  }

  public void setWidth(float width) {
    this.width = width;
  }

  public float getWidth() {
    return this.width;
  }

  public char getChar() {
    return this.ch;
  }

  public void setChar(char ch) {
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
