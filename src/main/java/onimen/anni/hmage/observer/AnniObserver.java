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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo.Color;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.HMageDiscordHandler;
import onimen.anni.hmage.observer.config.AnniConfigKey;
import onimen.anni.hmage.observer.config.AnniConfiguration;
import onimen.anni.hmage.observer.data.AnniTeamColor;
import onimen.anni.hmage.observer.data.GameInfo;
import onimen.anni.hmage.util.ShotbowUtils;

public class AnniObserver {

  private static final String MAP_PREFIX = TextFormatting.GOLD.toString() + TextFormatting.BOLD.toString() + "Map: ";

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
    HMageDiscordHandler.INSTANCE.updatePresenceWithGameInfo(gameInfo);
  }

  public void onLeaveGame() {
    this.tickLeftWhileNoAnniScoreboard = 0;

    if (gameInfo.getGamePhase().getValue() > 0 && gameInfo.getMeTeamColor() != AnniTeamColor.NO_JOIN) {
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
    HMageDiscordHandler.INSTANCE.clearPresence();
  }

  public void updatePresence() {
    HMageDiscordHandler.INSTANCE.updatePresenceWithGameInfo(gameInfo);
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

      AnniTeamColor previousTeamColor = gameInfo.getMeTeamColor();
      AnniTeamColor nextTeamColor = AnniTeamColor.NO_JOIN;

      ScorePlayerTeam team = scoreboard.getPlayersTeam(gameInfo.getMePlayerData().getPlayerName());

      if (team != null) {
        nextTeamColor = AnniTeamColor.findByTeamName(team.getDisplayName().replaceFirst("§.", ""));
      }

      if (previousTeamColor != nextTeamColor) {
        gameInfo.getMePlayerData().setTeamColor(nextTeamColor);
        updatePresence();
      }

    } else {
      tickLeftWhileNoAnniScoreboard++;
      if (tickLeftWhileNoAnniScoreboard > 100) {
        HMage.anniObserverMap.unsetAnniObserver();
        return;
      }
    }

    GamePhase previousPhase = this.gameInfo.getGamePhase(), nextPhase = GamePhase.UNKNOWN;
    //フェーズを取得
    if (bossInfoMap != null) {
      for (BossInfoClient bossInfo : bossInfoMap.values()) {
        //フェーズを表示するボスバーは青色なので
        if (bossInfo.getColor() == Color.BLUE) {
          String name = bossInfo.getName().getUnformattedText();
          nextPhase = GamePhase.getGamePhasebyText(name);
          break;
        }
      }
    }
    if (previousPhase == null || previousPhase.getValue() != nextPhase.getValue()) {
      this.gameInfo.setGamePhase(nextPhase);
      updatePresence();
    }

    //Mapを取得
    if (gameInfo.getMapName() == null && scoreboard != null) {
      String previousMapName = gameInfo.getMapName();
      String nextMapName = getMapFromScoreboard(scoreboard);
      if (previousMapName == null || !previousMapName.equals(nextMapName)) {
        gameInfo.setMapName(nextMapName);
        updatePresence();
      }
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

    //試合中かどうかを判定
    if (displayName.equals(AnniConfiguration.getConfig(AnniConfigKey.ANNI_SCOREBOARD))) { return true; }

    return false;
  }

  private String getMapFromScoreboard(Scoreboard scoreboard) {
    ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(1);

    if (scoreobjective == null) { return null; }

    for(String teamName : scoreboard.getTeamNames()) {
      if (!teamName.contains(MAP_PREFIX)) { 
        continue;
      }
      
      return teamName.replace(MAP_PREFIX, "");
    }
    return null;
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
