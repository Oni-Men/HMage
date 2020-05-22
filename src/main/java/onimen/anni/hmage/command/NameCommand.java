package onimen.anni.hmage.command;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.mojangAPI.MojangAPI;
import onimen.anni.hmage.mojangAPI.dto.NameChangeResponse;
import onimen.anni.hmage.mojangAPI.dto.UuidResponse;
import onimen.anni.hmage.mojangAPI.exception.PlayerNotFoundException;
import onimen.anni.hmage.util.scheduler.SyncTaskQueue;

public class NameCommand extends CommandBase implements IClientCommand {

  private static final DateTimeFormatter OF_PATTERN = DateTimeFormatter.ofPattern("uuuu/MM/dd");
  private static ExecutorService executorService = Executors.newSingleThreadExecutor();

  @Override
  public String getName() {
    return "name";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/name [player名]";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 0) { return; }

    executorService.execute(() -> {
      List<TextComponentString> messageList = new ArrayList<>();

      try {
        //名前からuuidへの変換
        UuidResponse uuidResponse = MojangAPI.getUUIDFromMCID(args[0]);

        //uuidから履歴を取得
        List<NameChangeResponse> nameHistory = MojangAPI.getNameHistory(uuidResponse.getId());

        //結果からメッセージを構築
        sender.sendMessage(new TextComponentString(ChatFormatting.GREEN + "====" + args[0] + "の名前変更履歴===="));
        int index = 1;
        for (NameChangeResponse response : nameHistory) {
          if (response.getChangedToAt() != null) {
            Instant instant = Instant.ofEpochMilli(response.getChangedToAt());
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            messageList.add(new TextComponentString(ChatFormatting.GOLD.toString() + index + ". "
                + response.getName() + "     " + ChatFormatting.AQUA + ldt.format(OF_PATTERN)));
          } else {
            messageList.add(new TextComponentString(ChatFormatting.GOLD.toString() + index + ". "
                + response.getName()));
          }
          index++;
        }

      } catch (IOException e) {
        HMage.logger.error("通信エラーが発生しました。", e);
        messageList.add(new TextComponentString(ChatFormatting.RED + "通信エラーが発生しました。"));
      } catch (PlayerNotFoundException e) {
        HMage.logger.warn("指定したプレイヤーが存在しません。:" + args[0]);
        messageList.add(new TextComponentString(ChatFormatting.RED + "指定したプレイヤーが存在しません。"));
      }

      //結果を画面に表示
      SyncTaskQueue.addTask(() -> {
        for (TextComponentString text : messageList) {
          sender.sendMessage(text);
        }
      });
    });
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
      BlockPos targetPos) {

    String s = (args.length == 0 ? "" : args[args.length - 1]).toLowerCase();

    List<String> nameList = sender.getEntityWorld().playerEntities.stream()
        .filter(p -> p.getName().toLowerCase().startsWith(s))
        .map(p -> p.getName())
        .collect(Collectors.toList());

    return nameList;
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
