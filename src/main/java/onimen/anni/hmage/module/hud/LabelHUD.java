package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import onimen.anni.hmage.module.annotation.ColorOption;

public abstract class LabelHUD extends AbstractHUD {

  protected FontRenderer fr;
  protected String text;

  @ColorOption(id = "color", name = "Text color")
  protected int color = 0xFFFFFFFF;

  @ColorOption(id = "background", name = "Background color")
  protected int background = 0x4D000000;
  protected int paddingX, paddingY = 0;

  public LabelHUD() {
    loadPreferences();
  }

  public LabelHUD(String text) {
    this.text = text;
    loadPreferences();
  }

  @Override
  public int getWidth() {
    if (this.fr == null || this.text == null)
      return (int) (paddingX * 2 * this.scale);
    if (widthHashCode != text.hashCode()) {
      cachedWidth = (int) ((this.fr.getStringWidth(text) + 2 * paddingX) * this.scale);
      widthHashCode = text.hashCode();
    }
    return cachedWidth;
  }

  @Override
  public int getHeight() {
    return (int) (((this.fr == null ? 0 : this.fr.FONT_HEIGHT) + 2 * paddingY) * this.scale);
  }

  @Override
  public void drawItem(Minecraft mc, boolean layoutMode) {
    if (this.fr == null)
      this.fr = mc.fontRenderer;

    if (this.text != null && !this.text.isEmpty()) {
      ScaledResolution sr = new ScaledResolution(mc);
      int width = getWidth();
      int height = getHeight();

      GlStateManager.pushMatrix();
      GlStateManager.translate(getComputedX(sr), getComputedY(sr), 0);

      if (background != 0) {
        drawRect(0, 0, width, height, background);
      }
      GlStateManager.translate(paddingX * this.scale, (paddingY + 1) * this.scale, 0);
      GlStateManager.scale(this.scale, this.scale, 1.0F);

      mc.fontRenderer.drawString(text, 0, 0, color);

      GlStateManager.popMatrix();
    }
  }
}
