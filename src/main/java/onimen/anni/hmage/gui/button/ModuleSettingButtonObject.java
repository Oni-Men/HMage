package onimen.anni.hmage.gui.button;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import onimen.anni.hmage.gui.GuiModuleSetting;
import onimen.anni.hmage.module.normal.InterfaceModule;

public class ModuleSettingButtonObject implements ButtonObject {

  private InterfaceModule module;
  private GuiScreen parent;

  public ModuleSettingButtonObject(InterfaceModule interfaceModule, GuiScreen parent) {
    this.module = interfaceModule;
    this.parent = parent;
  }

  @Override
  public String getTitle() {
    return module.getName();
  }

  @Override
  public String getButtonText() {
    return module.getName();
  }

  @Override
  public void actionPerformed(GuiButton button) {
    Minecraft.getMinecraft().displayGuiScreen(new GuiModuleSetting(parent, module));
  }

  @Override
  public List<String> getDescription() {
    return module.getDescription();
  }
}
