package onimen.anni.hmage.gui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    return null;
  }

  @Nullable
  private GuiScreen parent;

  @Nullable
  private Consumer<List<Font>> callback;

  private int currentFontIndex = -1;

  private List<String> chosenFontNames = null;

  private final String[] availableFontNames;

  private GuiButton done;

  private GuiTextField fontTextField;

  private boolean changed = false;

  public GuiFontChoose(GuiScreen parent, List<String> initialFontNames, Consumer<List<Font>> callback) {
    this.parent = parent;
    this.callback = callback;
    this.availableFontNames = getSystemAvailableFonts();
    this.chosenFontNames = new ArrayList<>();
    if (initialFontNames != null) {
      this.chosenFontNames.addAll(initialFontNames);
    }
  }

  @Override
  public void initGui() {
    super.initGui();
    done = new GuiButton(-1, this.width / 2 - 100, this.height - 36, I18n.format("gui.done"));
    addButton(done);

    Keyboard.enableRepeatEvents(true);

    fontTextField = new GuiTextField(2, fontRenderer, width / 2 - 100, 36, 200, 20);
    fontTextField.setEnableBackgroundDrawing(true);
    fontTextField.setMaxStringLength(32);
    fontTextField.setVisible(true);
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
      if (chosenFontNames.contains(text)) {
        text = ChatFormatting.LIGHT_PURPLE + ">" + text;
      }
      if (i == currentFontIndex) {
        text = ChatFormatting.UNDERLINE + text;
      }
      this.drawString(fontRenderer, text, width / 2 - 100, y - amountScroll % 12, 0xFFFFFF);
      y += 12;
    }

    y = TOP;
    for (String fontName : chosenFontNames) {
      this.drawString(fontRenderer, ChatFormatting.LIGHT_PURPLE + fontName, width / 2 + 10, y, 0xFFFFFF);
      y += 12;
    }

    fontTextField.drawTextBox();
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    Keyboard.enableRepeatEvents(false);
    if (changed) {
      this.callback.accept(
          chosenFontNames.stream()
              .map(n -> getFontByName(n))
              .filter(f -> f != null)
              .collect(Collectors.toList()));
    }
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
    if (fontTextField.mouseClicked(mouseX, mouseY, mouseButton))
      return;

    if (mouseY > TOP && mouseY < height - BOT + TOP) {
      if (mouseX > width / 2 - 100 && mouseX < width / 2) {
        int index = (mouseY + amountScroll - TOP) / 12;
        if (index >= 0 && index < availableFontNames.length) {
          String fontName = availableFontNames[index];
          if (!chosenFontNames.remove(fontName)) {
            chosenFontNames.add(fontName);
            changed = true;
          }
        }
      } else if (mouseX > width / 2 && mouseX < width / 2 + 100) {
        int index = (mouseY - TOP) / 12;
        if (index >= 0 && index < chosenFontNames.size()) {
          chosenFontNames.remove(index);
          changed = true;
        }
      }
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    int index = (mouseY + amountScroll - TOP) / 12;
    if (index >= 0 && index < availableFontNames.length) {
      if (mouseX > width / 2 - 100 && mouseX < width / 2 && mouseY > TOP && mouseY < height - BOT + TOP) {
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
      if (!backSpace && !isCtrlKeyDown() && !typedComma && split.length != 0) {
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
    int find = find(text, (s, t) -> s.startsWith(t));
    if (find != -1) {
      amountScroll = 12 * find;
    }
  }

  private int find(String text, BiPredicate<String, String> predicate) {
    if (text.isEmpty())
      return -1;

    text = text.toLowerCase();

    int min = 0, mid, max = availableFontNames.length;
    int idxFirstChar = -1;
    while (max >= min) {
      mid = min + (max - min) / 2;
      if (mid < 0 || mid >= availableFontNames.length) {
        break;
      }

      String fontName = availableFontNames[mid].toLowerCase();
      if (fontName.isEmpty())
        continue;

      if (fontName.charAt(0) > text.charAt(0)) {
        max = mid - 1;
        continue;
      }
      if (fontName.charAt(0) < text.charAt(0)) {
        min = mid + 1;
        continue;
      }
      idxFirstChar = mid;
      break;
    }

    if (idxFirstChar == -1)
      return idxFirstChar;

    int foundAt = -1;

    for (int i = idxFirstChar; i < availableFontNames.length; i++) {
      if (predicate.test(availableFontNames[i].toLowerCase(), text)) {
        foundAt = i;
        break;
      }
    }

    return foundAt;
  }
}
