package onimen.anni.hmage.transformer.hook;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import onimen.anni.hmage.transformer.HookInjector;
import onimen.anni.hmage.transformer.HookInjectorManager.ObfuscateType;

public class RenderScoreBoardHook extends HookInjector {

  public RenderScoreBoardHook() {
    super("net.minecraft.client.gui.GuiIngame");
    registerEntry(ObfuscateType.DEOBF, "renderScoreboard",
        "(Lnet/minecraft/scoreboard/ScoreObjective;Lnet/minecraft/client/gui/ScaledResolution;)V");
    registerEntry(ObfuscateType.OBF, "a", "(Lbhg;Lbit;)V");
  }

  @Override
  public boolean injectHook(InsnList list, ObfuscateType type) {
    ListIterator<AbstractInsnNode> iterator = list.iterator();
    AbstractInsnNode current, next = iterator.next();
    int timeReplaced = 0;
    while (iterator.hasNext()) {
      current = next;
      next = iterator.next();

      if (current.getOpcode() != Opcodes.LDC)
        continue;

      if (next.getOpcode() != Opcodes.INVOKEVIRTUAL)
        continue;

      if (!(next instanceof MethodInsnNode))
        continue;

      MethodInsnNode method = (MethodInsnNode) next;

      if (!method.owner.equals(type == ObfuscateType.DEOBF ? "net/minecraft/client/gui/FontRenderer" : "bip"))
        continue;

      if (!method.name.equals(type == ObfuscateType.DEOBF ? "drawString" : "a"))
        continue;

      if (method.desc.equals("(Ljava/lang/String;III)I")) {
        list.set(current, new LdcInsnNode(0xFFFFFFFF));
        timeReplaced++;
      }

    }

    return timeReplaced == 3;
  }

}
