package onimen.anni.hmage.module;

import java.util.Iterator;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RecipeBookRemover extends AbstractModule {

  @Override
  public String getId() {
    return "hmage.module.hide-recipebook";
  }

  @SubscribeEvent
  public void onInitGuiEvent(GuiScreenEvent.InitGuiEvent event) {
    if (!canBehaivor()) { return; }

    if (event.getGui() instanceof GuiInventory) {
      removeGuiButtonImage(event.getButtonList().iterator());
    }

    if (event.getGui() instanceof GuiCrafting) {
      removeGuiButtonImage(event.getButtonList().iterator());
    }
  }

  private void removeGuiButtonImage(Iterator<GuiButton> itr) {
    while (itr.hasNext()) {
      GuiButton guiButton = itr.next();
      if (guiButton instanceof GuiButtonImage) {
        itr.remove();
      }
    }
  }

}
