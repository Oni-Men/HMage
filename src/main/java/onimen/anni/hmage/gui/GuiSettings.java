package onimen.anni.hmage.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.gui.button.AimGameButtonObject;
import onimen.anni.hmage.gui.button.ButtonObject;
import onimen.anni.hmage.gui.button.CapeSelectButtonObject;
import onimen.anni.hmage.gui.button.GameHistoryButton;
import onimen.anni.hmage.gui.button.HUDLayoutButton;
import onimen.anni.hmage.gui.button.HurtingArmorColorButton;
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

    buttonObjects.add(new HUDLayoutButton());

    //各モジュールの有効/無効の設定
    for (InterfaceModule module : moduleMap.values()) {
      if (module.isShowMenu()) {
        buttonObjects.add(new ModuleSettingButtonObject(module, this));
      }
    }

    //capeの設定
    buttonObjects.add(new CapeSelectButtonObject());
    //Aim Game
    buttonObjects.add(new AimGameButtonObject());
    buttonObjects.add(new HurtingArmorColorButton());

    //    buttonObjects.add(new DebugButtonObject());
  }

  @Override
  public void initGui() {

    this.buttonList.clear();

    ScaledResolution sr = new ScaledResolution(mc);

    int width = 150;
    int height = 20;
    int x = sr.getScaledWidth() / 2 - 160;
    int y = 64;

    int splitIndex = this.buttonObjects.size() / 2;
    List<ButtonObject> left = this.buttonObjects.subList(0, splitIndex);
    List<ButtonObject> right = this.buttonObjects.subList(splitIndex, this.buttonObjects.size());

    for (ButtonObject obj : left) {
      GuiButton button = new GuiButton(obj.hashCode(), x, y, width, height, obj.getButtonText());
      this.addButton(button);
      y += 24;
    }

    x = sr.getScaledWidth() / 2 + 10;
    y = 64;
    for (ButtonObject obj : right) {
      GuiButton button = new GuiButton(obj.hashCode(), x, y, width, height, obj.getButtonText());
      this.addButton(button);
      y += 24;
    }

    maxScrollAmount = splitIndex * 24 + 48 - sr.getScaledHeight();
    if (maxScrollAmount < 0) {
      maxScrollAmount = 0;
    }
  }

  @Override
  public int getMaxScroll() {
    return maxScrollAmount;
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    ButtonObject mouseOveredObject = buttonObjects
        .stream()
        .filter(b -> b.hashCode() == button.id)
        .findFirst()
        .orElse(null);
    mouseOveredObject.actionPerformed(button);
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    Preferences.save();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    //    super.drawScreen(mouseX, mouseY, partialTicks);
    this.drawDefaultBackground();
    this.drawCenteredString(this.fontRenderer, "- HMage Settings -", this.width / 2, 16 - amountScroll, 0xffffff);

    ButtonObject mouseOveredObject = null;

    GlStateManager.pushMatrix();
    GlStateManager.translate(0D, -amountScroll - 24, 0D);
    for (int i = 0; i < this.buttonList.size(); ++i) {
      //ボタンの描画
      GuiButton button = this.buttonList.get(i);
      button.drawButton(this.mc, mouseX, mouseY + amountScroll + 24, partialTicks);

      //マウスでかぶさっているボタンを取得
      if (button.isMouseOver()) {
        mouseOveredObject = buttonObjects.stream().filter(b -> b.hashCode() == button.id).findFirst().orElse(null);
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
