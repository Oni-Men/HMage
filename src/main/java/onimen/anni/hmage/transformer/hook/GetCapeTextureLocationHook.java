package onimen.anni.hmage.transformer.hook;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class GetCapeTextureLocationHook extends HookInjector {

  /*
   * Change vanill's code like this.
   *
   *  public ResourceLocation getLocationCape() {
   * +    ResourceLocation locationCape = HMageHooks.onGetLocationCape(this);
   * +    if(locationCape != null)
   * +       return locationCape;
   *      NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
   *      return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
   * }
   *
   * Method name might be
   *    getLocationCape
   *    func_110303_q
   *    q
   */

  public GetCapeTextureLocationHook() {
    super("net.minecraft.client.entity.AbstractClientPlayer", "()Lnf;", "getLocationCape", "q");
  }

  @Override
  public void injectHook(InsnList list) {
    InsnList injectings = new InsnList();

    MethodInsnNode hook = new MethodInsnNode(Opcodes.INVOKESTATIC, "onimen/anni/hmage/HMageHooks",
        "onGetLocationCape",
        "(Lbua;)Lnf;",
        false);

    InsnNode returnNode = new InsnNode(Opcodes.ARETURN);
    LabelNode gotoNode = new LabelNode();
    JumpInsnNode ifnull = new JumpInsnNode(Opcodes.IFNULL, gotoNode);

    injectings.add(new VarInsnNode(Opcodes.ALOAD, 0));
    injectings.add(hook);
    injectings.add(new VarInsnNode(Opcodes.ASTORE, 1));
    injectings.add(new VarInsnNode(Opcodes.ALOAD, 1));
    injectings.add(ifnull);
    injectings.add(new VarInsnNode(Opcodes.ALOAD, 1));
    injectings.add(returnNode);
    injectings.add(gotoNode);
    list.insert(injectings);
    System.out.println("Hook for locationCape was injected");
  }

}
