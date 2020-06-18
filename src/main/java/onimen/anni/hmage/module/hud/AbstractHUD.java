package onimen.anni.hmage.module.hud;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.module.AbstractModule;
import onimen.anni.hmage.module.annotation.FloatOption;
import onimen.anni.hmage.module.annotation.IntegerOption;
import onimen.anni.hmage.module.hud.layout.Layout;
import onimen.anni.hmage.module.hud.layout.Layout.LayoutType;

public abstract class AbstractHUD extends AbstractModule implements InterfaceHUD {

  protected float zLevel = 0.0f;

  protected int widthHashCode, heightHashCode;
  protected int cachedWidth, cachedHeight;

  @IntegerOption(id = "x")
  protected int x;

  @IntegerOption(id = "y")
  protected int y;

  @FloatOption(id = "scale", min = 0.0F, max = 10.0F)
  protected float scale = 1.0f;

  @Override
  public void setX(int value) {
    this.x = value;
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public void setY(int value) {
    this.y = value;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public void setScale(float value) {
    this.widthHashCode = 0;
    this.heightHashCode = 0;
    this.scale = value;
  }

  @Override
  public float getScale() {
    return this.scale;
  }

  @Override
  public float getDefaultScale() {
    return 1.0F;
  }

  @Override
  public void setLayout(Layout layout) {
    widthHashCode = 0;
    heightHashCode = 0;
    Preferences.setInt(this.getId() + ".layout", layout.getCode());
  }

  @Override
  public Layout getLayout() {
    return Layout.getLayout(Preferences.getInt(this.getId() + ".layout", 0));
  }

  @Override
  public boolean isHorizontal() {
    return getLayout().getDirection() == LayoutType.HORIZONTAL;
  }

  @Override
  public int getComputedX(ScaledResolution sr) {
    int x = getX();
    switch (getLayout().getLayoutX()) {
    case LEFT:
      break;
    case RIGHT:
      x += sr.getScaledWidth() - getWidth();
      break;
    case CENTERX:
      x += (sr.getScaledWidth() - getWidth()) / 2;
      break;
    default:
      break;
    }
    return x;
  }

  @Override
  public int getComputedY(ScaledResolution sr) {
    int y = getY();
    switch (getLayout().getLayoutY()) {
    case TOP:
      break;
    case BOTTOM:
      y += sr.getScaledHeight() - getHeight();
      break;
    case CENTERY:
      y += (sr.getScaledHeight() - getHeight()) / 2;
      break;
    default:
      break;
    }
    return y;
  }

  @Override
  public void drawItem(Minecraft mc) {
    this.drawItem(mc, false);
  }

  public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
    bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) this.zLevel)
        .tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F))
        .endVertex();
    bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel)
        .tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F))
        .endVertex();
    bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) this.zLevel)
        .tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F))
        .endVertex();
    bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel)
        .tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F))
        .endVertex();
    tessellator.draw();
  }

  public void drawRect(int x, int y, int width, int height, int color) {

    float a = (float) (color >> 24 & 255) / 255F;
    float r = (float) (color >> 16 & 255) / 255F;
    float g = (float) (color >> 8 & 255) / 255F;
    float b = (float) (color & 255) / 255F;

    GlStateManager.disableTexture2D();
    GlStateManager.enableBlend();

    GlStateManager.color(r, g, b, a);
    GlStateManager.glBegin(GL11.GL_QUADS);
    GlStateManager.glVertex3f(x, y + height, 0);
    GlStateManager.glVertex3f(x + width, y + height, 0);
    GlStateManager.glVertex3f(x + width, y, 0);
    GlStateManager.glVertex3f(x, y, 0);
    GlStateManager.glEnd();

    GlStateManager.disableBlend();
    GlStateManager.enableTexture2D();
  }

  public boolean isInside(ScaledResolution sr, int x, int y) {
    int left = this.getComputedX(sr);
    int top = this.getComputedY(sr);
    return (x >= left && x <= left + getWidth() && y >= top && y <= top + getHeight());
  }
}
