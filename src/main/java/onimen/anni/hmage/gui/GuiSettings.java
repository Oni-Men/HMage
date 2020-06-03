package onimen.anni.hmage.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.gui.button.AimGameButtonObject;
import onimen.anni.hmage.gui.button.ButtonObject;
import onimen.anni.hmage.gui.button.CapeSelectButtonObject;
import onimen.anni.hmage.gui.button.GameHistoryButton;
import onimen.anni.hmage.gui.button.ModEnabledButtonObject;
import onimen.anni.hmage.gui.button.ModuleSettingButtonObject;
import onimen.anni.hmage.module.InterfaceModule;

public class GuiSettings extends GuiScroll {

  private List<ButtonObject> buttonObjects = new ArrayList<ButtonObject>();

  private int maxScrollAmount;

  public GuiSettings(Map<String, InterfaceModule> moduleMap) {
    //mod 有効/無効の設定
    buttonObjects.add(new ModEnabledButtonObject());
    buttonObjects.add(new GameHistoryButton());

    //各モジュールの有効/無効の設定
    for (InterfaceModule module : moduleMap.values()) {
      if (module.isShowMenu()) {
        buttonObjects.add(new ModuleSettingButtonObject(module));
      }
    }

    //capeの設定
    buttonObjects.add(new CapeSelectButtonObject());
    //Aim Game
    buttonObjects.add(new AimGameButtonObject());
    //    buttonObjects.add(new DebugButtonObject());

  }

  @Override
  public void initGui() {

    this.buttonList.clear();

    ScaledResolution sr = new ScaledResolution(mc);

    int x = sr.getScaledWidth() / 2 + 30;
    int y = 32 + 24;
    //1文字の半分の高さだけずらす
    y -= this.fontRenderer.FONT_HEIGHT / 2 - 12;

    int width = 100;
    int height = 20;

    int id = 0;
    for (ButtonObject module : this.buttonObjects) {
      GuiButton button = new GuiButton(id, x, y, width, height, module.getButtonText());
      this.addButton(button);
      id++;
      y += 24;
    }

    maxScrollAmount = this.buttonObjects.size() * 24 + 48 - sr.getScaledHeight();
  }

  @Override
  public int getMaxScroll() {
    return maxScrollAmount;
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    int delta = Mouse.getEventDWheel();

    if (delta != 0) {
      if (delta > 1) {
        delta = 1;
      }

      if (delta < -1) {
        delta = -1;
      }

      this.scroll(-delta * 8);
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    ButtonObject buttonObject = buttonObjects.get(button.id);
    buttonObject.actionPerformed(button);
  }

  @Override
  public void onGuiClosed() {
    Preferences.save();
    super.onGuiClosed();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

    this.drawDefaultBackground();
    this.drawCenteredString(this.fontRenderer, "- HMage Settings -", this.width / 2, 16 - amountScroll, 0xffffff);

    int x = sr.getScaledWidth() / 2 - 130;
    int y = 32 - amountScroll;

    y += 12;

    for (ButtonObject buttonObject : this.buttonObjects) {
      this.drawString(this.fontRenderer, buttonObject.getTitle(), x, y, 0xffffff);
      y += 24;
    }

    ButtonObject mouseOveredObject = null;

    GlStateManager.pushMatrix();
    GlStateManager.translate(0D, -amountScroll - 24, 0D);
    for (int i = 0; i < this.buttonList.size(); ++i) {
      //ボタンの描画
      GuiButton button = this.buttonList.get(i);
      button.drawButton(this.mc, mouseX, mouseY + amountScroll + 24, partialTicks);

      //マウスでかぶさっているボタンを取得
      if (button.isMouseOver()) {
        mouseOveredObject = buttonObjects.get(i);
      }
    }
    GlStateManager.popMatrix();

    //tooltip
    if (mouseOveredObject != null) {
      List<String> description = mouseOveredObject.getDescription();
      if (description != null && !description.isEmpty()) {
        this.drawHoveringText(description, mouseX, mouseY);
      }
    }
  }

  @Override
  public boolean doesGuiPauseGame() {
    return false;
  }

  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }

  /**
   * Called when a mouse button is released.
   */
  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
  }

}
