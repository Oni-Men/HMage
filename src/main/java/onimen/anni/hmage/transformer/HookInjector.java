package onimen.anni.hmage.transformer;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.tree.InsnList;

import onimen.anni.hmage.transformer.HookInjectorManager.ObfuscateType;

public abstract class HookInjector {

  static class MethodIdentifier {
    public final String methodName;
    public final String methodDesc;

    public MethodIdentifier(String methodName, String methodDesc) {
      this.methodName = methodName;
      this.methodDesc = methodDesc;
    }
  }

  public final String owner;
  private final Map<ObfuscateType, MethodIdentifier> map;

  public HookInjector(String owner) {
    this.map = new HashMap<>();
    this.owner = owner;
  }

  public void registerEntry(ObfuscateType type, String methodName, String methodDesc) {
    map.put(type, new MethodIdentifier(methodName, methodDesc));
  }

  public MethodIdentifier getEntry(ObfuscateType type) {
    return map.get(type);
  }

  public abstract boolean injectHook(InsnList list, ObfuscateType type);

}
