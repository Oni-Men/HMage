package onimen.anni.hmage.gui.button;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import onimen.anni.hmage.gui.AnniHistoryList;

public class DebugButtonObject implements ButtonObject {

  @Override
  public String getTitle() {
    return "";
  }

  @Override
  public String getButtonText() {
    return "debug";
  }

  @Override
  public void actionPerformed(GuiButton button) {
    Minecraft.getMinecraft().displayGuiScreen(new AnniHistoryList());
  }

  @Override
  public List<String> getDescription() {
    return null;
  }
}
