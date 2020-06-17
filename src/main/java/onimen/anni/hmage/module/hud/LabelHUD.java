package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public abstract class LabelHUD extends AbstractHUD {

  protected FontRenderer fr;
  protected String text;
  protected int color = 0xFFFFFFFF;
  protected int background = 0x4D000000;
  protected int paddingX, paddingY = 0;

  public LabelHUD() {
  }

  public LabelHUD(String text) {
    this.text = text;
  }

  @Override
  public int getWidth() {
    if (this.fr == null || this.text == null)
      return paddingX * 2;
    if (widthHashCode != text.hashCode()) {
      cachedWidth = this.fr.getStringWidth(text) + 2 * paddingX;
      widthHashCode = text.hashCode();
    }
    return (int) (cachedWidth * this.scale);
  }

  @Override
  public int getHeight() {
    return (int) ((this.fr == null ? 0 : this.fr.FONT_HEIGHT) + 2 * paddingY * this.scale);
  }

  @Override
  public void drawItem(Minecraft mc, boolean layoutMode) {
    if (this.fr == null)
      this.fr = mc.fontRenderer;

    if (this.text != null && !this.text.isEmpty()) {
      ScaledResolution sr = new ScaledResolution(mc);
      int width = getWidth();
      int height = getHeight();

      int x = 0;
      int y = 0;

      GlStateManager.pushMatrix();
      GlStateManager.translate(getComputedX(sr), getComputedY(sr), 0);
      GlStateManager.scale(this.scale, this.scale, 1.0F);

      if (background != 0) {
        drawRect(x, y, width, height, background);
      }

      mc.fontRenderer.drawString(text, x + paddingX, y + paddingY + 1, color);

      GlStateManager.popMatrix();
    }
  }
}
