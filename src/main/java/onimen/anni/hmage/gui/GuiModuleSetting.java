package onimen.anni.hmage.gui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.gui.button.ButtonObject;
import onimen.anni.hmage.gui.button.component.BooleanButtonObject;
import onimen.anni.hmage.gui.button.component.ColorButtonObject;
import onimen.anni.hmage.gui.button.component.HMageGuiSlider;
import onimen.anni.hmage.gui.button.component.NumberButtonObject;
import onimen.anni.hmage.module.AbstractModule;
import onimen.anni.hmage.module.InterfaceModule;
import onimen.anni.hmage.module.annotation.BooleanOption;
import onimen.anni.hmage.module.annotation.ColorOption;
import onimen.anni.hmage.module.annotation.IntegerOption;
import onimen.anni.hmage.util.JavaUtil;

public class GuiModuleSetting extends GuiScreen {

  private GuiScreen parent;
  private InterfaceModule module;

  private List<ButtonObject> buttonObjects = new ArrayList<>();

  public GuiModuleSetting(GuiScreen parent, InterfaceModule module) {
    this.parent = parent;
    this.module = module;

    Set<Field> fields = JavaUtil.getAllDeclaredField(module.getClass());

    addBooleanComponents(JavaUtil.getAnnotatedFields(fields, BooleanOption.class).entrySet());
    addIntegerComponents(JavaUtil.getAnnotatedFields(fields, IntegerOption.class).entrySet());
    addColorComponent(JavaUtil.getAnnotatedFields(fields, ColorOption.class).entrySet());
  }

  @Override
  public void initGui() {
    super.initGui();

    int height = fontRenderer.FONT_HEIGHT;

    int x = this.width / 2 + 30;
    int y = 32 - height / 2 + 12;
    int w = 100;
    int h = 20;

    for (ButtonObject buttonObj : buttonObjects) {
      if (buttonObj instanceof NumberButtonObject) {
        NumberButtonObject intObj = (NumberButtonObject) buttonObj;
        HMageGuiSlider slider = new HMageGuiSlider(intObj.hashCode(), x, y, w, h, intObj);
        addButton(slider);
      } else {
        GuiButton button = new GuiButton(buttonObj.hashCode(), x, y, w, h, buttonObj.getButtonText());
        addButton(button);
      }

      y += 24;
    }

    addButton(new GuiButton(-1, this.width / 2 - 100, this.height - 36, I18n.format("gui.done")));

  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);
    switch (button.id) {
    case -1:
      mc.displayGuiScreen(this.parent);
      break;
    default:
      ButtonObject clicked = this.buttonObjects.stream().filter(b -> b.hashCode() == button.id).findFirst()
          .orElse(null);
      if (clicked != null) {
        clicked.actionPerformed(button);
      }
    }

  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    AbstractModule.savePreferences(module);
    Preferences.save();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);

    this.drawCenteredString(this.fontRenderer, module.getName(), this.width / 2, 16, 0xffffff);

    int x = this.width / 2 - 130;
    int y = 32;

    y += 12;

    for (ButtonObject buttonObject : this.buttonObjects) {
      this.drawString(this.fontRenderer, buttonObject.getTitle(), x, y, 0xffffff);
      y += 24;
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }

  private void addBooleanComponents(Set<Entry<Field, BooleanOption>> entrySet) {
    JavaUtil.tryLoopOptionEntrySet(entrySet, (field, option) -> {
      field.setAccessible(true);
      BooleanButtonObject booleanButtonObject = new BooleanButtonObject(option.name(), value -> {
        JavaUtil.tryExecuteConsumer(field, f -> {
          field.setAccessible(true);
          field.setBoolean(module, value);
        });
      });

      booleanButtonObject.value = field.getBoolean(module);

      buttonObjects.add(booleanButtonObject);

    });
  }

  private void addIntegerComponents(Set<Entry<Field, IntegerOption>> entrySet) {
    JavaUtil.tryLoopOptionEntrySet(entrySet, (field, option) -> {
      field.setAccessible(true);

      NumberButtonObject integerButtonObject = new NumberButtonObject(option.name(), option.min(), option.max(),
          true);

      integerButtonObject.onReleased = value -> {
        JavaUtil.tryExecuteConsumer(field, f -> {
          field.setAccessible(true);
          field.setInt(module, value.intValue());
        });
      };

      buttonObjects.add(integerButtonObject);
    });
  }

  private void addColorComponent(Set<Entry<Field, ColorOption>> entrySet) {
    JavaUtil.tryLoopOptionEntrySet(entrySet, (field, option) -> {
      JavaUtil.tryExecuteConsumer(field, f1 -> {
        field.setAccessible(true);
        ColorButtonObject colorButtonObject = new ColorButtonObject(this, option.name(), field.getInt(module), i -> {
          JavaUtil.tryExecuteConsumer(field, f2 -> {
            f2.setAccessible(true);
            f2.setInt(module, i);
          });
        });
        buttonObjects.add(colorButtonObject);
      });
    });
  }

}
