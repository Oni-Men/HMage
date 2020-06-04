package onimen.anni.hmage.gui;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import onimen.anni.hmage.observer.AnniObserver;
import onimen.anni.hmage.observer.AnniObserverMap;
import onimen.anni.hmage.observer.data.GameInfo;
import onimen.anni.hmage.util.DateUtils;

public class AnniHistorySlot extends GuiScrollingList {

  private AnniHistoryList parent;
  private List<GameInfo> gameInfos;

  public AnniHistorySlot(AnniHistoryList parent, List<GameInfo> gameInfos, int listWidth, int slotHeight) {
    super(parent.getMinecraftInstance(), listWidth, parent.height, 32, parent.height - 88 + 4, 10, slotHeight,
        parent.width, parent.height);
    this.parent = parent;
    this.gameInfos = gameInfos;
  }

  public int getBottom() {
    return bottom;
  }

  public int getListWidth() {
    return this.listWidth;
  }

  public int getRight() {
    return right;
  }

  public int setSelectedIndex(int selectedIndex) {
    this.selectedIndex = selectedIndex;
    return this.selectedIndex;
  }

  @Override
  protected int getSize() {
    return gameInfos.size();
  }

  @Override
  protected void elementClicked(int index, boolean doubleClick) {
    this.parent.selectModIndex(index);
  }

  @Override
  protected boolean isSelected(int index) {
    return this.parent.modIndexSelected(index);
  }

  @Override
  protected void drawBackground() {
    this.parent.drawDefaultBackground();
  }

  @Override
  protected int getContentHeight() {
    return (this.getSize()) * 35 + 1;
  }

  List<GameInfo> getMods() {
    return gameInfos;
  }

  @Override
  protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
    GameInfo gameInfo = gameInfos.get(idx);
    FontRenderer font = this.parent.getFontRenderer();

    String mapName = gameInfo.getMapName();
    if (mapName == null || mapName.isEmpty()) {
      mapName = "Voting";
    }
    font.drawString(font.trimStringToWidth("Map: " + mapName, listWidth), this.left + 3,
        top, 0xCCCCCC);
    font.drawString(
        font.trimStringToWidth(gameInfo.getMeTeamColor().getColoredName(), listWidth),
        this.left + 3, top + 12, 0xFFFFFF);
    AnniObserver anniObserver = AnniObserverMap.getInstance().getAnniObserver();
    if (anniObserver == null || !anniObserver.getGameInfo().equals(gameInfo)) {
      font.drawString(font.trimStringToWidth(DateUtils.getDateString(gameInfo.getGameTimestamp()), listWidth),
          this.left + 3, top + 22, 0xFFFFFF);
    } else {
      font.drawString(font.trimStringToWidth(ChatFormatting.GOLD + "Playing Now", listWidth), this.left + 3, top + 22,
          0xFFFFFF);
    }
  }
}