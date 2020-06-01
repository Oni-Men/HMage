package onimen.anni.hmage.transformer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import onimen.anni.hmage.transformer.hook.HookInjector;

public class HMageClassTransformer implements IClassTransformer, Opcodes {

  private static HashMap<String, List<HookInjector>> hookInjectorMap = Maps.newHashMap();

  public static void registerHookInjector(HookInjector injector) {
    if (!hookInjectorMap.containsKey(injector.owner)) {
      hookInjectorMap.put(injector.owner, Lists.newArrayList());
    }
    hookInjectorMap.get(injector.owner).add(injector);
  }

  static {
    registerHookInjector(
        new HookInjector("net.minecraft.client.gui.GuiScreen", "drawWorldBackground", "(I)V") {

          @Override
          public InsnList getInjectInsn() {
            InsnList insnList = new InsnList();

            MethodInsnNode hookNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "onimen/anni/hmage/HMageHooks",
                "onDrawWorldBackground", "()Z",
                false);

            InsnNode returnNode = new InsnNode(Opcodes.RETURN);
            LabelNode gotoNode = new LabelNode();

            JumpInsnNode ifeqNode = new JumpInsnNode(Opcodes.IFEQ, gotoNode);

            insnList.add(hookNode);
            insnList.add(ifeqNode);
            insnList.add(returnNode);
            insnList.add(gotoNode);

            return insnList;
          }

        });
  }

  @Override
  public byte[] transform(String name, String transformedName, byte[] bytes) {
    if (FMLLaunchHandler.side().equals(Side.CLIENT)) {
      if (hookInjectorMap.containsKey(transformedName)) {
        try {

          ClassNode classNode = new ClassNode();
          ClassReader classReader = new ClassReader(bytes);

          classReader.accept(classNode, 0);

          List<HookInjector> injectorList = hookInjectorMap.get(transformedName);

          for (Iterator<MethodNode> i = classNode.methods.iterator(); i.hasNext();) {
            MethodNode methodNode = i.next();

            List<HookInjector> injectorsForMethod = injectorList.stream()
                .filter(h -> h.methodName.equals(methodNode.name) && h.methodDesc.equals(methodNode.desc))
                .collect(Collectors.toList());

            if (!injectorsForMethod.isEmpty()) {
              System.out.println("Inject Hook");
            }

            for (HookInjector injector : injectorsForMethod) {
              injector.injectHook(methodNode.instructions);
            }

          }

          ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
          classNode.accept(classWriter);

          return classWriter.toByteArray();
        } catch (Exception e) {
          throw new RuntimeException(e.getCause());
        }
      }
    }
    return bytes;
  }

}