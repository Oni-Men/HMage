package onimen.anni.hmage.gui.button.component;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.gui.button.ButtonObject;

public class BooleanButtonObject implements ButtonObject {

  private final String title;
  private final Consumer<Boolean> onClick;
  public boolean value;

  public BooleanButtonObject(String title, Consumer<Boolean> onClick) {
    this.title = title;
    this.onClick = onClick;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getButtonText() {
    return I18n.format(this.value ? "hmage.enable" : "hmage.disable");
  }

  @Override
  public void actionPerformed(GuiButton button) {
    this.value = !this.value;
    onClick.accept(value);
    button.displayString = getButtonText();
  }

  @Override
  public List<String> getDescription() {
    return null;
  }

}
