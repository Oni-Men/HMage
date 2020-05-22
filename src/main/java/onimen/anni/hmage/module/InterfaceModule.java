package onimen.anni.hmage.module;

import java.util.List;

public interface InterfaceModule {

  public boolean isEnable();

  public void setEnable(boolean value);

  public String getName();

  public String getDescription();

  public List<String> getPreferenceKeys();

}
