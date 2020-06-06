package onimen.anni.hmage.gui;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiScrollingList;
import onimen.anni.hmage.observer.AnniObserverMap;
import onimen.anni.hmage.observer.data.AnniPlayerData;
import onimen.anni.hmage.observer.data.GameInfo;

public class AnniHistoryList extends GuiScreen {

  private static DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
  private AnniHistorySlot gameList;
  private GuiScrollingList gameInfoList;
  private int selected = -1;
  private GameInfo selectedInfo;
  private int listWidth;
  private List<GameInfo> gameInfos;

  /**
   * @param mainMenu
   */
  public AnniHistoryList() {
    gameInfos = AnniObserverMap.getInstance().getGameInfoList();
    gameInfos.sort((g1, g2) -> (int) (g2.getGameTimestamp() - g1.getGameTimestamp()));

    //一番新しいものを選択する
    if (!gameInfos.isEmpty()) {
      selected = 0;
      selectedInfo = gameInfos.get(selected);
    }
  }

  /**
   * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the window resizes, the buttonList is cleared beforehand.
   */
  @Override
  public void initGui() {
    int slotHeight = 35;
    listWidth = getFontRenderer().getStringWidth("uuuu/MM/dd HH:mm:ss") + 10;
    this.gameList = new AnniHistorySlot(this, gameInfos, listWidth, slotHeight);

    this.buttonList
        .add(new GuiButton(6, this.width / 2 - 100, this.height - 38, I18n.format("gui.done")));

    updateCache();
  }

