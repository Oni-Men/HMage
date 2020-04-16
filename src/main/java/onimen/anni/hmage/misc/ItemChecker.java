package onimen.anni.hmage.misc;

import java.util.Iterator;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class ItemChecker {

	public static final String classItemText = ChatFormatting.GOLD + "Class Item";
	public static final String soulboundText = ChatFormatting.GOLD + "Soulbound";

	public static NBTTagList getTagListFromItemStack(ItemStack stack) {
		NBTTagCompound root = stack.getTagCompound();

		if (root == null)
			return null;

		NBTTagCompound display = root.getCompoundTag("display");

		if (display == null)
			return null;

		//8 is the number identifies if the Tag is NBTTagString or not.
		return display.getTagList("Lore", 8);
	}

	public static boolean hasRowInLore(NBTTagList lore, String row) {
		if (lore == null)
			return false;

		Iterator<NBTBase> iterator = lore.iterator();

		boolean flag = false;

		while (iterator.hasNext()) {
			NBTBase tag = iterator.next();
			if (tag.getId() == 8 && ((NBTTagString) tag).getString().equals(row))
				flag = true;
		}

		return flag;
	}

	public static boolean isClassItem(ItemStack stack) {
		NBTTagList lore = getTagListFromItemStack(stack);
		return hasRowInLore(lore, ItemChecker.classItemText);
	}

	public static boolean isSouldound(ItemStack stack) {
		NBTTagList lore = getTagListFromItemStack(stack);
		return hasRowInLore(lore, ItemChecker.soulboundText);
	}
}
