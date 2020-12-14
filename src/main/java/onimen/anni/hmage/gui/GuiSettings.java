package onimen.anni.hmage.gui;

import java.io.IOException;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.gui.button.CapeSelectButtonObject;
import onimen.anni.hmage.gui.button.ConsumableButton;
import onimen.anni.hmage.gui.button.GameHistoryButton;
import onimen.anni.hmage.gui.button.HUDLayoutButton;
import onimen.anni.hmage.gui.button.HurtingArmorColorButton;
import onimen.anni.hmage.gui.button.ModEnabledButtonObject;
import onimen.anni.hmage.module.normal.InterfaceModule;

public class GuiSettings extends HMageGui {

  public GuiSettings(Map<String, InterfaceModule> moduleMap) {
    super(null);
    //mod 有効/無効の設定
    buttonObjects.add(new ModEnabledButtonObject());
    buttonObjects.add(new GameHistoryButton(this));

    buttonObjects.add(new HUDLayoutButton(this));

    //capeの設定
    buttonObjects.add(new CapeSelectButtonObject());
    buttonObjects.add(new HurtingArmorColorButton());

    buttonObjects.add(new ConsumableButton("HUD Settings", "HUD Setting", button -> {
      mc.displayGuiScreen(new GuiHUDSettings(this));
    }));

    buttonObjects.add(new ConsumableButton("Normal Modules", "Normal Modules", button -> {
      mc.displayGuiScreen(new GuiNormalSettings(this));
    }));
    //buttonObjects.add(new DebugButtonObject());
  }

  @Override
  public void initGui() {
    super.initGui();
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    super.actionPerformed(button);
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    Preferences.save();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawCenteredString(this.fontRenderer, "- HMage Settings -", this.width / 2, 16, 0xffffff);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  public boolean doesGuiPauseGame() {
    return false;
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
