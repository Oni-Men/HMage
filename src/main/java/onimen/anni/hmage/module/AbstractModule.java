package onimen.anni.hmage.module;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.gui.button.ButtonObject;
import onimen.anni.hmage.gui.button.ModuleSettingButtonObject;
import onimen.anni.hmage.module.annotation.BooleanOption;
import onimen.anni.hmage.module.annotation.ColorOption;
import onimen.anni.hmage.module.annotation.FloatOption;
import onimen.anni.hmage.module.annotation.IntegerOption;
import onimen.anni.hmage.module.normal.InterfaceModule;
import onimen.anni.hmage.util.JavaUtil;

public abstract class AbstractModule implements InterfaceModule {

  private static final String LINE_SEPARATOR = System.lineSeparator();

  @BooleanOption(id = "enabled")
  private boolean enabled = true;

  public AbstractModule() {
    loadPreferences(this);
  }

  public static void loadPreferences(InterfaceModule module) {
    Set<Field> fields = JavaUtil.getAllDeclaredField(module.getClass());

    JavaUtil.tryLoopOptionEntrySet(JavaUtil.getAnnotatedFields(fields, BooleanOption.class).entrySet(),
        (field, option) -> {
          field.setAccessible(true);
          field.setBoolean(module,
              Preferences.getBoolean(module.getId() + "." + option.id(), field.getBoolean(module)));
        });
    JavaUtil.tryLoopOptionEntrySet(JavaUtil.getAnnotatedFields(fields, IntegerOption.class).entrySet(),
        (field, option) -> {
          field.setAccessible(true);
          field.setInt(module, Preferences.getInt(module.getId() + "." + option.id(), field.getInt(module)));
        });
    JavaUtil.tryLoopOptionEntrySet(JavaUtil.getAnnotatedFields(fields, ColorOption.class).entrySet(),
        (field, option) -> {
          field.setAccessible(true);
          field.setInt(module, Preferences.getInt(module.getId() + "." + option.id(), field.getInt(module)));
        });
    JavaUtil.tryLoopOptionEntrySet(JavaUtil.getAnnotatedFields(fields, FloatOption.class).entrySet(),
        (field, option) -> {
          field.setAccessible(true);
          field.setFloat(module, Preferences.getFloat(module.getId() + "." + option.id(), field.getFloat(module)));
        });
  }

  public static void savePreferences(InterfaceModule module) {
    Set<Field> fields = JavaUtil.getAllDeclaredField(module.getClass());
    JavaUtil.tryLoopOptionEntrySet(JavaUtil.getAnnotatedFields(fields, BooleanOption.class).entrySet(),
        (field, option) -> {
          field.setAccessible(true);
          Preferences.setBoolean(module.getId() + "." + option.id(), field.getBoolean(module));
        });
    JavaUtil.tryLoopOptionEntrySet(JavaUtil.getAnnotatedFields(fields, IntegerOption.class).entrySet(),
        (field, option) -> {
          field.setAccessible(true);
          Preferences.setInt(module.getId() + "." + option.id(), field.getInt(module));
        });
    JavaUtil.tryLoopOptionEntrySet(JavaUtil.getAnnotatedFields(fields, ColorOption.class).entrySet(),
        (field, option) -> {
          field.setAccessible(true);
          Preferences.setInt(module.getId() + "." + option.id(), field.getInt(module));
        });
    JavaUtil.tryLoopOptionEntrySet(JavaUtil.getAnnotatedFields(fields, FloatOption.class).entrySet(),
        (field, option) -> {
          field.setAccessible(true);
          Preferences.setFloat(module.getId() + "." + option.id(), field.getFloat(module));
        });
  }

  @Override
  public ButtonObject getSettingButton(GuiScreen parent) {
    return new ModuleSettingButtonObject(this, parent);
  }

  @Override
  public void setEnable(boolean value) {
    this.enabled = value;
  }

  @Override
  public boolean isEnable() {
    return enabled;
  }

  @Override
  public boolean canBehave() {
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

}
