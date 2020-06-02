package onimen.anni.hmage.gui;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.observer.data.GameInfo;

public class GuiGameHistory extends GuiScreen {

  private final List<GameInfo> gameInfoList;

  private HistoryList historyList;

  private DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");

  public GuiGameHistory(List<GameInfo> gameInfoList) {
    this.gameInfoList = gameInfoList;
  }

  @Override
  public void initGui() {
    super.initGui();
    this.historyList = new HistoryList(Minecraft.getMinecraft(), gameInfoList);
    this.historyList.registerScrollButtons(7, 8);
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    this.historyList.handleMouseInput();
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    this.historyList.actionPerformed(button);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.drawDefaultBackground();
    this.drawCenteredString(this.fontRenderer, "Game History", this.width / 2, 16, 0xffffff);

    if (this.gameInfoList.isEmpty()) {
      this.drawCenteredString(this.fontRenderer, "No games", this.width / 2, this.height / 2, 0xdddddd);
      return;
    }
    this.historyList.drawScreen(mouseX, mouseY, partialTicks);
  }

  @SideOnly(Side.CLIENT)
  class HistoryList extends GuiSlot {

    private List<GameInfo> gameInfoList;

    public HistoryList(Minecraft mcIn, List<GameInfo> gameInfoList) {
      super(mcIn, GuiGameHistory.this.width, GuiGameHistory.this.height, 32, GuiGameHistory.this.height - 65 + 4, 43);
      this.gameInfoList = gameInfoList;
    }

    @Override
    protected int getSize() {
      return gameInfoList.size();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
      //      this.mc.displayGuiScreen((GuiScreen) null);
      //      this.mc.setIngameFocus();
    }

    @Override
    protected boolean isSelected(int slotIndex) {
      return false;
    }

    @Override
    protected void drawBackground() {
      GuiGameHistory.this.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn,
        float partialTicks) {
      GuiGameHistory gui = GuiGameHistory.this;
      FontRenderer renderer = gui.fontRenderer;
      renderer.setBidiFlag(true);
      GameInfo gameInfo = gameInfoList.get(slotIndex);

      //試合時刻
      ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
      LocalDateTime matchDateTime = LocalDateTime.ofEpochSecond(gameInfo.getGameTimestamp() / 1000, 0, offset);
      gui.drawCenteredString(renderer, datePattern.format(matchDateTime), this.width / 2 - 64, yPos, 0xffffff);
      //マップ
      gui.drawCenteredString(renderer, "Map : " + gameInfo.getMapName(), this.width / 2, yPos, 0xffffff);

      //Team
      gui.drawCenteredString(renderer,
          gameInfo.getMeleeKillCount() + " Melee Kill", this.width / 2, yPos, 0xffffff);
      gui.drawCenteredString(renderer,
          gameInfo.getShotKillCount() + " Shot Kill", this.width / 2, yPos + 8, 0xffffff);
      gui.drawCenteredString(renderer,
          gameInfo.getNexusAttackCount() + " Nexus Damage", this.width / 2, yPos + 16, 0xffffff);
    }
  }
}
