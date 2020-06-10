package onimen.anni.hmage.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.observer.killeffect.AnniKillEffect;
import onimen.anni.hmage.observer.killeffect.AnniKillEffectList;
import onimen.anni.hmage.observer.killeffect.AnniKillEffectManager;

@SideOnly(Side.CLIENT)
public class AnniKillEffectSetting extends GuiScreen {
  /** The List GuiSlot object reference. */
  private AnniKillEffectSetting.List list;

  private AnniKillEffect selected;

  public AnniKillEffectSetting() {
    selected = AnniKillEffectManager.getInstance().getUseKillEffect();
  }

  /**
   * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the window resizes, the buttonList is cleared beforehand.
   */
  @Override
  public void initGui() {
    this.list = new AnniKillEffectSetting.List(this.mc);
    this.list.registerScrollButtons(7, 8);
    addButton(new GuiButton(6, this.width / 2 - 100, this.height - 38, I18n.format("gui.done")));
  }

  /**
   * Handles mouse input.
   */
  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    this.list.handleMouseInput();
  }

  /**
   * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
   */
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.enabled) {
      switch (button.id) {
      case 5:
        break;
      case 6:
        AnniKillEffectManager.getInstance().setKillEffect(selected);
        this.mc.displayGuiScreen((GuiScreen) null);
        this.mc.setIngameFocus();
        break;
      default:
        this.list.actionPerformed(button);
      }
    }
  }

  /**
   * Draws the screen and all the components in it.
   */
  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.list.drawScreen(mouseX, mouseY, partialTicks);
    this.drawCenteredString(this.fontRenderer, "Select Kill Effect", this.width / 2, 16, 16777215);
    this.drawCenteredString(this.fontRenderer, "Annihilation中のみ適用されます。Annihilationで以下のいずれかのキルエフェクトを選択している必要があります。",
        this.width / 2, this.height - 68, 8421504);
    this.drawCenteredString(this.fontRenderer, "(Bloody Mees, Confetti, Flame)", this.width / 2,
        this.height - 56, 8421504);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @SideOnly(Side.CLIENT)
  class List extends GuiSlot {
    private final AnniKillEffect[] anniKillEffects = AnniKillEffectList.getAllKillEffect()
        .toArray(new AnniKillEffect[0]);

    public List(Minecraft mcIn) {
      super(mcIn, AnniKillEffectSetting.this.width, AnniKillEffectSetting.this.height, 32,
          AnniKillEffectSetting.this.height - 85 + 4, 18);
    }

    @Override
    protected int getSize() {
      return this.anniKillEffects.length;
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
      selected = anniKillEffects[slotIndex];
    }

    @Override
    protected boolean isSelected(int slotIndex) {
      if (selected == null && slotIndex == 0) { return true; }
      return anniKillEffects[slotIndex] == selected;
    }

    @Override
    protected int getContentHeight() {
      return this.getSize() * 18;
    }

    @Override
    protected void drawBackground() {
      AnniKillEffectSetting.this.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn,
        float partialTicks) {
      AnniKillEffectSetting.this.fontRenderer.setBidiFlag(true);
      AnniKillEffectSetting.this.drawCenteredString(AnniKillEffectSetting.this.fontRenderer,
          anniKillEffects[slotIndex].getName(), this.width / 2, yPos + 1,
          16777215);
    }
  }
}