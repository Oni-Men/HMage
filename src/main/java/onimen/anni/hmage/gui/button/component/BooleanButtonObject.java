package onimen.anni.hmage.gui.button.component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.annotation.BooleanOption;
import onimen.anni.hmage.gui.button.ButtonObject;

public class BooleanButtonObject implements ButtonObject {

  private final String moduleId;
  private final BooleanOption option;
  private final Consumer<Boolean> onClick;
  public boolean value;

  public BooleanButtonObject(String moduleId, BooleanOption option, Consumer<Boolean> onClick) {
    this.moduleId = moduleId;
    this.option = option;
    this.onClick = onClick;
  }

  @Override
  public String getTitle() {
    if (option.name().isEmpty()) { return I18n.format("hmage.module"); }
    return I18n.format(moduleId + "." + option.id() + "." + option.name());
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
    if (option.description().isEmpty()) { return null; }
    return Arrays
        .asList(I18n.format(moduleId + "." + option.id() + "." + option.description()).split(System.lineSeparator()));
  }

}
