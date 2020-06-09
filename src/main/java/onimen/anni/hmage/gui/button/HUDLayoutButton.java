package onimen.anni.hmage.gui.button;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.gui.GuiHUDLayout;

public class HUDLayoutButton implements ButtonObject {

  @Override
  public String getTitle() {
    return I18n.format("hmage.gui.button.hudlayoutbutton.title");
  }

  @Override
  public String getButtonText() {
    return I18n.format("hmage.gui.button.hudlayoutbutton.buttontext");
  }

  @Override
  public void actionPerformed(GuiButton button) {
    Minecraft.getMinecraft().displayGuiScreen(new GuiHUDLayout(HMage.getHUDMap()));
  }

  @Override
  public List<String> getDescription() {
    return null;
  }

}
