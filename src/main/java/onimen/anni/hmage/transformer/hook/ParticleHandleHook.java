package onimen.anni.hmage.transformer.hook;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ParticleHandleHook extends HookInjector {

  /*
   * drawWorldBackground
   * func_146270_b
   * d_
   */
  public ParticleHandleHook() {
    super("net.minecraft.client.network.NetHandlerPlayClient",
        "(Ljg;)V", "handleParticles", "a");
  }

  @Override
  public void injectHook(InsnList list) {
    InsnList injectings = new InsnList();

    MethodInsnNode hook = new MethodInsnNode(Opcodes.INVOKESTATIC, "onimen/anni/hmage/HMageHooks",
        "onhandleParticles", "(Lnet/minecraft/network/play/server/SPacketParticles;)Z", false);

    InsnNode returnNode = new InsnNode(Opcodes.RETURN);
    LabelNode gotoNode = new LabelNode();
    JumpInsnNode ifeq = new JumpInsnNode(Opcodes.IFEQ, gotoNode);

    injectings.add(new VarInsnNode(Opcodes.ALOAD, 1));
    injectings.add(hook);
    injectings.add(new VarInsnNode(Opcodes.ISTORE, 2));
    injectings.add(new VarInsnNode(Opcodes.ILOAD, 2));
    injectings.add(ifeq);
    injectings.add(returnNode);
    injectings.add(gotoNode);
    list.insert(injectings);

  }

}
