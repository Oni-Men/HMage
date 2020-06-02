package onimen.anni.hmage.gui;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import onimen.anni.hmage.observer.data.GameInfo;

public class AnniHistorySlot extends GuiScrollingList {

  private DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");

  private AnniHistoryList parent;
  private List<GameInfo> mods;

  public AnniHistorySlot(AnniHistoryList parent, List<GameInfo> mods, int listWidth, int slotHeight) {
    super(parent.getMinecraftInstance(), listWidth, parent.height, 32, parent.height - 88 + 4, 10, slotHeight,
        parent.width, parent.height);
    this.parent = parent;
    this.mods = mods;
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
    return mods.size();
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
    return mods;
  }

  @Override
  protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
    GameInfo mc = mods.get(idx);
    FontRenderer font = this.parent.getFontRenderer();

    ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
    LocalDateTime matchDateTime = LocalDateTime.ofEpochSecond(mc.getGameTimestamp() / 1000, 0, offset);

    font.drawString(font.trimStringToWidth(datePattern.format(matchDateTime), listWidth), this.left + 3,
        top, 0xFFFFFF);
    font.drawString(font.trimStringToWidth("Map: " + mc.getMapName(), listWidth), this.left + 3,
        top + 12, 0xCCCCCC);
    font.drawString(
        font.trimStringToWidth("Color: " + mc.getMePlayerData().getTeamColor().getColorName(), listWidth - 10),
        this.left + 3, top + 22, 0xCCCCCC);
  }
}