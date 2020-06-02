package onimen.anni.hmage.transformer.hook;

import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.tree.InsnList;

public abstract class HookInjector {

  public final String owner, methodDesc;
  public final List<String> methodNames;

  public HookInjector(String owner, String methodDesc, String... methodNames) {
    this.owner = owner;
    this.methodDesc = methodDesc;
    this.methodNames = Arrays.asList(methodNames);
  }

  public abstract void injectHook(InsnList list);

}
