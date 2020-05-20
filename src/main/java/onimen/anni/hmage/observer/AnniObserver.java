package onimen.anni.hmage.observer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.UUID;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import onimen.anni.hmage.util.ShotbowUtils;

public class AnniObserver {

  public enum ClassType {
    ACROBAT("Acrobat"),
    ALCHEMIST("Alchemist"),
    ARCHER("Archer"),
    ASSASSIN("Assassin"),
    BARD("Bard"),
    BERSEKER("Berserker"),
    BLOODMAGE("Bloodmage"),
    BUILDER("Builder"),
    CIVILIAN("Civilian"),
    DASHER("Dasher"),
    DEFENDER("Defender"),
    ENCHANTER("Enchanter"),
    ENGINEER("Engineer"),
    FARMER("Farmer"),
    HANDYMAN("Handyman"),
    HEALER("Healer"),
    HUNTER("Hunter"),
    ICEMAN("Iceman"),
    IMMOBILIZER("Immobilizer"),
    LUMBERJACK("Lumberjack"),
    MERCENARY("Mercenary"),
    MINER("Miner"),
    NINJA("Ninja"),
    PYRO("Pyro"),
    RIFT_WALKER("Rift Walker"),
    ROBIN_HOOD("Robin Hood"),
    SCORPIO("Scorpio"),
    SCOUT("Scout"),
    SNIPER("Sniper"),
    SPIDER("Spider"),
    SPY("Spy"),
    SUCCUBUS("Succubus"),
    SWAPPER("Swapper"),
    THOR("Thor"),
    TINKERER("Tinkerer"),
    TRANSPORTER("Transporter"),
    VAMPIRE("Vampire"),
    WARRIOR("Warrior"),
    WIZARD("Wizard");

    private String name;

    private ClassType(String name) {
      this.name = name;
    }

    public String getDisplayName() {
      return this.name;
    }

    public static ClassType getClassTypeFromName(String name) {
      for (ClassType classType : ClassType.values()) {
        if (classType.getDisplayName().equals(name)) { return classType; }
      }
      return ClassType.CIVILIAN;
    }
  }

  private ClassType usingClassType;
  private boolean isInGame = false;
  private int phase = 0;
  private int kills = 0;

  private Minecraft mc;
  private Map<UUID, BossInfoClient> bossInfoMap = null;

  public AnniObserver(Minecraft mcIn) {
    this.mc = mcIn;
  }

  public ClassType getUsingClassType() {
    return usingClassType;
  }

  public boolean isInGame() {
    return isInGame;
  }

  public void setIsInGame(boolean isInGame) {
    this.isInGame = isInGame;
  }

  public int getPhase() {
    return phase;
  }

  public int getKillCount() {
    return this.kills;
  }

  public void onClientTick(ClientTickEvent event) {
    if (mc.ingameGUI != null) {

      if (this.bossInfoMap == null) {
        this.bossInfoMap = getBossInfoMap(mc.ingameGUI.getBossOverlay());
      }

      if (bossInfoMap != null) {
        for (BossInfoClient bossInfo : bossInfoMap.values()) {
          String name = bossInfo.getName().getUnformattedText();
          String[] split = name.split(" ");
          if (name.startsWith("Phase") && split.length >= 2) {
            this.phase = Integer.valueOf(split[1]);
          } else if (name.startsWith("NEXUS BLEED")) {
            this.phase = 5;
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

    if (!message.getFormattedText().startsWith(formattedName)) { return false; }

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
  private String removeTeamPrefix(String formatted) {
    if (formatted.endsWith("]")) { return null; }
    int indexOf = formatted.indexOf("]");
    if (indexOf == -1) { return null; }
    return formatted.substring(indexOf + 1);
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
