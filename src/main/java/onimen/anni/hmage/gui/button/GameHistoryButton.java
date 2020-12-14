package onimen.anni.hmage.gui.button;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import onimen.anni.hmage.gui.AnniHistoryList;

public class GameHistoryButton implements ButtonObject {

  private final GuiScreen screen;

  public GameHistoryButton(GuiScreen screen) {
    this.screen = screen;
  }

  @Override
  public String getTitle() {
    return "Match History";
  }

  @Override
  public String getButtonText() {
    return getTitle();
  }

  @Override
  public void actionPerformed(GuiButton button) {
    Minecraft.getMinecraft().displayGuiScreen(new AnniHistoryList(screen));
  }

  @Override
  public List<String> getDescription() {
    return Arrays.asList("Annihilation試合履歴を表示します");
  }
}
