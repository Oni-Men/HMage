package onimen.anni.hmage.transformer;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraftforge.fml.common.FMLLog;
import onimen.anni.hmage.transformer.hook.DrawBackgroundHook;
import onimen.anni.hmage.transformer.hook.GetCapeTextureLocationHook;
import onimen.anni.hmage.transformer.hook.GetCharWidthFloatHook;
import onimen.anni.hmage.transformer.hook.GetCharWidthHook;
import onimen.anni.hmage.transformer.hook.LoadFontTextureHook;
import onimen.anni.hmage.transformer.hook.ParticleHandleHook;
import onimen.anni.hmage.transformer.hook.RenderCharHook;
import onimen.anni.hmage.transformer.hook.RenderScoreBoardHook;

public class HookInjectorManager {

  public enum ObfuscateType {
    OBF,
    SRG,
    DEOBF
  }

  private static HashMap<String, List<HookInjector>> hookInjectorMap = Maps.newHashMap();

  public static void registerHookInjector(HookInjector injector) {
    hookInjectorMap
        .computeIfAbsent(injector.owner, k -> Lists.newArrayList())
        .add(injector);
  }

  public static List<HookInjector> getInjectorsFor(String transformedName) {
    return hookInjectorMap.get(transformedName);
  }

  public static boolean hasInjectorFor(String transformedName) {
    return hookInjectorMap.containsKey(transformedName);
  }

  static {
    FMLLog.log.info("[HMage CORE]Registering Hook injector");
    registerHookInjector(new DrawBackgroundHook());
    registerHookInjector(new GetCapeTextureLocationHook());
    registerHookInjector(new ParticleHandleHook());
    registerHookInjector(new RenderCharHook());
    registerHookInjector(new LoadFontTextureHook());
    registerHookInjector(new GetCharWidthHook());
    registerHookInjector(new GetCharWidthFloatHook());
    registerHookInjector(new RenderScoreBoardHook());
  }

}
