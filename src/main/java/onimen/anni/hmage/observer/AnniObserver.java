package onimen.anni.hmage.observer;

import org.lwjgl.util.Timer;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

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
  private Timer bossTimer;
  private int killCounter = 0;

  private Minecraft mc;

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

  public void onRecieveChat(ClientChatReceivedEvent event) {

    System.out.println("[EVENT] ClientChatReceivedEvent");

    ITextComponent message = event.getMessage();

    if (event.getType() != ChatType.CHAT)
      return;

    if (this.isClassSelect(message)) {
      System.out.println("[IF-PASSED] ClassSelectMessage");
      this.usingClassType = getClassType(message);
    }

    if (this.isKillLogOfPlayer(message, mc.player)) {
      System.out.println("[IF-PASSED] Log of My Kill");
      killCounter++;
    }
  }

  private boolean isClassSelect(ITextComponent message) {
    return message.getFormattedText().startsWith(ChatFormatting.GOLD + "[Class]");
  }

  private ClassType getClassType(ITextComponent message) {
    String[] split = message.getUnformattedText().split(" ");

    if (split.length != 3)
      return ClassType.CIVILIAN;

    return ClassType.getClassTypeFromName(split[1]);
  }

  private boolean isKillLogOfPlayer(ITextComponent message, EntityPlayer player) {
    if (player == null)
      return false;

    System.out.println("Player's Display Name: " + player.getDisplayName().getFormattedText());

    if (!message.getFormattedText().startsWith(player.getDisplayName().getFormattedText()))
      return false;

    String[] split = message.getUnformattedText().split(" ");

    if (split.length < 3)
      return false;

    return split[1].equals("killed");
  }
}
