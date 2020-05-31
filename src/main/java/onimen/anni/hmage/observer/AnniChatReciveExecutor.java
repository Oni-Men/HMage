package onimen.anni.hmage.observer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.text.ITextComponent;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.observer.data.AnniTeamColor;

public class AnniChatReciveExecutor {

  private static Pattern killChatPattern = Pattern
      .compile("§(.)(.+)§(.)\\((.+)\\) (shot|killed) §(.)(.+)§(.)\\((.+)\\).*");

  private static BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();

  private static ChatParseTask target = null;

  /**
   * チャットを受け取ったときの処理。
   *
   * @param textComponent チャット
   */
  public static void onReceiveChat(ITextComponent textComponent) {
    blockingQueue.add(textComponent.getUnformattedText());
  }

  public synchronized static void startThread() {
    if (target != null) { return; }

    target = new ChatParseTask();
    Thread thread = new Thread(target);
    thread.setDaemon(true);
    thread.start();
  }

  static class ChatParseTask implements Runnable {

    @Override
    public void run() {
      while (true) {
        try {
          String text = blockingQueue.poll(200, TimeUnit.SECONDS);
          if (text == null) {
            continue;
          }

          //パターンにマッチすることを確認
          Matcher matcher = killChatPattern.matcher(text);
          if (!matcher.matches()) {
            continue;
          }

          AnniObserver anniObserver = AnniObserverMap.getInstance().getAnniObserver();
          if (anniObserver == null) { return; }

          //キル数を加算
          AnniTeamColor killerTeam = AnniTeamColor.findByColorCode(matcher.group(1));
          AnniTeamColor deadTeam = AnniTeamColor.findByColorCode(matcher.group(6));
          AnniKillType killType = matcher.group(5).equalsIgnoreCase("shot") ? AnniKillType.SHOT : AnniKillType.MELEE;
          anniObserver.getGameInfo().addKillCount(matcher.group(2), killerTeam, matcher.group(7), deadTeam, killType);

        } catch (Throwable e) {
          HMage.logger.error("Error:", e);
        }
      }
    }
  }
}
