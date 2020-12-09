package onimen.anni.hmage.transformer.hook;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class GetCharWidthHook extends HookInjector {

  public GetCharWidthHook() {
    super("net.minecraft.client.gui.FontRenderer", "(C)I", "getCharWidth", "a");
  }

  @Override
  public void injectHook(InsnList list) {
    InsnList injectings = new InsnList();

    String descriptor = "(C)Lonimen/anni/hmage/event/GetCharWidthEvent;";
    MethodInsnNode onGetCharWith = new MethodInsnNode(Opcodes.INVOKESTATIC, "onimen/anni/hmage/HMageHooks",
        "onGetCharWidth", descriptor, false);

    MethodInsnNode isCanceled = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "onimen/anni/hmage/event/GetCharWidthEvent",
        "isCanceled", "()Z", false);

    MethodInsnNode getWidth = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "onimen/anni/hmage/event/GetCharWidthEvent",
        "getWidth", "()I", false);

    LabelNode endOfInjection = new LabelNode();

    injectings.add(new VarInsnNode(Opcodes.ILOAD, 1));
    injectings.add(onGetCharWith);
    injectings.add(new VarInsnNode(Opcodes.ASTORE, 3)); //LocalVariableTable Index
    injectings.add(new VarInsnNode(Opcodes.ALOAD, 3));
    injectings.add(isCanceled);
    injectings.add(new JumpInsnNode(Opcodes.IFEQ, endOfInjection));
    injectings.add(new VarInsnNode(Opcodes.ALOAD, 3));
    injectings.add(getWidth);
    injectings.add(new InsnNode(Opcodes.IRETURN));
    injectings.add(endOfInjection);

    ListIterator<AbstractInsnNode> itr = list.iterator();
    AbstractInsnNode next;
    while (itr.hasNext()) {
      next = itr.next();

      if (next.getOpcode() != Opcodes.ISTORE)
        continue;

      if (!(next instanceof VarInsnNode))
        continue;

      if (((VarInsnNode) next).var == 2) {
        list.insert(next, injectings);
        break;
      }
    }
  }

}