  /**
   * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
   */
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    switch (button.id) {
    case 6: {
      this.mc.displayGuiScreen((GuiScreen) null);
      this.mc.setIngameFocus();
      return;
    }
    }
    super.actionPerformed(button);
  }

  public int drawLine(String line, int offset, int shifty) {
    this.fontRenderer.drawString(line, offset, shifty, 0xd7edea);
    return shifty + 10;
  }

  /**
   * Draws the screen and all the components in it.
   */
  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.gameList.drawScreen(mouseX, mouseY, partialTicks);
    if (this.gameInfoList != null)
      this.gameInfoList.drawScreen(mouseX, mouseY, partialTicks);

    this.drawCenteredString(this.fontRenderer, ChatFormatting.UNDERLINE + "Annihilation Match History", this.width / 2,
        16,
        0xFFFFFF);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  /**
   * Handles mouse input.
   */
  @Override
  public void handleMouseInput() throws IOException {
    int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
    int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

    super.handleMouseInput();
    if (this.gameInfoList != null)
      this.gameInfoList.handleMouseInput(mouseX, mouseY);
    this.gameList.handleMouseInput(mouseX, mouseY);
  }

  Minecraft getMinecraftInstance() {
    return mc;
  }

  FontRenderer getFontRenderer() {
    return fontRenderer;
  }

  public void selectModIndex(int index) {
    if (index == this.selected)
      return;
    this.selected = index;
    this.selectedInfo = (index >= 0 && index <= gameInfos.size()) ? gameInfos.get(selected) : null;

    updateCache();
  }

  public boolean modIndexSelected(int index) {
    return index == selected;
  }

  private void updateCache() {
    gameInfoList = null;

    if (selectedInfo == null)
      return;

    gameInfoList = new Info(this.width - this.listWidth - 30, selectedInfo);
  }

  private class Info extends GuiScrollingList {
    private GameInfo gameInfo;

    public Info(int width, GameInfo gameInfo) {
      super(AnniHistoryList.this.getMinecraftInstance(),
          width,
          AnniHistoryList.this.height,
          32, AnniHistoryList.this.height - 50,
          AnniHistoryList.this.listWidth + 20, 60,
          AnniHistoryList.this.width,
          AnniHistoryList.this.height);
      this.gameInfo = gameInfo;

      this.setHeaderInfo(true, getHeaderHeight());
    }

    @Override
    protected int getSize() {
      return 0;
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
    }

    @Override
    protected boolean isSelected(int index) {
      return false;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
    }

    private int getHeaderHeight() {
      return 292;
    }

    @Override
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
      int top = relativeY;
      int left = this.left + 20;
      int color = 0xFFFFFF;
      int rank = 1;

      FontRenderer fr = AnniHistoryList.this.fontRenderer;

      String mapName = gameInfo.getMapName();
      if (mapName == null || mapName.isEmpty()) {
        mapName = "Voting";
      }
      String mapAndTeamText = mapName + " - " + gameInfo.getMeTeamColor().getColoredName();
      //MAP NAME AND TEAM COLOR
      fr.drawStringWithShadow(mapAndTeamText, left, top, color);
      top += 12;

      Instant instant = Instant.ofEpochMilli(gameInfo.getGameTimestamp());
      LocalDateTime matchDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
      fr.drawStringWithShadow(datePattern.format(matchDateTime), left, top, 0xCCCCCC);
      top += 20;

      //SECTION START - PLAYER STATS
      fr.drawStringWithShadow(ChatFormatting.UNDERLINE + "Your Stats", left, top, color);
      top += 12;
      left += 8;
      fr.drawStringWithShadow(gameInfo.getMeleeKillCount() + " Melee kills", left, top, color);
      top += 10;
      fr.drawStringWithShadow(gameInfo.getShotKillCount() + " Shot kills", left, top, color);
      top += 10;
      fr.drawStringWithShadow(gameInfo.getNexusAttackCount() + " Nexus damage", left, top, color);
      top += 10;
      fr.drawStringWithShadow(gameInfo.getMePlayerData().getDeathCount() + " Deaths", left, top, color);
      top += 10;
      left -= 8;
      //SECTION END - PLAYER STATS

      top += 10;

      Collection<AnniPlayerData> statsMap = Lists.newArrayList(gameInfo.getOtherPlayerStatsMap().values());
      statsMap.add(gameInfo.getMePlayerData());

      //SECTION START - TOP OF THE GAME
      fr.drawStringWithShadow(ChatFormatting.UNDERLINE + "Top of the Game", left, top, color);
      top += 20;
      left += 8;

      fr.drawStringWithShadow("Kill Count", left, top, color);
      left += 8;
      top += 12;
      List<AnniPlayerData> topPlayerKiller = getTopPlayerKiller(statsMap, 5);
      for (AnniPlayerData data : topPlayerKiller) {
        fr.drawStringWithShadow(
            String.format("%d. %s%s", rank, data.getTeamColor().getColorCode(), data.getPlayerName()), left, top,
            color);
        fr.drawStringWithShadow(data.getTotalKillCount() + " kills", left + 100, top, color);
        top += 10;
        rank++;
      }
      if (topPlayerKiller.isEmpty()) {
        fr.drawStringWithShadow("No Result", left, top, color);
        top += 10;
      }
      left -= 8;
      top += 10;
      rank = 1;

      fr.drawStringWithShadow("Nexus Damage", left, top, color);
      left += 8;
      top += 12;
      List<AnniPlayerData> topNexusDamager = getTopNexusDamager(statsMap, 5);
      for (AnniPlayerData data : topNexusDamager) {
        fr.drawStringWithShadow(
            String.format("%d. %s%s", rank, data.getTeamColor().getColorCode(), data.getPlayerName()), left, top,
            color);
        fr.drawStringWithShadow(data.getNexusDamageCount() + " damage", left + 100, top, color);
        top += 10;
        rank++;
      }
      if (topNexusDamager.isEmpty()) {
        fr.drawStringWithShadow("No Result", left, top, color);
        top += 10;
      }

      left -= 8;
      //SECTION END - TOP OF THE GAME

    }

    private List<AnniPlayerData> getTopPlayerKiller(Collection<AnniPlayerData> data, long limit) {
      return data.stream()
          .sorted((a, b) -> b.getTotalKillCount() - a.getTotalKillCount())
          .filter(a -> a.getTotalKillCount() != 0)
          .limit(limit)
          .collect(Collectors.toList());
    }

    private List<AnniPlayerData> getTopNexusDamager(Collection<AnniPlayerData> data, long limit) {
      return data.stream()
          .sorted((a, b) -> b.getNexusDamageCount() - a.getNexusDamageCount())
          .filter(a -> a.getNexusDamageCount() != 0)
          .limit(limit)
          .collect(Collectors.toList());
    }

    @Override
    protected void clickHeader(int x, int y) {
    }
  }
}
