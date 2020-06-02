package onimen.anni.hmage.gui;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.GuiScrollingList;
import onimen.anni.hmage.observer.AnniObserverMap;
import onimen.anni.hmage.observer.data.GameInfo;

public class AnniHistoryList extends GuiScreen {

  private AnniHistorySlot modList;
  private GuiScrollingList modInfo;
  private int selected = -1;
  private GameInfo selectedMod;
  private int listWidth;
  private List<GameInfo> mods;

  /**
   * @param mainMenu
   */
  public AnniHistoryList() {
    mods = AnniObserverMap.getInstance().getGameInfoList();
  }

  /**
   * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the window resizes, the buttonList is cleared beforehand.
   */
  @Override
  public void initGui() {
    int slotHeight = 35;
    listWidth = getFontRenderer().getStringWidth("uuuu/MM/dd HH:mm:ss") + 10;
    this.modList = new AnniHistorySlot(this, mods, listWidth, slotHeight);

    this.buttonList
        .add(
            new GuiButton(6, ((modList.getRight() + this.width) / 2) - 100, this.height - 38, I18n.format("gui.done")));

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
    this.modList.drawScreen(mouseX, mouseY, partialTicks);
    if (this.modInfo != null)
      this.modInfo.drawScreen(mouseX, mouseY, partialTicks);

    int left = ((this.width - this.listWidth - 38) / 2) + this.listWidth + 30;
    this.drawCenteredString(this.fontRenderer, "Annihilation History", left, 16, 0xFFFFFF);
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
    if (this.modInfo != null)
      this.modInfo.handleMouseInput(mouseX, mouseY);
    this.modList.handleMouseInput(mouseX, mouseY);
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
    this.selectedMod = (index >= 0 && index <= mods.size()) ? mods.get(selected) : null;

    updateCache();
  }

  public boolean modIndexSelected(int index) {
    return index == selected;
  }

  private void updateCache() {
    modInfo = null;

    if (selectedMod == null)
      return;

    List<String> lines = new ArrayList<String>();

    lines.add(selectedMod.getMapName());
    lines.add(String.format("Date: %s", "2020/1/1 10:20"));
    lines.add("Team: " + selectedMod.getMePlayerData().getTeamColor().getColorName());

    lines.add(null);

    modInfo = new Info(this.width - this.listWidth - 30, lines);
  }

  private class Info extends GuiScrollingList {
    @Nullable
    private ResourceLocation logoPath;
    private Dimension logoDims;
    private List<ITextComponent> lines = null;

    public Info(int width, List<String> lines) {
      super(AnniHistoryList.this.getMinecraftInstance(),
          width,
          AnniHistoryList.this.height,
          32, AnniHistoryList.this.height - 88 + 4,
          AnniHistoryList.this.listWidth + 20, 60,
          AnniHistoryList.this.width,
          AnniHistoryList.this.height);
      this.lines = resizeContent(lines);

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

    private List<ITextComponent> resizeContent(List<String> lines) {
      List<ITextComponent> ret = new ArrayList<ITextComponent>();
      for (String line : lines) {
        if (line == null) {
          ret.add(null);
          continue;
        }

        ITextComponent chat = ForgeHooks.newChatWithLinks(line, false);
        int maxTextLength = this.listWidth - 8;
        if (maxTextLength >= 0) {
          ret.addAll(
              GuiUtilRenderComponents.splitText(chat, maxTextLength, AnniHistoryList.this.fontRenderer, false, true));
        }
      }
      return ret;
    }

    private int getHeaderHeight() {
      int height = 0;
      if (logoPath != null) {
        double scaleX = logoDims.width / 200.0;
        double scaleY = logoDims.height / 65.0;
        double scale = 1.0;
        if (scaleX > 1 || scaleY > 1) {
          scale = 1.0 / Math.max(scaleX, scaleY);
        }
        logoDims.width *= scale;
        logoDims.height *= scale;

        height += logoDims.height;
        height += 10;
      }
      height += (lines.size() * 10);
      if (height < this.bottom - this.top - 8)
        height = this.bottom - this.top - 8;
      return height;
    }

    @Override
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
      int top = relativeY;

      if (logoPath != null) {
        GlStateManager.enableBlend();
        AnniHistoryList.this.mc.renderEngine.bindTexture(logoPath);
        BufferBuilder wr = tess.getBuffer();
        int offset = (this.left + this.listWidth / 2) - (logoDims.width / 2);
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.pos(offset, top + logoDims.height, zLevel).tex(0, 1).endVertex();
        wr.pos(offset + logoDims.width, top + logoDims.height, zLevel).tex(1, 1).endVertex();
        wr.pos(offset + logoDims.width, top, zLevel).tex(1, 0).endVertex();
        wr.pos(offset, top, zLevel).tex(0, 0).endVertex();
        tess.draw();
        GlStateManager.disableBlend();
        top += logoDims.height + 10;
      }

      for (ITextComponent line : lines) {
        if (line != null) {
          GlStateManager.enableBlend();
          AnniHistoryList.this.fontRenderer.drawStringWithShadow(line.getFormattedText(), this.left + 4, top, 0xFFFFFF);
          GlStateManager.disableAlpha();
          GlStateManager.disableBlend();
        }
        top += 10;
      }
    }

    @Override
    protected void clickHeader(int x, int y) {
      int offset = y;
      if (logoPath != null) {
        offset -= logoDims.height + 10;
      }
      if (offset <= 0)
        return;

      int lineIdx = offset / 10;
      if (lineIdx >= lines.size())
        return;

      ITextComponent line = lines.get(lineIdx);
      if (line != null) {
        int k = -4;
        for (ITextComponent part : line) {
          if (!(part instanceof TextComponentString))
            continue;
          k += AnniHistoryList.this.fontRenderer.getStringWidth(((TextComponentString) part).getText());
          if (k >= x) {
            AnniHistoryList.this.handleComponentClick(part);
            break;
          }
        }
      }
    }
  }
}
