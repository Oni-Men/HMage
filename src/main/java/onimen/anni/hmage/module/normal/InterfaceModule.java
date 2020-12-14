package onimen.anni.hmage.module.normal;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import onimen.anni.hmage.gui.button.ButtonObject;

public interface InterfaceModule {

  public boolean isEnable();

  default boolean doesShowMenu() {
    return true;
  }

  public void setEnable(boolean value);

  public boolean canBehave();

  public String getId();

  public String getName();

  public List<String> getDescription();

  public ButtonObject getSettingButton(GuiScreen parent);
}
