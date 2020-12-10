package onimen.anni.hmage.transformer.hook;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

//  private ResourceLocation getUnicodePageLocation(int page) {
//    if (UNICODE_PAGE_LOCATIONS[page] == null) {
//      UNICODE_PAGE_LOCATIONS[page] = HMageooks.onGetUnicodePageLocation(page,
//          new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", page)));
//    }
//    return UNICODE_PAGE_LOCATIONS[page];
//  }
public class LoadFontTextureHook extends HookInjector {

  private static final String RESOURCE_LOCATION = "Lnf;";//"Lnet/minecraft/util/ResourceLocation;";

  public LoadFontTextureHook() {
    super("net.minecraft.client.gui.FontRenderer", "(I)" + RESOURCE_LOCATION,
        "getUnicodePageLocation", "func_111271_a", "a");
  }

  @Override
  public void injectHook(InsnList list) {
    VarInsnNode iload_1 = new VarInsnNode(Opcodes.ILOAD, 1);
    String descriptor = String.format("(I%s)%s", RESOURCE_LOCATION, RESOURCE_LOCATION);
    MethodInsnNode onLoadFontTexture = new MethodInsnNode(Opcodes.INVOKESTATIC, "onimen/anni/hmage/HMageHooks",
        "onLoadFontTexture", descriptor, false);

    ListIterator<AbstractInsnNode> itr = list.iterator();
    AbstractInsnNode cur = itr.next();
    AbstractInsnNode next = itr.next();
    while (itr.hasNext()) {
      if (cur.getOpcode() == Opcodes.ILOAD) {
        if ((cur instanceof VarInsnNode) && ((VarInsnNode) cur).var == 1) {
          if (next.getOpcode() == Opcodes.NEW) {
            list.insert(cur, iload_1);
          }
        }
      } else if (cur.getOpcode() == Opcodes.INVOKESPECIAL) {
        if ((cur instanceof MethodInsnNode) && ((MethodInsnNode) cur).name.equals("<init>")) {
          list.insert(cur, onLoadFontTexture);
          break;
        }
      }
      cur = next;
      next = itr.next();
    }
  }

}
