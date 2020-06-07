package onimen.anni.hmage.transformer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import onimen.anni.hmage.transformer.hook.DrawBackgroundHook;
import onimen.anni.hmage.transformer.hook.GetCapeTextureLocationHook;
import onimen.anni.hmage.transformer.hook.HookInjector;
import onimen.anni.hmage.transformer.hook.ParticleHandleHook;

public class HMageClassTransformer implements IClassTransformer {

  private static HashMap<String, List<HookInjector>> hookInjectorMap = Maps.newHashMap();

  public static void registerHookInjector(HookInjector injector) {
    hookInjectorMap
        .computeIfAbsent(injector.owner, k -> Lists.newArrayList())
        .add(injector);
  }

  static {
    FMLLog.log.info("[HMage CORE]Registering Hook injector");
    registerHookInjector(new DrawBackgroundHook());
    registerHookInjector(new GetCapeTextureLocationHook());
    registerHookInjector(new ParticleHandleHook());
  }

  @Override
  public byte[] transform(String name, String transformedName, byte[] bytes) {
    if (FMLLaunchHandler.side().equals(Side.CLIENT) && transformedName.startsWith("net.minecraft.")) {
      if (hookInjectorMap.containsKey(transformedName)) {
        try {

          ClassNode classNode = new ClassNode();
          ClassReader classReader = new ClassReader(bytes);

          classReader.accept(classNode, 0);

          List<HookInjector> injectorList = hookInjectorMap.get(transformedName);

          for (Iterator<MethodNode> i = classNode.methods.iterator(); i.hasNext();) {
            MethodNode methodNode = i.next();

            List<HookInjector> injectorsForMethod = injectorList.stream()
                .filter(h -> h.methodNames.contains(methodNode.name) && h.methodDesc.equals(methodNode.desc))
                .collect(Collectors.toList());

            for (HookInjector injector : injectorsForMethod) {
              injector.injectHook(methodNode.instructions);
              FMLLog.log.info(String.format("[HMage CORE] Hook was injected into %s", injector.owner));
            }
          }

          ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
          classNode.accept(classWriter);

          return classWriter.toByteArray();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    return bytes;
  }

}