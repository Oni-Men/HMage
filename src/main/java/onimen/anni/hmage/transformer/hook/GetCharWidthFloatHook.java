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

import onimen.anni.hmage.transformer.HookInjector;
import onimen.anni.hmage.transformer.HookInjectorManager.ObfuscateType;

public class GetCharWidthFloatHook extends HookInjector {

  public GetCharWidthFloatHook() {
    super("net.minecraft.client.gui.FontRenderer");
    this.registerEntry(ObfuscateType.DEOBF, "getCharWidthFloat", "(C)F");
  }

  @Override
  public boolean injectHook(InsnList list, ObfuscateType type) {
    InsnList injectings = new InsnList();

    String descriptor = "(C)Lonimen/anni/hmage/event/GetCharWidthEvent;";
    MethodInsnNode onGetCharWith = new MethodInsnNode(Opcodes.INVOKESTATIC, "onimen/anni/hmage/HMageHooks",
        "onGetCharWidth", descriptor, false);

    MethodInsnNode isCanceled = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "onimen/anni/hmage/event/GetCharWidthEvent",
        "isCanceled", "()Z", false);

    MethodInsnNode getWidthFloat = new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
        "onimen/anni/hmage/event/GetCharWidthEvent",
        "getWidthFloat", "()F", false);

    LabelNode jumpTo = new LabelNode();

    injectings.add(new VarInsnNode(Opcodes.ILOAD, 1));
    injectings.add(onGetCharWith);
    injectings.add(new VarInsnNode(Opcodes.ASTORE, 3)); //LocalVariableTable Index
    injectings.add(new VarInsnNode(Opcodes.ALOAD, 3));
    injectings.add(isCanceled);
    injectings.add(new JumpInsnNode(Opcodes.IFEQ, jumpTo));
    injectings.add(new VarInsnNode(Opcodes.ALOAD, 3));
    injectings.add(getWidthFloat);
    injectings.add(new InsnNode(Opcodes.FRETURN));
    injectings.add(jumpTo);

    ListIterator<AbstractInsnNode> itr = list.iterator();
    AbstractInsnNode next;
    while (itr.hasNext()) {
      next = itr.next();
      //Find Vanilla's inject position
      if (next.getOpcode() != Opcodes.ISTORE)
        continue;

      if (!(next instanceof VarInsnNode))
        continue;

      if (((VarInsnNode) next).var == 2) {
        list.insert(next, injectings);
        return true;
      }
    }
    return false;
  }

}
