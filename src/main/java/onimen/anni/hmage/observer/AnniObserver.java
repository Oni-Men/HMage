package onimen.anni.hmage.observer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo.Color;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.util.ShotbowUtils;
import scala.collection.mutable.StringBuilder;

public class AnniObserver {

  private static final String MAP_PREFIX = ChatFormatting.GOLD.toString() + ChatFormatting.BOLD.toString() + "Map: ";
  private static final String VOTING_TEXT = ChatFormatting.GREEN + "/vote [map name] to vote";

  private ClassType usingClassType = ClassType.CIVILIAN;
  private GamePhase gamePhase = GamePhase.UNKNOWN;
  private int kills = 0;

  private Minecraft mc;
  private Map<UUID, BossInfoClient> bossInfoMap = null;

  private int tickLeftWhileNoScoreboard = 0;

  public AnniObserver(Minecraft mcIn) {
    this.mc = mcIn;
  }

  public ClassType getUsingClassType() {
    return usingClassType;
  }

  public GamePhase getGamePhase() {
    return gamePhase;
  }

  public int getKillCount() {
    return this.kills;
  }

  public void onJoinGame() {
    this.tickLeftWhileNoScoreboard = 0;
  }

  public void onLeaveGame() {
    this.tickLeftWhileNoScoreboard = 0;
  }

  @SideOnly(Side.CLIENT)
  public void onClientTick(ClientTickEvent event) {
    if (mc.ingameGUI != null) {

      if (this.bossInfoMap == null) {
        this.bossInfoMap = getBossInfoMap(mc.ingameGUI.getBossOverlay());
      }

      if (mc.world != null) {
        Scoreboard scoreboard = mc.world.getScoreboard();

        if (scoreboard != null && isAnniScoreboard(scoreboard)) {
          tickLeftWhileNoScoreboard = 0;
        } else {
          tickLeftWhileNoScoreboard++;
          if (tickLeftWhileNoScoreboard > 100) {
            HMage.anniObserverMap.unsetAnniObserver();
          }
        }
      }

      if (bossInfoMap != null) {
        if (!bossInfoMap.isEmpty()) {
          for (BossInfoClient bossInfo : bossInfoMap.values()) {
            if (bossInfo.getColor() == Color.BLUE) {
              String name = bossInfo.getName().getUnformattedText();
              gamePhase = GamePhase.getGamePhasebyText(name);
            }
          }
        }
      }
    }
  }

  public void onRecieveChat(ClientChatReceivedEvent event) {

    if (!ShotbowUtils.isShotbow(mc))
      return;

    ITextComponent message = event.getMessage();

    if (message.getUnformattedText().contentEquals(""))
      return;

    if (event.getType() == ChatType.SYSTEM && this.isClassSelect(message)) {
      ClassType classType = getClassType(message);

      if (classType != null) {
        this.usingClassType = classType;
      }
    }

    if (event.getType() == ChatType.CHAT && this.isKillLogOfPlayer(message, mc.player)) {
      kills++;
    }
  }

  /**
   * 渡されたメッセージがクラスを選択したときに受け取るメッセージかチェックします。
   *
   * @param message
   * @return
   */
  private boolean isClassSelect(ITextComponent message) {
    return message.getFormattedText().startsWith(ChatFormatting.GOLD + "[Class]");
  }

  /**
   * 渡されたメッセージから選択したクラスの種類を特定します。
   *
   * @param message
   * @return
   */
  private ClassType getClassType(ITextComponent message) {
    String[] split = message.getUnformattedText().split(" ");

    if (split.length != 3)
      return null;

    return ClassType.getClassTypeFromName(split[1]);
  }

  /**
   * メッセージがプレイヤーによるキルログであるかどうかをチェックします。
   *
   * @param message
   * @param player
   * @return
   */
  private boolean isKillLogOfPlayer(ITextComponent message, EntityPlayer player) {

    if (player == null) { return false; }

    String formattedName = removeTeamPrefix(player.getDisplayName().getFormattedText());

    if (formattedName != null && !message.getFormattedText().startsWith(formattedName)) { return false; }

    String[] split = message.getUnformattedText().split(" ");

    if (split.length < 3)
      return false;

    return split[1].contentEquals("killed") || split[1].contentEquals("shot");
  }

  /**
   * Remove team prefix of anni from given string. for example, [R]Onimen ---> Onimen
   *
   * @param formatted formatted string
   * @return
   */
  @Nullable
  private String removeTeamPrefix(String formatted) {

    final StringBuilder builder = new StringBuilder();
    final char prefix = ChatFormatting.PREFIX_CODE;
    final char[] chars = formatted.toCharArray();

    boolean insideBracket = false;

    for (int i = 0, len = chars.length; i < len; i++) {

      if (chars[i] == prefix) {
        if (i + 2 < len && chars[i + 1] == 'r') {
          if (chars[i + 2] == '[') {
            insideBracket = true;
            i += 2;
            continue;
          }
          if (chars[i + 2] == ']') {
            insideBracket = false;
            i += 2;
            continue;
          }
        }
      }

      if (!insideBracket)
        builder.append(chars[i]);
    }

    return builder.toString();
  }

  private boolean isAnniScoreboard(Scoreboard scoreboard) {
    ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(1);

    if (scoreobjective != null) {
      String displayName = scoreobjective.getDisplayName();

      if (displayName.contentEquals(VOTING_TEXT)) { return true; }

      for (int i = 0, len = Math.min(MAP_PREFIX.length(), displayName.length()); i < len; i++) {
        if (MAP_PREFIX.charAt(i) != displayName.charAt(i))
          return false;
      }

      return true;
    }
    return false;
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
