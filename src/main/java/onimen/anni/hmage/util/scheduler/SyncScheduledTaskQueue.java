package onimen.anni.hmage.util.scheduler;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SyncScheduledTaskQueue {

  static class ScheduledTask {
    private long when;
    private final Runnable task;

    public ScheduledTask(Runnable task, long later) {
      this.task = task;
      this.when = System.currentTimeMillis() + later;
    }

    public boolean shouldExecute() {
      return this.when <= System.currentTimeMillis();
    }

  }

  private static List<ScheduledTask> nextExecuteList = Collections.synchronizedList(new LinkedList<>());

  public static void addTask(Runnable task, long later) {
    if (task == null || later < 0)
      return;
    nextExecuteList.add(new ScheduledTask(task, later));
  }

  public static void tick() {
    if (nextExecuteList.isEmpty())
      return;

    Iterator<ScheduledTask> iterator = nextExecuteList.iterator();
    ScheduledTask next;
    while (iterator.hasNext()) {
      next = iterator.next();
      if (next.shouldExecute()) {
        try {
          next.task.run();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          iterator.remove();
        }
      }
    }

  }
}
