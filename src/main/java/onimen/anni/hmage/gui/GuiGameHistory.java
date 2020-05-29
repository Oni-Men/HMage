package onimen.anni.hmage.gui;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import onimen.anni.hmage.observer.GameInfo;

public class GuiGameHistory extends GuiScreen {

  private final List<GameInfo> gameInfoList;

  public GuiGameHistory(List<GameInfo> gameInfoList) {
    this.gameInfoList = gameInfoList;
  }

  @Override
  public void initGui() {
    super.initGui();

  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.drawDefaultBackground();
    this.drawCenteredString(this.fontRenderer, "Game History", this.width / 2, 16, 0xffffff);

    if (this.gameInfoList.isEmpty()) {
      this.drawCenteredString(this.fontRenderer, "No games", this.width / 2, this.height / 2, 0xdddddd);
    } else {
      int x = this.width / 2;
      int y = 32;
      for (GameInfo gameInfo : gameInfoList) {
        this.drawString(this.fontRenderer, gameInfo.getMapName(), x - 64, y, 0xffffff);
        this.drawString(this.fontRenderer, gameInfo.getMeleeKillCount() + " Melee Kill", x, y, 0xffffff);
        this.drawString(this.fontRenderer, gameInfo.getShotKillCount() + " Shot Kill", x, y + 8, 0xffffff);
        this.drawString(this.fontRenderer, gameInfo.getNexusAttackCount() + " Nexus Damage", x, y + 16, 0xffffff);

        y += 32;
      }
    }
  }

}
