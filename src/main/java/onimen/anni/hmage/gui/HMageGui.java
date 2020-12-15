package onimen.anni.hmage.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.gui.button.ButtonObject;

public abstract class HMageGui extends GuiScreen {

  protected List<ButtonObject> buttonObjects = new ArrayList<ButtonObject>();
  protected final GuiScreen parent;

  protected String title = "HMage GUI";
  protected int rows = 2;
  protected boolean buttonTitleVisible = false;

  public HMageGui(@Nullable GuiScreen parent) {
    this.parent = parent;
  }

  @Override
  public void initGui() {
    this.buttonList.clear();

    int buttonWidth = 150;
    int buttonHeight = 20;
    int length = this.buttonObjects.size() / rows;
    int x = width / 2 - (buttonWidth * rows / 2);
    int y = 64;

    for (int i = 0; i < rows; i++) {
      y = 64;
      int fromIndex = i * length;
      int toIndex = Math.min((i + 1) * length, this.buttonObjects.size());
      List<ButtonObject> sub = this.buttonObjects.subList(fromIndex, toIndex);

      for (ButtonObject obj : sub) {
        int buttonX = buttonTitleVisible ? x + buttonWidth / 2 : x;
        int buttonW = buttonTitleVisible ? buttonWidth / 2 : buttonWidth;
        GuiButton button = new GuiButton(obj.hashCode(), buttonX, y, buttonW, buttonHeight, obj.getButtonText());
        this.addButton(button);
        y += 24;
      }
      x += buttonWidth + 5;
    }

    this.addButton(new GuiButton(-1, width / 2 - 100, y + 24, I18n.format("gui.done")));
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.drawDefaultBackground();
    this.drawCenteredString(this.fontRenderer, "- " + title + " -", this.width / 2, 16, 0xffffff);

    ButtonObject mouseOveredObject = null;

    for (int i = 0; i < this.buttonList.size(); ++i) {
      //ボタンの描画
      GuiButton button = this.buttonList.get(i);
      ButtonObject object = this.getObjectFromGuiButton(button);
      button.drawButton(this.mc, mouseX, mouseY, partialTicks);

      if (object != null && this.buttonTitleVisible) {
        this.drawString(fontRenderer, object.getTitle(), button.x - button.width, (button.y + button.height / 2 - 4),
            0xFFFFFF);
      }

      //マウスでかぶさっているボタンを取得
      if (button.isMouseOver()) {
        mouseOveredObject = object;
      }
    }

    //tooltip
    if (mouseOveredObject != null) {
      List<String> description = mouseOveredObject.getDescription();
      if (description != null && !description.isEmpty()) {
        this.drawHoveringText(description, mouseX, mouseY);
      }
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    if (button.id == -1) {
      mc.displayGuiScreen(this.parent);
      return;
    }
    ButtonObject mouseOveredObject = getObjectFromGuiButton(button);
    if (mouseOveredObject != null)
      mouseOveredObject.actionPerformed(button);
  }

  @Nullable
  private ButtonObject getObjectFromGuiButton(GuiButton button) {
    return buttonObjects
        .stream()
        .filter(b -> b.hashCode() == button.id)
        .findFirst()
        .orElse(null);
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
  }

  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
  }

  @Override
  public void handleKeyboardInput() throws IOException {
    super.handleKeyboardInput();
  }
}
