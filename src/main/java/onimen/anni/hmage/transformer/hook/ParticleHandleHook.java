package onimen.anni.hmage.transformer.hook;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import onimen.anni.hmage.transformer.HookInjector;
import onimen.anni.hmage.transformer.HookInjectorManager.ObfuscateType;

public class ParticleHandleHook extends HookInjector {

  /*
   * drawWorldBackground
   * func_146270_b
   * d_
   */
  public ParticleHandleHook() {
    super("net.minecraft.client.network.NetHandlerPlayClient");
    this.registerEntry(ObfuscateType.DEOBF, "handleParticles", "(Ljg;)V");
    this.registerEntry(ObfuscateType.DEOBF, "a", "(Ljg;)V");
  }

  @Override
  public boolean injectHook(InsnList list, ObfuscateType type) {
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

    return true;
  }

}
