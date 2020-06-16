package onimen.anni.hmage.gui.button.component;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.gui.GuiButton;
import onimen.anni.hmage.gui.button.ButtonObject;

public class NumberButtonObject implements ButtonObject {

  private final String title;
  public boolean useInts;
  public float value = 0f;
  public final float min, max;

  public Consumer<Number> onReleased;

  public NumberButtonObject(String title, float min, float max, boolean useInts) {
    this.title = title;
    this.useInts = useInts;
    this.min = min;
    this.max = max;
  }

  @Override
  public String getTitle() {
    return this.title;
  }

  @Override
  public String getButtonText() {
    return String.valueOf(value);
  }

  @Override
  public void actionPerformed(GuiButton button) {

  }

  @Override
  public List<String> getDescription() {
    return null;
  }

}
