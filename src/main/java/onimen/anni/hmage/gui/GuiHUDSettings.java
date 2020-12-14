package onimen.anni.hmage.gui;

import net.minecraft.client.gui.GuiScreen;
import onimen.anni.hmage.module.ModuleManager;
import onimen.anni.hmage.module.hud.InterfaceHUD;

public class GuiHUDSettings extends HMageGui {

  public GuiHUDSettings(GuiScreen parent) {
    super(parent);

    for (InterfaceHUD module : ModuleManager.getHUDMap().values()) {
      if (module.doesShowMenu()) {
        buttonObjects.add(module.getSettingButton(this));
      }
    }
  }

}
