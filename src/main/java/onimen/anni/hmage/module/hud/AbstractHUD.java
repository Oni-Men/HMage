package onimen.anni.hmage.module.hud;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.module.AbstractModule;

public abstract class AbstractHUD extends AbstractModule implements InterfaceHUD {

  protected float zLevel = 0.0f;

  @Override
  public void setX(int value) {
    Preferences.setInt(getName() + ".x", value);
  }

  @Override
  public int getX() {
    return Preferences.getInt(getName() + ".x", 0);
  }

  @Override
  public void setY(int value) {
    Preferences.setInt(getName() + ".y", value);
  }

  @Override
  public int getY() {
    return Preferences.getInt(getName() + ".y", 0);
  }

  @Override
  public void setPosition(int position) {
    Preferences.setInt(getName() + ".position", position);
  }

  @Override
  public int getPosition() {
    return Preferences.getInt(getName() + ".position", getDefaultPosition());
  }

  @Override
  public List<String> getPreferenceKeys() {
    List<String> preferenceKeys = super.getPreferenceKeys();
    preferenceKeys.addAll(Lists.newArrayList("x", "y", "position"));
    return preferenceKeys;
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

}
