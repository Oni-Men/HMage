package onimen.anni.hmage.gui;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lwjgl.input.Mouse;

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
    private int rankingHashCode = 0;
    private int rankingValueOffset = 0;

    private int killRankingLimit = 10;
    private int nexusRankingLimit = 5;

    int color = 0xFFFFFF;

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
      int height = 0;

      height += 32; //Map name + date + space

      height += 62; //Player stats

      height += 22; //Top of the Game

      height += 22; //Kill Count

      int killRankingSize = gameInfo.getTotalKillRanking(killRankingLimit).size();
      height += (killRankingSize == 0 ? 10 : killRankingSize * 10);

      height += 22;

      int nexusRankingSize = gameInfo.getNexusRanking(nexusRankingLimit).size();
      height += (nexusRankingSize == 0 ? 10 : nexusRankingSize * 10);

      if (height < this.bottom - this.top - 8)
        height = this.bottom - this.top - 8;

      return height;
    }

    @Override
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
      int top = relativeY;
      int left = this.left + 20;

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
      top += 20;
      left -= 8;
      //SECTION END - PLAYER STATS

      List<AnniPlayerData> killRanking = gameInfo.getTotalKillRanking(killRankingLimit);
      List<AnniPlayerData> nexusRanking = gameInfo.getNexusRanking(nexusRankingLimit);
      List<AnniPlayerData> meleeKillRanking = gameInfo.getMeleeKillRanking(10);
      List<AnniPlayerData> shotKillRanking = gameInfo.getShotKillRanking(5);

      List<AnniPlayerData> allCurrentRankers = Stream.of(killRanking, nexusRanking, meleeKillRanking, shotKillRanking)
          .flatMap(Collection::stream)
          .collect(Collectors.toList());

      if (rankingHashCode != allCurrentRankers.hashCode()) {
        rankingValueOffset = getMaxPlayerNameWidth(allCurrentRankers);
        rankingValueOffset += 8;
        rankingHashCode = allCurrentRankers.hashCode();
      }

      //SECTION START - TOP OF THE GAME
      fr.drawStringWithShadow(ChatFormatting.UNDERLINE + "Top of the Game", left, top, color);
      top += 22;
      left += 8;

      //TODO GUIスケールAUTOではみ出すバグ直す
      int underMeleeRanking = drawRanking("Melee Kill", meleeKillRanking, fr, top, left + 200, color,
          p -> p.getMeleeCount() + " Kills");
      drawRanking("Shot Kill", shotKillRanking, fr, underMeleeRanking, left + 200, color,
          p -> p.getBowCount() + " Kills");

      top = drawRanking("Total Kill", killRanking, fr, top, left, color, p -> p.getTotalKillCount() + " Kills");
      top = drawRanking("Nexus Damage", nexusRanking, fr, top, left, color, p -> p.getNexusDamageCount() + " Damage");

      left -= 8;
      //SECTION END - TOP OF THE GAME

      this.setHeaderInfo(true, this.getHeaderHeight());
    }

    private int drawRanking(String title, List<AnniPlayerData> ranking, FontRenderer fr, int top,
        int left, int color, Function<AnniPlayerData, String> value) {
      fr.drawStringWithShadow(title, left, top, color);
      left += 8;
      top += 12;

      if (ranking.isEmpty()) {
        fr.drawStringWithShadow("No Result", left, top, color);
        top += 10;
        return top;
      }

      String rankText;
      int rank = 1;
      for (AnniPlayerData data : ranking) {
        rankText = rank + ".";
        fr.drawStringWithShadow(rankText, left - fr.getStringWidth(rankText), top, color);
        fr.drawStringWithShadow(
            String.format(" %s%s", data.getTeamColor().getColorCode(), data.getPlayerName()), left, top,
            color);
        fr.drawStringWithShadow(value.apply(data), left + rankingValueOffset, top, color);
        top += 10;
        rank++;
      }
      top += 10;
      return top;
    }

    private int getMaxPlayerNameWidth(Collection<AnniPlayerData> datas) {
      int width = 0;
      for (AnniPlayerData data : datas) {
        int nameWidth = AnniHistoryList.this.fontRenderer.getStringWidth(data.getPlayerName());
        if (nameWidth > width) {
          width = nameWidth;
        }
      }
      return width;
    }

    @Override
    protected void clickHeader(int x, int y) {
    }
  }
}
