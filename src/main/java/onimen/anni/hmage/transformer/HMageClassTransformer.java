package onimen.anni.hmage.transformer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

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

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

public class HMageClassTransformer implements IClassTransformer, Opcodes {

  private static final String[] replaceList = new String[] {
      "net.minecraft.client.gui.GuiScreen"
  };

  @Override
  public byte[] transform(String name, String transformedName, byte[] bytes) {
    if (FMLLaunchHandler.side().equals(Side.CLIENT)) {
      if (Arrays.<String> asList(replaceList).contains(transformedName)) {
        try {
          System.out.println("Trying to replace class: " + transformedName);
          return transformClass(bytes, name, transformedName);
        } catch (Exception e) {
          throw new RuntimeException(e.getCause());
        }
      }
    }
    return bytes;
  }

  private byte[] transformClass(byte[] bytes, String name, String transformedName) throws IOException {

    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);

    classReader.accept(classNode, 0);

    injectHook(classNode);

    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    classNode.accept(classWriter);

    return classWriter.toByteArray();
  }

  private void injectHook(ClassNode node) {
    for (Iterator<MethodNode> i = node.methods.iterator(); i.hasNext();) {
      MethodNode next = i.next();
      if (next.name.equals("drawWorldBackground") && next.desc.equals("(I)V")) {

        /* insert code like this.
         *
         * if (onDrawWorldBackground()) return;
         *
         */

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

        next.instructions.insert(insnList);

        break;
      }
    }
  }
}