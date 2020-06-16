package onimen.anni.hmage.module;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLockIconButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OldGUI extends AbstractModule {

  @Override
  public String getId() {
    return "hmage.module.old-gui";
  }

  @SubscribeEvent
  public void onGuiInit(InitGuiEvent event) {
    if (!canBehaivor()) { return; }
    if (event.getGui() instanceof GuiOptions) {
      event.setButtonList(removeDifficultyLock(event.getButtonList()));
    }
  }

  private List<GuiButton> removeDifficultyLock(List<GuiButton> buttonList) {
    GuiButton current = null, previous = null;
    for (Iterator<GuiButton> i = buttonList.iterator(); i.hasNext();) {
      previous = current;
      current = i.next();
      if (current instanceof GuiLockIconButton) {
        i.remove();
        if (previous != null) {
          previous.setWidth(150);
        }
      }
    }
    return buttonList;
  }
}
