package onimen.anni.hmage.observer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo.Color;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.observer.data.GameInfo;
import onimen.anni.hmage.util.ShotbowUtils;

public class AnniObserver {

  private static final String MAP_PREFIX = ChatFormatting.GOLD.toString() + ChatFormatting.BOLD.toString() + "Map: ";

  private static final String VOTING_TEXT = ChatFormatting.GREEN + "/vote [map name] to vote";

  private Minecraft mc;
  private Map<UUID, BossInfoClient> bossInfoMap = null;

  private int tickLeftWhileNoAnniScoreboard = 0;

  @Nonnull
  private final GameInfo gameInfo;

  public AnniObserver(Minecraft mcIn) {
    this.mc = mcIn;
    this.gameInfo = new GameInfo();
  }

  public GameInfo getGameInfo() {
    return this.gameInfo;
  }

  public void onJoinGame() {
    this.tickLeftWhileNoAnniScoreboard = 0;
  }

  public void onLeaveGame() {
    this.tickLeftWhileNoAnniScoreboard = 0;
    //gameInfoを保存
    File historyDataDir = AnniObserverMap.getHistoryDataDir();
    Gson gson = new Gson();
    String json = gson.toJson(gameInfo);
    try {
      Files.write(json, new File(historyDataDir, gameInfo.getGameTimestamp() + ".txt"), StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
      //握りつぶす
    }

    //昔の情報を削除
    File[] listFiles = historyDataDir.listFiles(f -> f.getName().endsWith(".txt"));
    Arrays.stream(listFiles).sorted((f1, f2) -> f2.compareTo(f1)).skip(20).forEach(f -> f.delete());
  }

  @SideOnly(Side.CLIENT)
  public void onClientTick(ClientTickEvent event) {
    if (event.phase != Phase.END) { return; }

    //ゲーム中でないならリセット
    if (mc.ingameGUI == null) {
      HMage.anniObserverMap.unsetAnniObserver();
      return;
    }
    if (mc.world == null) {
      tickLeftWhileNoAnniScoreboard++;
      if (tickLeftWhileNoAnniScoreboard > 100) {
        HMage.anniObserverMap.unsetAnniObserver();
      }
      return;
    }

    //ボスゲージ取得
    if (this.bossInfoMap == null) {
      this.bossInfoMap = getBossInfoMap(mc.ingameGUI.getBossOverlay());
    }

    Scoreboard scoreboard = mc.world.getScoreboard();
    //Anniをプレイ中かどうか確認
    if (scoreboard != null && isAnniScoreboard(scoreboard)) {
      tickLeftWhileNoAnniScoreboard = 0;
    } else {
      tickLeftWhileNoAnniScoreboard++;
      if (tickLeftWhileNoAnniScoreboard > 100) {
        HMage.anniObserverMap.unsetAnniObserver();
        return;
      }
    }

    //フェーズを取得
    if (bossInfoMap != null) {
      for (BossInfoClient bossInfo : bossInfoMap.values()) {
        if (bossInfo.getColor() == Color.BLUE) {
          String name = bossInfo.getName().getUnformattedText();
          this.gameInfo.setGamePhase(GamePhase.getGamePhasebyText(name));
        }
      }
    }

    //Mapを取得
    if (gameInfo.getMapName() == null && scoreboard != null) {
      gameInfo.setMapName(getMapFromScoreboard(scoreboard));
    }
  }

  public void onRecieveChat(ClientChatReceivedEvent event) {

    if (!ShotbowUtils.isShotbow(mc))
      return;

    ITextComponent message = event.getMessage();

    if (message.getUnformattedText().isEmpty())
      return;

    //チャットを元に処理を実行
    AnniChatReciveExecutor.onReceiveChat(message, event.getType());
  }

  private boolean isAnniScoreboard(Scoreboard scoreboard) {
    ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(1);

    if (scoreobjective == null) { return false; }
    String displayName = scoreobjective.getDisplayName();

    //Voteの場合
    if (displayName.contentEquals(VOTING_TEXT)) { return true; }

    //試合中の場合
    if (displayName.contains(MAP_PREFIX)) { return true; }

    return false;
  }

  private String getMapFromScoreboard(Scoreboard scoreboard) {
    ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(1);

    if (scoreobjective == null) { return null; }
    String displayName = scoreobjective.getDisplayName();

    if (!displayName.contains(MAP_PREFIX)) { return null; }

    return displayName.replace(MAP_PREFIX, "");
  }

  @SuppressWarnings("unchecked")
  private Map<UUID, BossInfoClient> getBossInfoMap(GuiBossOverlay bossOverlay) {
    if (bossOverlay != null) {

      Field[] declaredFields = bossOverlay.getClass().getDeclaredFields();

      for (Field field : declaredFields) {

        int modifiers = field.getModifiers();

        if (!Modifier.isPrivate(modifiers) || !Modifier.isFinal(modifiers))
          continue;

        if (!Map.class.isAssignableFrom(field.getType()))
          continue;

        try {
          field.setAccessible(true);
          return (Map<UUID, BossInfoClient>) field.get(bossOverlay);
        } catch (IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

}
