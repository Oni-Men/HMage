package onimen.anni.hmage.module;

import java.util.List;

import com.google.common.collect.Lists;

import onimen.anni.hmage.preference.Preferences;

public abstract class AbstractModule implements InterfaceModule {

  @Override
  public void setEnable(boolean value) {
    Preferences.setBoolean(this.getName() + ".enabled", value);
  }

  @Override
  public boolean isEnable() {
    return Preferences.getBoolean(this.getName() + ".enabled", true);
  }

  @Override
  public List<String> getPreferenceKeys() {
    return Lists.newArrayList("enabled");
  }

}
