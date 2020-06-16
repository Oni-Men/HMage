package onimen.anni.hmage.module;

import java.util.List;

public interface InterfaceModule {

  public boolean isEnable();

  default boolean isShowMenu() {
    return true;
  }

  public void setEnable(boolean value);

  public boolean canBehaivor();

  public String getId();

  public String getName();

  public List<String> getDescription();
}
