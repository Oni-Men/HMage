package onimen.anni.hmage.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.observer.AnniChatReciveExecutor;
import onimen.anni.hmage.observer.AnniChatReciveExecutor.ChatParseRunner;
import onimen.anni.hmage.observer.AnniObserver;

public class TestAnniCommand extends CommandBase implements IClientCommand {

  private static long limitedTime;

  @Override
  public String getName() {
    return "testAnni";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/testAnni";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    TextComponentString text = new TextComponentString("");

    String[] splited = { "§9TestPlayer1", "§9(ACR)", " killed ", "§eTestPlayer2", "§e(MIN)" };
    for (String parts : splited) {
      text.appendText(parts);
    }
    HMage.INSTANCE.onRecieveChat(new ClientChatReceivedEvent(ChatType.CHAT, text));
    sender.sendMessage(new TextComponentString("[hmage debug] 試合履歴からTestPlayer1のキル数がカウントされていることを確認してください。"));

    sender.sendMessage(new TextComponentString("[hmage debug] コマンド実行後、60秒の間にキルログが流れていることを確認してください。"));
    sender.sendMessage(new TextComponentString("[hmage debug] その後開発者へminecraftのログ(latest.log)を提出してください。"));

    limitedTime = System.currentTimeMillis() + (1000 * 60);

    //observer情報
    HMage.logger.info("===HMage Mod Debug Information===");
    AnniObserver anniObserver = HMage.anniObserverMap.getAnniObserver();
    HMage.logger.info("Observer: " + (anniObserver == null ? "none" : "exist"));
    HMage.logger.info("Server Name: " + HMage.anniObserverMap.getPlayingServerName());
    ChatParseRunner target = AnniChatReciveExecutor.getTarget();
    HMage.logger.info("Chat Execute Thread: " + (target == null ? "none" : "exist"));
    HMage.logger.info("Chat Execute Last Time: "
        + (target == null ? "null" : (System.currentTimeMillis() - target.getLastExecuteTime()) + " ms ago"));
    HMage.logger.info("===HMage Mod Debug Information===");

  }

  public static void debugPrint(ClientChatReceivedEvent event) {
    if (limitedTime == -1) { return; }

    if (System.currentTimeMillis() > limitedTime) {
      limitedTime = -1;
      return;
    }
    ITextComponent message = event.getMessage();
    HMage.logger.info("[hmage debug] message:" + message.getUnformattedText() + " (" + event.getType() + ")");
  }

  @Override
  public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
    return false;
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 0;
  }

}
