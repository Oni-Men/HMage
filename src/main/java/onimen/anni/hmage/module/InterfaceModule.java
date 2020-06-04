package onimen.anni.hmage.module;

import java.util.List;

import onimen.anni.hmage.gui.button.ButtonObject;

public interface InterfaceModule {

  public boolean isEnable();

  default boolean isShowMenu() {
    return true;
  }

  public void setEnable(boolean value);

  public String getName();

  public String getDescription();

  public List<String> getPreferenceKeys();

  public ButtonObject getPreferenceButton();

}
