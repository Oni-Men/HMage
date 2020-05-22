package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import onimen.anni.hmage.module.InterfaceModule;

public interface InterfaceHUD extends InterfaceModule {

  public void setX(int value);

  public int getX();

  public void setY(int value);

  public int getY();

  public void setPosition(int position);

  public int getPosition();

  public int getDefaultPosition();

  public void drawItem(Minecraft mc);

}
