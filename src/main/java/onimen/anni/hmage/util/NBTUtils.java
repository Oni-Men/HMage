package onimen.anni.hmage.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class NBTUtils {

  public static List<String> getLore(ItemStack stack) {
    if (stack == null)
      return null;

    NBTTagCompound root = stack.getTagCompound();

    if (root == null)
      return null;

    NBTTagCompound display = root.getCompoundTag("Display");

    if (display == null)
      return null;

    NBTTagList lore = display.getTagList("Lore", NBT.TAG_STRING);

    if (lore == null)
      return null;

    ArrayList<String> list = new ArrayList<String>();

    for (Iterator<NBTBase> itr = lore.iterator(); itr.hasNext();) {
      NBTBase tag = itr.next();

      list.add(tag.toString());
    }

    return list;
  }

}
