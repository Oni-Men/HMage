package onimen.anni.hmage.gui.button;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.gui.GuiGameHistory;

public class GameHistoryButton implements ButtonObject {

  public GameHistoryButton() {
  }

  @Override
  public String getTitle() {
    return "Match History";
  }

  @Override
  public String getButtonText() {
    return "Open";
  }

  @Override
  public void actionPerformed(GuiButton button) {
    Minecraft.getMinecraft().displayGuiScreen(new GuiGameHistory(HMage.anniObserverMap.getGameInfoList()));
  }

  @Override
  public List<String> getDescription() {
    return Arrays.asList("試合履歴を表示します");
  }
}
