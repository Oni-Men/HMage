package onimen.anni.hmage.gui.button;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.gui.GuiButton;

public class ConsumableButton implements ButtonObject {

  private String title;
  private String buttonText;
  private List<String> description;
  private Consumer<GuiButton> onClick;

  public ConsumableButton(String title, String buttonText, Consumer<GuiButton> onClick) {
    this.title = title;
    this.buttonText = buttonText;
    this.onClick = onClick;
  }

  @Override
  public String getTitle() {
    return this.title;
  }

  @Override
  public String getButtonText() {
    return this.buttonText;
  }

  @Override
  public void actionPerformed(GuiButton button) {
    this.onClick.accept(button);
  }

  @Override
  public List<String> getDescription() {
    return description;
  }

  public void setDescription(List<String> description) {
    this.description = description;
  }

  public void setButtonText(String buttonText) {
    this.buttonText = buttonText;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
