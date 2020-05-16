package onimen.anni.hmage.gui;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import onimen.anni.hmage.Preferences;

public class GuiSettings extends GuiScreen {

  private GuiButton modStateButton;
  private GuiButton statusEffectHUD;
  private GuiButton armorDurability;
  private GuiButton arrowRemaining;
  private GuiButton cpsCounterHUD;
  private GuiButton toggleSneak;

  private String getButtonText(boolean b) {
    return b ? "Enabled" : "Disabled";
  }

  public void initGui() {

    this.buttonList.clear();

    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

    int baseX = sr.getScaledWidth() / 2 + 30;
    int baseY = sr.getScaledHeight() / 4;

    int width = 100;
    int height = 20;

    modStateButton = new GuiButton(0, baseX, baseY, width, height,
        getButtonText(Preferences.enabled));

    baseY += 25;

    statusEffectHUD = new GuiButton(1, baseX, baseY, width, height,
        getButtonText(Preferences.statusEffectOption.isEnabled()));

    baseY += 25;

    armorDurability = new GuiButton(2, baseX, baseY, width, height,
        getButtonText(Preferences.statusArmorOption.isEnabled()));

    baseY += 25;

    arrowRemaining = new GuiButton(3, baseX, baseY, width, height,
        getButtonText(Preferences.arrowCounterOption.isEnabled()));

    baseY += 25;

    cpsCounterHUD = new GuiButton(4, baseX, baseY, width, height,
        getButtonText(Preferences.cpsCounterOption.isEnabled()));

    baseY += 25;

    toggleSneak = new GuiButton(5, baseX, baseY, width, height,
        getButtonText(Preferences.toggleSneak));

    this.buttonList.add(modStateButton);
    this.buttonList.add(statusEffectHUD);
    this.buttonList.add(armorDurability);
    this.buttonList.add(arrowRemaining);
    this.buttonList.add(cpsCounterHUD);
    this.buttonList.add(toggleSneak);
  }

  protected void actionPerformed(GuiButton button) {
    switch (button.id) {
    case 0:
      Preferences.enabled = !Preferences.enabled;
      button.displayString = getButtonText(Preferences.enabled);
      break;
    case 1:
      Preferences.statusEffectOption.toggleEnabled();
      button.displayString = getButtonText(Preferences.statusEffectOption.isEnabled());
      break;
    case 2:
      Preferences.statusArmorOption.toggleEnabled();
      button.displayString = getButtonText(Preferences.statusArmorOption.isEnabled());
      break;
    case 3:
      Preferences.arrowCounterOption.toggleEnabled();
      button.displayString = getButtonText(Preferences.arrowCounterOption.isEnabled());
      break;
    case 4:
      Preferences.cpsCounterOption.toggleEnabled();
      button.displayString = getButtonText(Preferences.cpsCounterOption.isEnabled());
      break;
    case 5:
      Preferences.toggleSneak = !Preferences.toggleSneak;
      button.displayString = getButtonText(Preferences.toggleSneak);
    }
  }

  @Override
  public void onGuiClosed() {
    Preferences.save();
    super.onGuiClosed();
  }

  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    int halfWidth = sr.getScaledWidth() / 2;

    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);

    this.drawCenteredString(this.fontRenderer, ChatFormatting.GOLD + "- HMage Settings -", halfWidth,
        16,
        0xffffff);

    int baseX = sr.getScaledWidth() / 2 - 130;
    int baseY = sr.getScaledHeight() / 4;

    int offset = this.fontRenderer.FONT_HEIGHT / 2 - 12;

    baseY -= offset;

    this.drawString(this.fontRenderer, "Mod", baseX, baseY, 0xffffff);
    baseY += 25;
    this.drawString(this.fontRenderer, "Status Effect HUD", baseX, baseY, 0xffffff);
    baseY += 25;
    this.drawString(this.fontRenderer, "Status Armor HUD", baseX, baseY, 0xffffff);
    baseY += 25;
    this.drawString(this.fontRenderer, "Arrow Counter HUD", baseX, baseY, 0xffffff);
    baseY += 25;
    this.drawString(this.fontRenderer, "CPS HUD", baseX, baseY, 0xffffff);
    baseY += 25;
    this.drawString(this.fontRenderer, "Toggle Sneak", baseX, baseY, 0xffffff);

  }

  public boolean doesGuiPauseGame() {
    return false;
  }

}
