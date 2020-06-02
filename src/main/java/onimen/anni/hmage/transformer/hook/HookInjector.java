package onimen.anni.hmage.transformer.hook;

import org.objectweb.asm.tree.InsnList;

public abstract class HookInjector {

  public final String owner, methodName, methodDesc;

  public HookInjector(String owner, String methodName, String methodDesc) {
    this.owner = owner;
    this.methodName = methodName;
    this.methodDesc = methodDesc;
  }

  public abstract void injectHook(InsnList list);

}
