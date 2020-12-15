package onimen.anni.hmage.gui.button;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.gui.GuiColorPicker;

public class HurtingArmorColorButton implements ButtonObject {

  private Consumer<Integer> callback = i -> {
    Preferences.hurtingArmorColor = i;
    Preferences.save();
  };

  private GuiScreen screen;

  public HurtingArmorColorButton(GuiScreen screen) {
    this.screen = screen;
  }


  @Override
  public String getTitle() {
    return "Hurting Armor";
  }

  @Override
  public String getButtonText() {
    return getTitle();
  }

  @Override
  public void actionPerformed(GuiButton button) {
    Minecraft.getMinecraft()
        .displayGuiScreen(new GuiColorPicker(screen, callback, Preferences.hurtingArmorColor));
  }

  @Override
  public List<String> getDescription() {
    return Arrays.asList("殴ったときに変わる敵の色を変更できます。");
  }

}
