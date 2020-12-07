package onimen.anni.hmage.transformer.hook;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class FontRendererHook extends HookInjector {

  //net.minecraft.client.gui.FontRenderer is "bip.class" in obfuscated
  public FontRendererHook() {
    super("net.minecraft.client.gui.FontRenderer", "(CZ)F", "renderChar", "func_181559_a", "a");
  }

  //  private float renderChar(char ch, boolean italic)
  //  {
  //      if (ch == 160) return 4.0F; // forge: display nbsp as space. MC-2595
  //      if (ch == ' ')
  //      {
  //          return 4.0F;
  //      }
  //      else
  //      {
  //          int i = "Chars in range of unicode 0-256".indexOf(ch);
  //          RenderFontEvent event = HMageHooks.onRenderFont(ch, italic);
  //          if (event.isCancelled())
  //          {
  //              return event.getWidth();
  //          }
  //          return i != -1 && !this.unicodeFlag ? this.renderDefaultChar(i, italic) : this.renderUnicodeChar(ch, italic);
  //      }
  //  }

  @Override
  public void injectHook(InsnList list) {
    InsnList injectings = new InsnList();

    String descriptor = "(CZFF)Lonimen/anni/hmage/event/RenderFontEvent;";
    MethodInsnNode onRenderFont = new MethodInsnNode(Opcodes.INVOKESTATIC, "onimen/anni/hmage/HMageHooks",
        "onRenderFont", descriptor, false);

    MethodInsnNode isCancelled = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "onimen/anni/hmage/event/RenderFontEvent",
        "isCanceled", "()Z", false);

    MethodInsnNode getWidth = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "onimen/anni/hmage/event/RenderFontEvent",
        "getWidth", "()F", false);

    LabelNode endOfInjection = new LabelNode();

    injectings.add(new VarInsnNode(Opcodes.ILOAD, 1));
    injectings.add(new VarInsnNode(Opcodes.ILOAD, 2));
    injectings.add(new VarInsnNode(Opcodes.ALOAD, 0));
    injectings.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/FontRenderer", "posX", "F"));
    injectings.add(new VarInsnNode(Opcodes.ALOAD, 0));
    injectings.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/FontRenderer", "posY", "F"));
    injectings.add(onRenderFont);
    injectings.add(new VarInsnNode(Opcodes.ASTORE, 4));
    injectings.add(new VarInsnNode(Opcodes.ALOAD, 4));
    injectings.add(isCancelled);
    injectings.add(new JumpInsnNode(Opcodes.IFNE, endOfInjection));
    injectings.add(new VarInsnNode(Opcodes.ALOAD, 4));
    injectings.add(getWidth);
    injectings.add(new InsnNode(Opcodes.FRETURN));
    injectings.add(endOfInjection);

    ListIterator<AbstractInsnNode> iterator = list.iterator();
    AbstractInsnNode next;
    while (iterator.hasNext()) {
      next = iterator.next();
      if (next.getOpcode() != Opcodes.ISTORE)
        continue;

      if (!(next instanceof VarInsnNode))
        continue;

      VarInsnNode varInsnNode = (VarInsnNode) next;

      if (varInsnNode.getOpcode() == Opcodes.ISTORE && varInsnNode.var == 3) {
        list.insert(next, injectings);
        break;
      }
    }
  }

}
