package onimen.anni.hmage.gui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiFontChoose extends GuiScroll {

  public static String[] getSystemAvailableFonts() {
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
  }

  public static Font getFontByName(String name) {
    Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    if (name != null) {
      for (Font font : fonts) {
        if (font.getFamily().equals(name)) { return font; }
      }
    }
    return new Font("System", Font.PLAIN, 12);
  }

  @Nullable
  private GuiScreen parent;

  @Nullable
  private Consumer<Font> callback;

  private int currentFontIndex = -1;

  private String chosenFontName = null;

  private final String[] availableFontNames;

  private GuiButton done;

  public GuiFontChoose(GuiScreen parent, Consumer<Font> callback) {
    this.parent = parent;
    this.callback = callback;
    this.availableFontNames = getSystemAvailableFonts();
  }

  @Override
  public void initGui() {
    super.initGui();
    done = new GuiButton(-1, this.width / 2 - 100, this.height - 36, I18n.format("gui.done"));
    addButton(done);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.drawCenteredString(fontRenderer, "Customize Font", width / 2, 24, 0xFFFFFF);

    if (availableFontNames == null)
      return;

    int startIndex = Math.max(0, (amountScroll) / 12);
    int endIndex = Math.min(availableFontNames.length, (amountScroll + height - 96) / 12);

    int y = 48;
    for (int i = startIndex; i < endIndex; i++) {
      String text = availableFontNames[i];
      if (text.equals(chosenFontName)) {
        text = ">" + text;
      }
      if (i == currentFontIndex) {
        text = ChatFormatting.UNDERLINE + text;
      }
      this.drawString(fontRenderer, text, width / 2 - 100, y - amountScroll % 12, 0xFFFFFF);
      y += 12;
    }

  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    this.callback.accept(getFontByName(chosenFontName));
  }

  @Override
  public int getMaxScroll() {
    return 12 * availableFontNames.length - height + 96;
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);
    switch (button.id) {
    case -1:
      mc.displayGuiScreen(this.parent);
      break;
    default:
      break;
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    if (done.mousePressed(mc, mouseX, mouseY)) {
      done.playPressSound(this.mc.getSoundHandler());
      this.actionPerformed(done);
    }

    int index = (mouseY + amountScroll - 48) / 12;
    if (index >= 0 && index < availableFontNames.length) {
      if (mouseX > width / 2 - 100 && mouseX < width / 2) {
        chosenFontName = availableFontNames[index];
      }
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    int index = (mouseY + amountScroll - 48) / 12;
    if (index >= 0 && index < availableFontNames.length) {
      if (mouseX > width / 2 - 100 && mouseX < width / 2) {
        currentFontIndex = index;
      }
    }
  }

  @Override
  public void handleInput() throws IOException {
    super.handleInput();
  }

  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    if (mouseY > 48 && mouseY < height - 96) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
  }

  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
  }

}
