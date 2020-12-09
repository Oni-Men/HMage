package onimen.anni.hmage.module;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraftforge.common.MinecraftForge;
import onimen.anni.hmage.module.hud.InterfaceHUD;
import onimen.anni.hmage.module.normal.InterfaceModule;

public class ModuleManager {

  private static Map<String, InterfaceModule> moduleMap = Maps.newLinkedHashMap();
  private static Map<String, InterfaceHUD> hudMap = Maps.newLinkedHashMap();

  public static void registerModule(InterfaceModule module) {
    if (module == null) { return; }

    MinecraftForge.EVENT_BUS.register(module);

    moduleMap.put(module.getName(), module);
    if (module instanceof InterfaceHUD) {
      hudMap.put(module.getName(), (InterfaceHUD) module);
    }
  }

  public static Map<String, InterfaceModule> getModuleMap() {
    return moduleMap;
  }

  public static Map<String, InterfaceHUD> getHUDMap() {
    return hudMap;
  }
}
