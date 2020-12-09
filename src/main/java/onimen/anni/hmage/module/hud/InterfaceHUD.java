package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import onimen.anni.hmage.module.hud.layout.Layout;
import onimen.anni.hmage.module.normal.InterfaceModule;

public interface InterfaceHUD extends InterfaceModule {

  public void setX(int value);

  public int getX();

  public void setY(int value);

  public int getY();

  public void setLayout(Layout layout);

  public Layout getLayout();

  public Layout getDefaultLayout();

  public int getDefaultX();

  public int getDefaultY();

  public float getDefaultScale();

  public void setScale(float value);

  public float getScale();

  public void drawItem(Minecraft mc);

  public void drawItem(Minecraft mc, boolean layoutMode);

  public int getWidth();

  public int getHeight();

  public int getComputedX(ScaledResolution sr);

  public int getComputedY(ScaledResolution sr);

  public boolean isHorizontal();

  public boolean isInside(ScaledResolution sr, int x, int y);
}
