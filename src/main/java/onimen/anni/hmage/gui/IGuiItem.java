package onimen.anni.hmage.gui;

import net.minecraft.client.Minecraft;

public interface IGuiItem {

	public boolean isEnabled();

	public void drawItem(Minecraft mc);

}
