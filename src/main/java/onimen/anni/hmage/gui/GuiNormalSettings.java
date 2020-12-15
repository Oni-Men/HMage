package onimen.anni.hmage.gui;

import net.minecraft.client.gui.GuiScreen;
import onimen.anni.hmage.module.ModuleManager;
import onimen.anni.hmage.module.normal.InterfaceModule;

public class GuiNormalSettings extends HMageGui {

  public GuiNormalSettings(GuiScreen parent) {
    super(parent);
    this.title = "Normal Module Settings";
    for (InterfaceModule module : ModuleManager.getNormalMap().values()) {
      if (module.doesShowMenu()) {
        this.buttonObjects.add(module.getSettingButton(this));
      }
    }

  }

}
