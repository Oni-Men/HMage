package onimen.anni.hmage.gui.button;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import onimen.anni.hmage.gui.AimGameGui;

public class AimGameButtonObject implements ButtonObject {

  @Override
  public String getTitle() {
    return "Aim Game";
  }

  @Override
  public String getButtonText() {
    return "Game Start";
  }

  @Override
  public void actionPerformed(GuiButton button) {
    Minecraft.getMinecraft().displayGuiScreen(new AimGameGui());
  }

  @Override
  public List<String> getDescription() {
    return Arrays.asList("AIMを鍛えるゲームを開始する。");
  }

}
