package onimen.anni.hmage.gui.button;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import onimen.anni.hmage.Preferences;

public class ModEnabledButtonObject implements ButtonObject {

  @Override
  public String getTitle() {
    return "HMage Mod";
  }

  @Override
  public String getButtonText() {
    return Preferences.enabled ? "Enable" : "Disable";
  }

  @Override
  public void actionPerformed(GuiButton button) {
    Preferences.enabled = !Preferences.enabled;
    button.displayString = getButtonText();
  }

  @Override
  public List<String> getDescription() {
    return null;
  }

}
