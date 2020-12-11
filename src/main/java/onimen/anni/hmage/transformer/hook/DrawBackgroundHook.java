package onimen.anni.hmage.transformer.hook;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;

import onimen.anni.hmage.transformer.HookInjector;
import onimen.anni.hmage.transformer.HookInjectorManager.ObfuscateType;

public class DrawBackgroundHook extends HookInjector {

  /*
   * drawWorldBackground
   * func_146270_b
   * d_
   */
  public DrawBackgroundHook() {
    super("net.minecraft.client.gui.GuiScreen");
    this.registerEntry(ObfuscateType.DEOBF, "drawWorldBackground", "(I)V");
    this.registerEntry(ObfuscateType.OBF, "d_", "(I)V");
  }

  @Override
  public boolean injectHook(InsnList list, ObfuscateType type) {
    InsnList injectings = new InsnList();

    MethodInsnNode hookNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "onimen/anni/hmage/HMageHooks",
        "onDrawWorldBackground", Type.getMethodDescriptor(Type.BOOLEAN_TYPE), false);

    InsnNode returnNode = new InsnNode(Opcodes.RETURN);
    LabelNode gotoNode = new LabelNode();

    JumpInsnNode ifeqNode = new JumpInsnNode(Opcodes.IFEQ, gotoNode);

    injectings.add(hookNode);
    injectings.add(ifeqNode);
    injectings.add(returnNode);
    injectings.add(gotoNode);

    list.insert(injectings);

    return true;
  }

}
