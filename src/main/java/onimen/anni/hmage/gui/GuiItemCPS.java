/*
 * Referenced https://github.com/GitFyu/KeystrokesBase
 */

package onimen.anni.hmage.gui;

import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import onimen.anni.hmage.Preferences;

public class GuiItemCPS extends Gui implements IGuiItem, AttackKeyListener {

	private Queue<Long> clicks = new LinkedList<Long>();

	public GuiItemCPS() {
		this.clicks = new LinkedList<>();
	}

	private int calculateCPS() {

		long now = System.currentTimeMillis();

		while (!clicks.isEmpty() && clicks.peek() < now) {
			clicks.remove();
		}

		return clicks.size();

	}

	@Override
	public boolean isEnabled() {
		return Preferences.cpsHUD;
	}

	@Override
	public void drawItem(Minecraft mc) {

		String text = String.format("%d CPS", this.calculateCPS());

		int width = mc.fontRenderer.getStringWidth("00 CPS") + 4;
		int height = mc.fontRenderer.FONT_HEIGHT;

		int x = width / 2 + 2;
		int y = height / 2;

		if (Preferences.armorDurabilityHUD) {
			x += mc.fontRenderer.getStringWidth("000") + 20 + 1;
		}

		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();

		GlStateManager.pushMatrix();
		GlStateManager.color(0, 0, 0, 0.3f);
		GlStateManager.translate(x - width / 2, y - 1, 0);
		GlStateManager.glBegin(GL11.GL_QUADS);
		GlStateManager.glVertex3f(0, height + 2, 0);
		GlStateManager.glVertex3f(width, height + 2, 0);
		GlStateManager.glVertex3f(width, 0, 0);
		GlStateManager.glVertex3f(0, 0, 0);
		GlStateManager.glEnd();
		GlStateManager.popMatrix();

		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();

		this.drawCenteredString(mc.fontRenderer, text, x, y, 0xffffff);

	}

	@Override
	public void onAttackKeyClick() {
		this.clicks.add(System.currentTimeMillis() + 1000L);
	}

}
