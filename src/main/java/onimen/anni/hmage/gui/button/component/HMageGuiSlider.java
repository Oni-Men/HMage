package onimen.anni.hmage.gui.button.component;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class HMageGuiSlider extends GuiButton {

  public float value, min, max;
  public boolean isSliding, userIntegers;

  private static DecimalFormat formatter = new DecimalFormat("#.00");

  private final NumberButtonObject obj;

  public HMageGuiSlider(int id, int x, int y, int width, int height, NumberButtonObject obj) {
    super(id, x, y, width, height, "");

    this.obj = obj;

    this.min = obj.min;
    this.max = obj.max;
    this.value = obj.value;
    this.userIntegers = obj.useInts;

    setLabel();
  }

  public float getFloatValue() {
    return (max - min) * this.value + min;
  }

  public int getIntValue() {
    return (int) this.getFloatValue();
  }

  protected float roundValue(float v) {
    v = 0.01F * (float) Math.round(v / 0.01F);
    return v;
  }

  public void setLabel() {
    this.displayString = getLabel();
  }

  public String getLabel() {
    return String.valueOf(userIntegers ? getIntValue() : formatter.format(getFloatValue()));
  }

  @Override
  protected int getHoverState(boolean mouseOver) {
    return 0;
  }

  @Override
  protected void mouseDragged(Minecraft mc, int mousePosX, int mousePosY) {
    if (visible) {
      if (this.isSliding) {
        value = roundValue((float) (mousePosX - (x + 4)) / (float) (width - 8));

        if (value < 0.0F) {
          value = 0.0F;
        }
        if (value > 1.0F) {
          value = 1.0F;
        }

        setLabel();
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.drawTexturedModalRect(x + (int) (value * (float) (width - 8)), y, 0, 66, 4, 20);
      this.drawTexturedModalRect(x + (int) (value * (float) (width - 8)) + 4, y, 196, 66, 4, 20);
    }
  }

  @Override
  public boolean mousePressed(Minecraft mc, int mousePosX, int mousePosY) {
    if (super.mousePressed(mc, mousePosX, mousePosY)) {
      value = roundValue((float) (mousePosX - (x + 4)) / (float) (width - 8));

      if (value < 0.0F) {
        value = 0.0F;
      }
      if (value > 1.0F) {
        value = 1.0F;
      }

      setLabel();
      this.isSliding = true;
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void mouseReleased(int mousePosX, int mousePosY) {
    this.isSliding = false;
    if (this.obj.onReleased != null) {
      this.obj.onReleased.accept(this.userIntegers ? this.getIntValue() : this.getFloatValue());
    }
  }
}
