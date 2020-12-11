package onimen.anni.hmage.transformer;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import onimen.anni.hmage.transformer.HookInjector.MethodIdentifier;
import onimen.anni.hmage.transformer.HookInjectorManager.ObfuscateType;

public class HMageClassTransformer implements IClassTransformer {

  @Override
  public byte[] transform(String name, String transformedName, byte[] bytes) {
    if (FMLLaunchHandler.side().equals(Side.CLIENT) && transformedName.startsWith("net.minecraft.")) {
      if (HookInjectorManager.hasInjectorFor(transformedName)) {
        try {

          ClassNode classNode = new ClassNode();
          ClassReader classReader = new ClassReader(bytes);

          classReader.accept(classNode, ClassReader.SKIP_FRAMES);

          List<HookInjector> injectorList = HookInjectorManager.getInjectorsFor(transformedName);

          for (Iterator<MethodNode> i = classNode.methods.iterator(); i.hasNext();) {
            MethodNode methodNode = i.next();

            for (HookInjector injector : injectorList) {
              callInjector(methodNode, injector);
            }

            //            List<HookInjector> injectorsForMethod = injectorList.stream()
            //                .filter(h -> h.methodNames.contains(methodNode.name) && h.methodDesc.equals(methodNode.desc))
            //                .collect(Collectors.toList());
            //            for (HookInjector injector : injectorsForMethod) {
            //
            //              injector.injectHook(methodNode.instructions, null);
            //              FMLLog.log.info(String.format("[HMage CORE] Hook was injected into %s", injector.owner));
            //            }
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

  private void callInjector(MethodNode methodNode, HookInjector injector) {
    for (ObfuscateType type : ObfuscateType.values()) {
      MethodIdentifier entry = injector.getEntry(type);
      if (entry == null)
        continue;

      if (!methodNode.name.equals(entry.methodName))
        continue;

      if (!methodNode.desc.equals(entry.methodDesc))
        continue;

      boolean ok = injector.injectHook(methodNode.instructions, type);
      FMLLog.log.info(String.format("[HMage CORE] Hook inject into %s: %s", injector.owner, ok ? "SUCCESS" : "FAILED"));
    }
  }
}