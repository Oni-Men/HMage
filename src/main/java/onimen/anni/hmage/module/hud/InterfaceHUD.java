package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import onimen.anni.hmage.module.InterfaceModule;
import onimen.anni.hmage.util.PositionHelper.Position;

public interface InterfaceHUD extends InterfaceModule {

  public void setX(int value);

  public int getX();

  public void setY(int value);

  public int getY();

  public void setPositionFlag(int position);

  public int getPositionFlag();

  public int getDefaultPosition();

  public int getDefaultX();

  public int getDefaultY();

  public void drawItem(Minecraft mc);

  public int getWidth();

  public int getHeight();

  public int getComputedX(ScaledResolution sr);

  public int getComputedY(ScaledResolution sr);

  public Position getPosition();

  public boolean isHorizontal();
}
