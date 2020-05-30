package onimen.anni.hmage.gui;

import java.io.IOException;
import java.util.Random;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import onimen.anni.hmage.util.JavaUtil;

public class AimGameGui extends GuiScreen {

  private int BACKGROUND_SIZE = 300;

  private int targetX;

  private int targetY;

  private GamePhase phase = GamePhase.START;

  private ResultData point = new ResultData();

  private static ResultData hightScore = new ResultData();

  private Random rand = new Random();

  private int inSize = 20;

  private int outSize = 60;

  private long startTime;

  public AimGameGui() {
    ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
    inSize /= resolution.getScaleFactor();
    outSize /= resolution.getScaleFactor();
    BACKGROUND_SIZE /= resolution.getScaleFactor();

    showStartGui();
  }

  protected void showStartGui() {
    phase = GamePhase.START;
    ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
    targetX = resolution.getScaledWidth() / 2;
    targetY = resolution.getScaledHeight() / 2;
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);

    //制限時間を確認
    if (phase == GamePhase.IN_GAME) {
      if (startTime + 30 * 1000 < System.currentTimeMillis()) {
        phase = GamePhase.RESULT;
        if (point.point >= hightScore.point) {
          hightScore = point;
        }
        addButton(new GuiButton(1, this.width / 2 - 50, 16 * 20, 100, 20, "BACK"));
      }
    }

    drawBackGrund();
    if (phase == GamePhase.RESULT) {
      showResult();
      return;
    }

    this.buttonList.clear();
    this.drawCenteredString(this.fontRenderer, ChatFormatting.GOLD + "- AIM GAME - POINT: " + point.point,
        this.width / 2, 16, 0xffffff);

