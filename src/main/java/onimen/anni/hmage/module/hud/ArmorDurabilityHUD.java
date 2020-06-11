package onimen.anni.hmage.module.hud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import onimen.anni.hmage.module.hud.layout.Layout;

public class ArmorDurabilityHUD extends AbstractHUD {

  private static final List<ItemStack> armorListForLayoutMode = new ArrayList<>();

  private List<ItemStack> armorList = Lists.newArrayList();

  public ArmorDurabilityHUD() {
    armorListForLayoutMode.add(new ItemStack(Item.getItemById(276)));
    armorListForLayoutMode.add(new ItemStack(Item.getItemById(360)));
    armorListForLayoutMode.add(new ItemStack(Item.getItemById(313)));
    armorListForLayoutMode.add(new ItemStack(Item.getItemById(312)));
    armorListForLayoutMode.add(new ItemStack(Item.getItemById(311)));
    armorListForLayoutMode.add(new ItemStack(Item.getItemById(310)));
  }

  @Override
  public String getId() {
    return "hmage.module.hud.equipment-info";
  }

  @Override
  public Layout getDefaultLayout() {
    return Layout.getLayout().top().left();
  }

  @Override
  public int getDefaultX() {
    return 0;
  }

  @Override
  public int getDefaultY() {
    return 0;
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

  @Override
  public int getWidth() {
    if (widthHashCode != armorList.hashCode()) {
      int width = 0;
      for (ItemStack armor : armorList) {
        if (Item.getIdFromItem(armor.getItem()) == 0)
          continue;

        int durabilityTextWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(getTextForItemStack(armor));

        if (isHorizontal()) {
          width += 20 + durabilityTextWidth;
        } else {
          width = Math.max(width, 20 + durabilityTextWidth);
        }
      }
      cachedWidth = width;
      widthHashCode = armorList.hashCode();
    }
    return cachedWidth;
  }

  @Override
  public int getHeight() {
    if (isHorizontal()) { return 20; }
    if (heightHashCode != armorList.hashCode()) {
      cachedHeight = armorList.stream()
          .filter(armor -> Item.getIdFromItem(armor.getItem()) != 0)
          .collect(Collectors.toList())
          .size() * (20);
      heightHashCode = armorList.hashCode();
    }
    return cachedHeight;
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
  public void drawItem(Minecraft mc, boolean layoutMode) {

    ScaledResolution sr = new ScaledResolution(mc);
    EntityPlayerSP player = mc.player;
    FontRenderer fontRenderer = mc.fontRenderer;

    armorList.clear();
    if (layoutMode) {
      armorList.addAll(armorListForLayoutMode);
    } else {
      //手持ちのアイテム
      armorList.add(player.getHeldItem(EnumHand.OFF_HAND));
      armorList.add(player.getHeldItem(EnumHand.MAIN_HAND));
      //装備
      Iterator<ItemStack> iterator = player.getArmorInventoryList().iterator();
      while (iterator.hasNext()) {
        armorList.add(iterator.next());
      }
    }

    Collections.reverse(armorList);
    Layout layout = getLayout();

    int x = getComputedX(sr);
    int y = getComputedY(sr);

    for (ItemStack armor : armorList) {

      if (Item.getIdFromItem(armor.getItem()) == 0)
        continue;

      int color = getDurabilityColor(armor);
      String text = getTextForItemStack(armor);
      int stringWidth = fontRenderer.getStringWidth(text);

      if (layout.isRight() && !layout.isHorizontal()) {
        drawLightedItem(mc, armor, x + stringWidth + 2, y + 2);
        mc.fontRenderer.drawString(text, x, y + 10 - mc.fontRenderer.FONT_HEIGHT / 2, color);
      } else {
        drawLightedItem(mc, armor, x + 2, y + 2);
        mc.fontRenderer.drawString(text, x + 20, y + 10 - mc.fontRenderer.FONT_HEIGHT / 2, color);
      }

      if (layout.isHorizontal()) {
        x += 20 + stringWidth;
      } else {
        y += 20;
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
