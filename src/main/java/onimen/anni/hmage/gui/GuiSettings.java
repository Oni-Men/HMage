package onimen.anni.hmage.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.gui.button.ButtonObject;
import onimen.anni.hmage.gui.button.CapeSelectButtonObject;
import onimen.anni.hmage.gui.button.ModEnabledButtonObject;
import onimen.anni.hmage.gui.button.ModuleSettingButtonObject;
import onimen.anni.hmage.module.InterfaceModule;

public class GuiSettings extends GuiScreen {

  private List<ButtonObject> buttonObjects = new ArrayList<ButtonObject>();

  private int prevMouseY = 0;
  private int scrollY = 0;

  public GuiSettings(Map<String, InterfaceModule> moduleMap) {
    //mob 有効/無効の設定
    buttonObjects.add(new ModEnabledButtonObject());

    //各モジュールの有効/無効の設定
    for (InterfaceModule module : moduleMap.values()) {
      buttonObjects.add(new ModuleSettingButtonObject(module));
    }

    //capeの設定
    buttonObjects.add(new CapeSelectButtonObject());
  }

  @Override
  public void initGui() {

    this.buttonList.clear();

    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

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

  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
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

    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

    this.drawDefaultBackground();

    this.drawCenteredString(this.fontRenderer, "- HMage Settings -", this.width / 2, 16 - scrollY, 0xffffff);

    int x = sr.getScaledWidth() / 2 - 130;
    int y = 32 - scrollY;

    y += 12;

    for (ButtonObject buttonObject : this.buttonObjects) {
      this.drawString(this.fontRenderer, buttonObject.getTitle(), x, y, 0xffffff);
      y += 24;
    }

    GlStateManager.pushMatrix();
    GlStateManager.translate(0D, -scrollY - 24, 0D);
    for (int i = 0; i < this.buttonList.size(); ++i) {
      this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY + scrollY + 24, partialTicks);
    }
    GlStateManager.popMatrix();
  }

  @Override
  public boolean doesGuiPauseGame() {
    return false;
  }

  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

    scrollY += prevMouseY - mouseY;
    prevMouseY = mouseY;

    if (scrollY < 0) {
      scrollY = 0;
    }

    int maxScrollY = this.buttonObjects.size() * 24 + 32;

    if (scrollY > maxScrollY) {
      scrollY = maxScrollY;
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    prevMouseY = mouseY;
    if (mouseButton == 0) {
      for (int i = 0; i < this.buttonList.size(); ++i) {
        GuiButton guibutton = this.buttonList.get(i);

        if (guibutton.mousePressed(this.mc, mouseX, mouseY + scrollY + 24)) {
          net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre event = new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre(
              this, guibutton, this.buttonList);
          if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
            break;
          guibutton = event.getButton();
          this.selectedButton = guibutton;
          guibutton.playPressSound(this.mc.getSoundHandler());
          this.actionPerformed(guibutton);
          if (this.equals(this.mc.currentScreen))
            net.minecraftforge.common.MinecraftForge.EVENT_BUS
                .post(new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post(this,
                    event.getButton(), this.buttonList));
        }
      }
    }
  }

  /**
   * Called when a mouse button is released.
   */
  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    if (this.selectedButton != null && state == 0) {
      this.selectedButton.mouseReleased(mouseX, mouseY + scrollY);
      this.selectedButton = null;
    }
  }
}
