package onimen.anni.hmage.transformer.hook;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import onimen.anni.hmage.transformer.HookInjector;
import onimen.anni.hmage.transformer.HookInjectorManager.ObfuscateType;

public class EntityMetadataHandleHook extends HookInjector {

  public EntityMetadataHandleHook() {
    super("net.minecraft.client.network.NetHandlerPlayClient");
    this.registerEntry(ObfuscateType.DEOBF, "handleEntityMetadata",
        "(Lnet/minecraft/network/play/server/SPacketEntityMetadata;)V");
    this.registerEntry(ObfuscateType.OBF, "a", "(Lkd;)V");
  }

  @Override
  public boolean injectHook(InsnList list, ObfuscateType type) {
    InsnList injectings = new InsnList();

    String entityMetadata = type == ObfuscateType.OBF ? "Lkd;"
        : "Lnet/minecraft/network/play/server/SPacketEntityMetadata;";

    MethodInsnNode onHandleEntityMetadata = new MethodInsnNode(Opcodes.INVOKESTATIC, "onimen/anni/hmage/HMageHooks",
        "onHandleEntityMetadata", "(" + entityMetadata + ")V", false);

    injectings.add(new VarInsnNode(Opcodes.ALOAD, 1));
    injectings.add(onHandleEntityMetadata);

    list.insert(injectings);
    return true;
  }

}