    if (phase == GamePhase.START) {
      this.drawCenteredString(this.fontRenderer, ChatFormatting.RED + "↓ Click To Start ↓",
          this.width / 2, targetY - outSize - 15, 0xffffff);
      drawTargetRect();
    } else if (phase == GamePhase.IN_GAME) {
      drawTargetRect();
      this.drawCenteredString(this.fontRenderer,
          ChatFormatting.BLACK.toString() + JavaUtil.round(30 - (System.currentTimeMillis() - startTime) / 1000.0, 2)
              + " sec",
          this.width / 2, this.height / 2 - 16, 0xffffff);
    }
  }

  private void showResult() {
    this.drawCenteredString(this.fontRenderer, ChatFormatting.GOLD + "=========NOW RESULT=========", this.width / 2,
        16, 0xffffff);
    if (hightScore == point) {
      this.drawCenteredString(this.fontRenderer,
          ChatFormatting.GREEN + "point : " + point.point + ChatFormatting.RED + "   →NEW RECORD!!←", this.width / 2,
          16 * 2, 0xffffff);
    } else {
      this.drawCenteredString(this.fontRenderer, ChatFormatting.GREEN + "point : " + point.point, this.width / 2,
          16 * 2, 0xffffff);
    }
    this.drawCenteredString(this.fontRenderer, ChatFormatting.GREEN + "CriticalHit : " + point.criticalHitCount
        + " (" + JavaUtil.round(point.criticalHitCount * 100.0 / point.totalCount(), 2) + "%)", this.width / 2,
        16 * 3, 0xffffff);
    this.drawCenteredString(this.fontRenderer, ChatFormatting.GREEN + "Hit : " + point.hitCount
        + " (" + JavaUtil.round(point.hitCount * 100.0 / point.totalCount(), 2) + "%)", this.width / 2,
        16 * 4, 0xffffff);
    this.drawCenteredString(this.fontRenderer, ChatFormatting.GREEN + "Miss : " + point.missCount
        + " (" + JavaUtil.round(point.missCount * 100.0 / point.totalCount(), 2) + "%)", this.width / 2,
        16 * 5, 0xffffff);

    this.drawCenteredString(this.fontRenderer, ChatFormatting.GOLD + "=========HIGHT SCORE=========", this.width / 2,
        16 * 9, 0xffffff);
    this.drawCenteredString(this.fontRenderer, ChatFormatting.GREEN + "point : " + hightScore.point, this.width / 2,
        16 * 10, 0xffffff);
    this.drawCenteredString(this.fontRenderer, ChatFormatting.GREEN + "CriticalHit : " + hightScore.criticalHitCount
        + " (" + JavaUtil.round(point.criticalHitCount * 100.0 / point.totalCount(), 2) + "%)", this.width / 2,
        16 * 11, 0xffffff);
    this.drawCenteredString(this.fontRenderer, ChatFormatting.GREEN + "Hit : " + hightScore.hitCount
        + " (" + JavaUtil.round(point.hitCount * 100.0 / point.totalCount(), 2) + "%)", this.width / 2,
        16 * 12, 0xffffff);
    this.drawCenteredString(this.fontRenderer, ChatFormatting.GREEN + "Miss : " + hightScore.missCount
        + " (" + JavaUtil.round(point.missCount * 100.0 / point.totalCount(), 2) + "%)", this.width / 2,
        16 * 13, 0xffffff);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 1) {
      showStartGui();
    }
  }

  public static void drawRect(int left, int top, int right, int bottom, int colorRed, int colorGreen,
      int colorBlue, float colorAlpha) {
    if (left < right) {
      int i = left;
      left = right;
      right = i;
    }

    if (top < bottom) {
      int j = top;
      top = bottom;
      bottom = j;
    }

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    GlStateManager.enableBlend();
    GlStateManager.disableTexture2D();
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.color(colorRed / 255.0f, colorGreen / 255.0f, colorBlue / 255.0f, colorAlpha);
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
    bufferbuilder.pos(left, bottom, 0.0D).endVertex();
    bufferbuilder.pos(right, bottom, 0.0D).endVertex();
    bufferbuilder.pos(right, top, 0.0D).endVertex();
    bufferbuilder.pos(left, top, 0.0D).endVertex();
    tessellator.draw();
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
  }

  /**
   * 的を描画する。
   */
  private void drawTargetRect() {
    drawRect(targetX - outSize, targetY + outSize, targetX + outSize, targetY - outSize,
        128, 0, 0, 0.8f);
    drawRect(targetX - inSize, targetY + inSize, targetX + inSize, targetY - inSize,
        254, 0, 0, 0.8f);
  }

  /**
   * 背景を描画する。
   */
  private void drawBackGrund() {
    ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
    int centerX = resolution.getScaledWidth() / 2;
    int centerY = resolution.getScaledHeight() / 2;

    drawRect(centerX - BACKGROUND_SIZE, centerY + BACKGROUND_SIZE, centerX + BACKGROUND_SIZE, centerY - BACKGROUND_SIZE,
        230, 230, 230, 0.3f);

    //タイマー
    if (phase == GamePhase.IN_GAME) {
      double timeParcent = (System.currentTimeMillis() - startTime) / (30.0 * 1000);
      int radius = (int) (BACKGROUND_SIZE * timeParcent);
      drawRect(centerX - radius, centerY + radius, centerX + radius, centerY - radius,
          176, 196, 222, 0.3f);
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);

    //結果画面の場合は何もしない
    if (phase == GamePhase.RESULT) { return; }

    int distanceX = Math.abs(mouseX - targetX);
    int distanceY = Math.abs(mouseY - targetY);

    HitType hitType;
    if (distanceX < inSize && distanceY < inSize) {
      hitType = HitType.CRITICAL;
    } else if (distanceX < outSize && distanceY < outSize) {
      hitType = HitType.HIT;
    } else {
      hitType = HitType.MISS;
    }

    //開始前 かつ ミスの場合は何もしない
    if (phase == GamePhase.START && hitType == HitType.MISS) { return; }

    if (hitType == HitType.CRITICAL || hitType == HitType.HIT) {
      if (phase == GamePhase.START) {
        //開始画面の場合はゲームを開始
        initGame();
      }

      //的を移動させる
      ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
      int centerX = resolution.getScaledWidth() / 2;
      int centerY = resolution.getScaledHeight() / 2;

      targetX = random(centerX - BACKGROUND_SIZE + outSize, centerX + BACKGROUND_SIZE - outSize);
      targetY = random(centerY - BACKGROUND_SIZE + outSize, centerY + BACKGROUND_SIZE - outSize);
    }

    //ポイントを加算
    point.point += hitType.point;
    //クリック数をカウント
    point.count(hitType);

  }

  private int random(int start, int end) {
    return rand.nextInt(end - start) + start;
  }

  protected void initGame() {
    phase = GamePhase.IN_GAME;
    point = new ResultData();
    startTime = System.currentTimeMillis();
  }

  private enum GamePhase {
    START, IN_GAME, RESULT;
  }

  private enum HitType {
    HIT(1), CRITICAL(2), MISS(-2);

    private int point;

    private HitType(int point) {
      this.point = point;
    }
  }

  private static class ResultData {
    int point = 0;

    int hitCount = 0;

    int criticalHitCount = 0;

    int missCount = 0;

    public void count(HitType hitType) {
      switch (hitType) {
      case HIT:
        hitCount++;
        break;
      case CRITICAL:
        criticalHitCount++;
        break;
      case MISS:
        missCount++;
        break;
      }
    }

    public int totalCount() {
      return hitCount + criticalHitCount + missCount;
    }
  }
}
