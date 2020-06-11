package onimen.anni.hmage.module;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.gui.button.ButtonObject;

public abstract class AbstractModule implements InterfaceModule {

  private static final String LINE_SEPARATOR = System.lineSeparator();

  @Override
  public void setEnable(boolean value) {
    Preferences.setBoolean(this.getId() + ".enabled", value);
  }

  @Override
  public boolean isEnable() {
    return Preferences.getBoolean(this.getId() + ".enabled", true);
  }

  @Override
  public boolean canBehaivor() {
    return Preferences.enabled && isEnable();
  }

  @Override
  public String getName() {
    return I18n.format(getId() + ".name");
  }

  @Override
  public List<String> getDescription() {
    return Arrays.asList(I18n.format(getId() + ".description").split(LINE_SEPARATOR));
  }

  @Override
  public ButtonObject getPreferenceButton() {
    return null;
  }

}
