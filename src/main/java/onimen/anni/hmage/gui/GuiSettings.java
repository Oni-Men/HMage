package onimen.anni.hmage.gui;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import onimen.anni.hmage.Preferences;

public class GuiSettings extends GuiScreen {

	private GuiButton modStateButton;
	private GuiButton potionEffectHUD;
	private GuiButton armorDurability;
	private GuiButton arrowRemaining;
	private GuiButton cpsHUD;

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

		potionEffectHUD = new GuiButton(1, baseX, baseY + 25, width, height,
				getButtonText(Preferences.potionEffectsHUD));

		armorDurability = new GuiButton(2, baseX, baseY + 50, width, height,
				getButtonText(Preferences.armorDurabilityHUD));

		arrowRemaining = new GuiButton(3, baseX, baseY + 75, width, height,
				getButtonText(Preferences.arrowCounter));

		cpsHUD = new GuiButton(4, baseX, baseY + 100, width, height,
				getButtonText(Preferences.cpsHUD));

		this.buttonList.add(modStateButton);
		this.buttonList.add(potionEffectHUD);
		this.buttonList.add(armorDurability);
		this.buttonList.add(arrowRemaining);
		this.buttonList.add(cpsHUD);
	}

	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			Preferences.enabled = !Preferences.enabled;
			button.displayString = getButtonText(Preferences.enabled);
			break;
		case 1:
			Preferences.potionEffectsHUD = !Preferences.potionEffectsHUD;
			button.displayString = getButtonText(Preferences.potionEffectsHUD);
			break;
		case 2:
			Preferences.armorDurabilityHUD = !Preferences.armorDurabilityHUD;
			button.displayString = getButtonText(Preferences.armorDurabilityHUD);
			break;
		case 3:
			Preferences.arrowCounter = !Preferences.arrowCounter;
			button.displayString = getButtonText(Preferences.arrowCounter);
			break;
		case 4:
			Preferences.cpsHUD = !Preferences.cpsHUD;
			button.displayString = getButtonText(Preferences.cpsHUD);
			break;
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

		this.drawString(this.fontRenderer, "Mod", baseX, baseY - offset, 0xffffff);
		this.drawString(this.fontRenderer, "Potion Effect HUD", baseX, baseY + 25 - offset, 0xffffff);
		this.drawString(this.fontRenderer, "Armor Durability HUD", baseX, baseY + 50 - offset, 0xffffff);
		this.drawString(this.fontRenderer, "Arrow Counter", baseX, baseY + 75 - offset, 0xffffff);
		this.drawString(this.fontRenderer, "CPS HUD", baseX, baseY + 100 - offset, 0xffffff);

	}

	public boolean doesGuiPauseGame() {
		return false;
	}

}
