package onimen.anni.hmage.transformer;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class RedTintTransformer implements IClassTransformer {

	private static final String LAYER_ARMOR_BASE = "net.minecraft.client.renderer.entity.layers.LayerArmorBase";
	private static final String RENDER_ARMOR_LAYER = "renderArmorLayer";
	private static final String RENDER_ARMOR_LAYER_OBF = "func_188361_a";

	private static final String RED_TINT_CLASS = "onimen/anni/hmage/transformer/RedTintArmor";
	private static final String METHOD_NAME_SET = "set";
	private static final String METHOD_NAME_UNSET = "unset";
	private static final String METHOD_DESC_SET = "(Lnet/minecraft/entity/EntityLivingBase;)V";
	private static final String METHOD_DESC_UNSET = "()V";

	private static final int INSERT_IDX_SET = 131;
	private static final int INSERT_IDX_UNSET = 140;

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {

		if (transformedName.equals(LAYER_ARMOR_BASE)) {

			System.out.println("HMage is going to transform " + transformedName);

			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(basicClass);

			classReader.accept(classNode, 0);

			Iterator<MethodNode> methods = classNode.methods.iterator();

			while (methods.hasNext()) {
				MethodNode m = methods.next();

				if (m.name.equals(RENDER_ARMOR_LAYER) || m.name.equals(RENDER_ARMOR_LAYER_OBF)) {
					System.out.println("Finish looking up and modify method: " + m.name);

					AbstractInsnNode insnBeforeSet = m.instructions.get(INSERT_IDX_SET);
					AbstractInsnNode insnBeforeUnset = m.instructions.get(INSERT_IDX_UNSET);

					MethodInsnNode insnSet = new MethodInsnNode(Opcodes.INVOKESTATIC, RED_TINT_CLASS, METHOD_NAME_SET,
							METHOD_DESC_SET, false);

					MethodInsnNode insnUnset = new MethodInsnNode(Opcodes.INVOKESTATIC, RED_TINT_CLASS,
							METHOD_NAME_UNSET, METHOD_DESC_UNSET,
							false);

					m.instructions.insert(insnBeforeSet, insnSet);
					m.instructions.insertBefore(insnSet, new VarInsnNode(Opcodes.ALOAD, 1));

					m.instructions.insert(insnBeforeUnset, insnUnset);

					break;
				}
			}

			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			classNode.accept(classWriter);
			return classWriter.toByteArray();
		}

		return basicClass;
	}

}
