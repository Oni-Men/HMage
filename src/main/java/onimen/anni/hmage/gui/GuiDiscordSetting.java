package onimen.anni.hmage.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.gui.button.ConsumableButton;

public class GuiDiscordSetting extends HMageGui {

  public GuiDiscordSetting(GuiScreen parent) {
    super(parent);
    this.title = "Discord Settings";
    this.rows = 1;
    this.buttonTitleVisible = true;

    this.buttonObjects.add(new ConsumableButton("Discord", buttonText(Preferences.enableDiscordRPC),
        b -> {
          Preferences.enableDiscordRPC = !Preferences.enableDiscordRPC;
          b.displayString = buttonText(Preferences.enableDiscordRPC);
        }));

    this.buttonObjects
        .add(new ConsumableButton("Server IP", buttonText(Preferences.showServerIPonDiscord), b -> {
          Preferences.showServerIPonDiscord = !Preferences.showServerIPonDiscord;
          b.displayString = buttonText(Preferences.showServerIPonDiscord);
        }));

    this.buttonObjects
        .add(new ConsumableButton("Player Name", buttonText(Preferences.showUserNameOnDiscord), b -> {
          Preferences.showUserNameOnDiscord = !Preferences.showUserNameOnDiscord;
          b.displayString = buttonText(Preferences.showUserNameOnDiscord);
        }));

    this.buttonObjects
        .add(new ConsumableButton("Annihilation Info", buttonText(Preferences.showAnniGameInfoOnDiscord), b -> {
          Preferences.showAnniGameInfoOnDiscord = !Preferences.showAnniGameInfoOnDiscord;
          b.displayString = buttonText(Preferences.showAnniGameInfoOnDiscord);
        }));
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    Preferences.save();
  }

  private String buttonText(boolean b) {
    return I18n.format(b ? "hmage.enable" : "hmage.disable");
  }
}
