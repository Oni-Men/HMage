package onimen.anni.hmage.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import onimen.anni.hmage.gui.CapeSetting;

public class CapeSelectButtonObject implements ButtonObject {

  @Override
  public String getTitle() {
    return "Cape";
  }

  @Override
  public String getButtonText() {
    return "Cape Select";
  }

  @Override
  public void actionPerformed(GuiButton button) {
    Minecraft.getMinecraft().displayGuiScreen(new CapeSetting());
  }

}
