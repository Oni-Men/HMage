package onimen.anni.hmage.module;

import java.util.Queue;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import onimen.anni.hmage.Preferences;

public class CpsCounter extends AbstractModule {

  private final Queue<Long> clicks;

  public CpsCounter() {
    this.clicks = Lists.newLinkedList();
  }

  @Override
  public boolean isShowMenu() {
    return false;
  }

  @Override
  public String getName() {
    return "CountCPS";
  }

  @Override
  public String getDescription() {
    return "CPSを計測する";
  }

  public int getCurrentCPS() {

    long now = System.currentTimeMillis();

    while (!clicks.isEmpty() && clicks.peek() < now) {
      clicks.remove();
    }

    return clicks.size();

  }

  @SubscribeEvent
  public void onAttackKeyClick(MouseInputEvent event) {
    if (!Preferences.enabled)
      return;

    if (!Mouse.getEventButtonState())
      return;

    if (Mouse.getEventButton() != 0)
      return;

    this.clicks.add(System.currentTimeMillis() + 1000L);
  }

}
