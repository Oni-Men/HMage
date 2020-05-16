package onimen.anni.hmage.gui.hud;

import net.minecraft.client.Minecraft;

public interface InterfaceHUD {

  public boolean isEnabled();

  public String getPrefKey();

  public void drawItem(Minecraft mc);

}
