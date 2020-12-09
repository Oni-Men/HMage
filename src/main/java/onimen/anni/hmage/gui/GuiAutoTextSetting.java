package onimen.anni.hmage.gui;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.module.normal.AutoText;

public class GuiAutoTextSetting extends GuiScreen {

  public GuiTextField handlingTextField;
  public UUID handlingBindId, handlingFieldId;
  private GuiAutoTextBindList bindList;
  private AutoText autoText;

  public GuiAutoTextSetting() {
    this.autoText = HMage.autoText;
  }

  @Override
  public void initGui() {
    super.initGui();
    this.buttonList
        .add(new GuiButton(200, this.width / 2 - 75, this.height - 26, 150, 20, I18n.format("gui.done")));
    this.bindList = new GuiAutoTextBindList(this, this.mc, this.autoText);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();
    this.bindList.drawScreen(mouseX, mouseY, partialTicks);
    this.drawCenteredString(this.fontRenderer, "Auto Text", this.width / 2, 8, 0xFFFFFF);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);
    if (button.id == 200) {
      mc.displayGuiScreen(null);
    }
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    this.autoText.save();
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    if (handlingBindId != null) {
      this.autoText.updateBindKey(handlingBindId, -100 + mouseButton);
    } else if (mouseButton != 0 || !this.bindList.mouseClicked(mouseX, mouseY, mouseButton)) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
    }
  }

  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    if (state != 0 || !this.bindList.mouseReleased(mouseX, mouseY, state)) {
      super.mouseReleased(mouseX, mouseY, state);
    }
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (this.handlingBindId != null) {
      if (keyCode ==  1) {
        this.autoText.updateBindKey(handlingBindId, 0);
      } else if (keyCode != 0) {
        this.autoText.updateBindKey(handlingBindId, keyCode);
      } else if (typedChar > 0) {
        this.autoText.updateBindKey(handlingBindId, typedChar + 256);
      }

      if (!KeyModifier.isKeyCodeModifier(keyCode)) {
        this.handlingBindId = null;
        KeyBinding.resetKeyBindingArrayAndHash();
      }
    } else if (this.handlingFieldId != null && this.handlingTextField != null) {
      this.handlingTextField.textboxKeyTyped(typedChar, keyCode);
      String text = this.handlingTextField.getText();
      this.autoText.updateBindMessage(this.handlingFieldId, text);
    } else {
      super.keyTyped(typedChar, keyCode);
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    this.bindList.handleMouseInput();
  }
}
