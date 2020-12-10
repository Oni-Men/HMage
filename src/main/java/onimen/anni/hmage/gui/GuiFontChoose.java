package onimen.anni.hmage.gui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiFontChoose extends GuiScroll {

  private static final int TOP = 64, BOT = 96;

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

  private GuiTextField fontTextField;

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

    fontTextField = new GuiTextField(2, fontRenderer, width / 2 - 100, 24, 200, 20);

  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.drawCenteredString(fontRenderer, "Customize Font", width / 2, 24, 0xFFFFFF);

    if (availableFontNames == null)
      return;

    int startIndex = Math.max(0, (amountScroll) / 12);
    int endIndex = Math.min(availableFontNames.length, (amountScroll + height - BOT) / 12);

    int y = TOP;
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

    fontTextField.drawTextBox();
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    this.callback.accept(getFontByName(chosenFontName));
  }

  @Override
  public int getMaxScroll() {
    return 12 * availableFontNames.length - height + BOT;
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
    fontTextField.mouseClicked(mouseX, mouseY, mouseButton);
    int index = (mouseY + amountScroll - TOP) / 12;
    if (index >= 0 && index < availableFontNames.length) {
      if (mouseX > width / 2 - 100 && mouseX < width / 2) {
        chosenFontName = availableFontNames[index];
      }
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    int index = (mouseY + amountScroll - TOP) / 12;
    if (index >= 0 && index < availableFontNames.length) {
      if (mouseX > width / 2 - 100 && mouseX < width / 2) {
        currentFontIndex = index;
      }
    }
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (this.fontTextField.textboxKeyTyped(typedChar, keyCode)) {
      String[] split = fontTextField.getText().split(",");
      boolean backSpace = keyCode == Keyboard.KEY_BACK;
      boolean typedComma = typedChar == ',';
      if (!backSpace && !typedComma && split.length != 0) {
        this.findAndScroll(split[split.length - 1]);
      }
    } else {
      super.keyTyped(typedChar, keyCode);
    }
  }

  @Override
  public void handleInput() throws IOException {
    super.handleInput();
  }

  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    if (mouseY > TOP && mouseY < height - BOT) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
  }

  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
  }

  private void findAndScroll(String text) {
    if (text.isEmpty())
      return;

    text = text.toLowerCase();

    int min = 0, mid, max = availableFontNames.length;
    int find = -1;
    while (max >= min) {
      mid = min + (max - min) / 2;
      if (mid < 0 || mid >= availableFontNames.length) {
        break;
      }

      String fontName = availableFontNames[mid].toLowerCase();

      if (fontName.charAt(0) > text.charAt(0)) {
        max = mid - 1;
        continue;
      }
      if (fontName.charAt(0) < text.charAt(0)) {
        min = mid + 1;
        continue;
      }
      find = mid;
      break;
    }

    for (int i = find; i < availableFontNames.length; i++) {
      if (availableFontNames[i].toLowerCase().startsWith(text)) {
        find = i;
        break;
      }
    }

    if (find != -1) {
      amountScroll = 12 * find - TOP;
    }
  }

}
