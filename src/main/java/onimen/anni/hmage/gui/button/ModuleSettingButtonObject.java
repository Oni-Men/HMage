package onimen.anni.hmage.gui.button;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.module.InterfaceModule;

public class ModuleSettingButtonObject implements ButtonObject {

  private InterfaceModule module;

  public ModuleSettingButtonObject(InterfaceModule interfaceModule) {
    this.module = interfaceModule;
  }

  @Override
  public String getTitle() {
    return module.getName();
  }

  @Override
  public String getButtonText() {
    return I18n.format(Preferences.getBoolean(module.getId() + ".enabled", false) ? "hmage.enable" : "hmage.disable");
  }

  @Override
  public void actionPerformed(GuiButton button) {
    module.setEnable(!module.isEnable());
    button.displayString = getButtonText();
  }

  @Override
  public List<String> getDescription() {
    return module.getDescription();
  }
}
