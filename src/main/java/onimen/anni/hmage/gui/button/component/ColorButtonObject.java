package onimen.anni.hmage.gui.button.component;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.gui.GuiColorPicker;
import onimen.anni.hmage.gui.button.ButtonObject;

public class ColorButtonObject implements ButtonObject {

  private final GuiScreen parent;
  private final Consumer<Integer> callback;
  private final String title;
  private final int init;

  public ColorButtonObject(GuiScreen parent, String title, int init, Consumer<Integer> callback) {
    this.init = init;
    this.parent = parent;
    this.callback = callback;
    this.title = title;
  }

  @Override
  public String getTitle() {
    return this.title;
  }

  @Override
  public String getButtonText() {
    return I18n.format("hmage.gui.change-color");
  }

  @Override
  public void actionPerformed(GuiButton button) {
    Minecraft.getMinecraft().displayGuiScreen(new GuiColorPicker(parent, callback, init));
  }

  @Override
  public List<String> getDescription() {
    return null;
  }

}
