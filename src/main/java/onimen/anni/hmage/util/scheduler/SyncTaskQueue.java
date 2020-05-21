package onimen.anni.hmage.util.scheduler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SyncTaskQueue {

  private static List<Runnable> nextExecuteList = Collections.synchronizedList(new LinkedList<>());

  public static void addTask(Runnable runnable) {
    nextExecuteList.add(runnable);
  }

  public static Runnable getNextTask() {
    if (nextExecuteList.isEmpty()) { return null; }
    return nextExecuteList.remove(0);
  }
}
