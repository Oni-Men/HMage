package onimen.anni.hmage.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiScreenUtils {

  private static final String GRAY = ChatFormatting.GRAY.toString();
  private static final String BLACK = ChatFormatting.BLACK.toString();
  public static final String SELEC_SERVER = MessageFormat.format("{0}|{1}Select Server{0}", GRAY, BLACK);


  public static ITextComponent getChestDisplayName(GuiChest chest) {

    for (Field field : chest.getClass().getDeclaredFields()) {
      try {

        int mod = field.getModifiers();

        if (!Modifier.isPrivate(mod) || !Modifier.isFinal(mod)) {
          continue;
        }

        if (!IInventory.class.isAssignableFrom(field.getType())) {
          continue;
        }

        field.setAccessible(true);
        IInventory inv = (IInventory) field.get(chest);

        if (inv instanceof InventoryPlayer) {
          continue;
        }

        System.out.println(inv.getDisplayName().getUnformattedText());

        return inv.getDisplayName();
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

}
