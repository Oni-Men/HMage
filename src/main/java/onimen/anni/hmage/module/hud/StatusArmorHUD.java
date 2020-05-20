package onimen.anni.hmage.module.hud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.util.PositionHelper;
import onimen.anni.hmage.util.PositionHelper.Position;

public class StatusArmorHUD extends Gui implements InterfaceHUD {

  private static final int SPACE = 1;

  @Override
  public String getPrefKey() {
    return "statusArmorHUD";
  }

  @Override
  public boolean isEnabled() {
    return Preferences.statusArmorOption.isEnabled();
  }

  public String getTextForItemStack(ItemStack stack) {
    boolean itemStackDamageable = stack.isItemStackDamageable();
    if (itemStackDamageable) {
      return String.valueOf(stack.getMaxDamage() - stack.getItemDamage());

    } else if (stack.isStackable()) {
      return String.valueOf(stack.getCount());
    } else {
      return "";
    }
  }

  public int calculateWidth(FontRenderer fontRenderer, List<ItemStack> armorList, boolean isHorizontal) {
    int width = 0;

    for (ItemStack armor : armorList) {
      if (Item.getIdFromItem(armor.getItem()) == 0)
        continue;

      int durabilityTextWidth = fontRenderer.getStringWidth(getTextForItemStack(armor));
      if (isHorizontal) {
        width += 20 + durabilityTextWidth + SPACE;
      } else {
        width = Math.max(width, 20 + durabilityTextWidth);
      }
    }

    return width;
  }

  public int calculateHeight(List<ItemStack> armorList, boolean isHorizontal) {

    if (isHorizontal) { return 20; }

    return armorList.stream()
        .filter(armor -> Item.getIdFromItem(armor.getItem()) != 0)
        .collect(Collectors.toList())
        .size() * (20 + SPACE);

  }

  public int getDurabilityColor(ItemStack stack) {

    if (!stack.isItemStackDamageable())
      return 0xffffff;

    int durability = (int) (255 - 255 * ((float) stack.getItemDamage() / (float) stack.getMaxDamage()));
    int red = 0xff;
    int green = durability;
    int blue = (int) (durability * ((float) durability / 256));

    return (red << 16) | (green << 8) | blue;
  }

  @Override
  public void drawItem(Minecraft mc) {

    ScaledResolution sr = new ScaledResolution(mc);
    EntityPlayerSP player = mc.player;
    FontRenderer fontRenderer = mc.fontRenderer;

    List<ItemStack> armorList = new ArrayList<>();
    //手持ちのアイテム
    armorList.add(player.getHeldItem(EnumHand.OFF_HAND));
    armorList.add(player.getHeldItem(EnumHand.MAIN_HAND));
    //装備
    Iterator<ItemStack> iterator = player.getArmorInventoryList().iterator();
    while (iterator.hasNext()) {
      armorList.add(iterator.next());
    }

    Collections.reverse(armorList);

    Position position = new PositionHelper.Position(Preferences.statusArmorOption.getPosition());

    int x = Preferences.statusArmorOption.getTranslateX();
    int y = Preferences.statusArmorOption.getTranslateY();

    if (position.right) {
      x += sr.getScaledWidth() - calculateWidth(fontRenderer, armorList, position.isHorizontal()) - SPACE;
    } else {
      x += SPACE;
    }

    if (position.bottom) {
      y += sr.getScaledHeight() - calculateHeight(armorList, position.isHorizontal()) - SPACE;
    } else {
      y += SPACE;
    }

    for (ItemStack armor : armorList) {

      if (Item.getIdFromItem(armor.getItem()) == 0)
        continue;

      int color = getDurabilityColor(armor);
      String text = getTextForItemStack(armor);
      int stringWidth = fontRenderer.getStringWidth(text);

      if (position.right && !position.isHorizontal()) {
        drawLightedItem(mc, armor, x + stringWidth + 2, y + 2);
        this.drawString(mc.fontRenderer, text, x, y + 10 - mc.fontRenderer.FONT_HEIGHT / 2, color);
      } else {
        drawLightedItem(mc, armor, x + 2, y + 2);
        this.drawString(mc.fontRenderer, text, x + 20, y + 10 - mc.fontRenderer.FONT_HEIGHT / 2, color);
      }

      if (position.isHorizontal()) {
        x += 20 + stringWidth + SPACE;
      } else {
        y += 20 + SPACE;
      }

    }

    GlStateManager.disableRescaleNormal();
    GlStateManager.disableBlend();

  }

  private void drawLightedItem(Minecraft mc, ItemStack stack, int x, int y) {
    RenderHelper.enableGUIStandardItemLighting();
    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
    RenderHelper.disableStandardItemLighting();
  }
}
