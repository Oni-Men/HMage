package onimen.anni.hmage.transformer.hook;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public abstract class HookInjector {

  public final String owner, methodName, methodDesc;

  public HookInjector(String owner, String methodName, String methodDesc) {
    this.owner = owner;
    this.methodName = methodName;
    this.methodDesc = methodDesc;
  }

  public final void injectHook(InsnList list) {

    InsnList injectInsn = getInjectInsn();

    if (injectInsn == null)
      return;

    AbstractInsnNode location = getLocation(list);

    if (location != null) {
      list.insert(location, injectInsn);
    } else {
      list.insert(injectInsn);
    }
  }

  public AbstractInsnNode getLocation(InsnList list) {
    return null;
  }

  abstract public InsnList getInjectInsn();
}
