package onimen.anni.hmage.transformer;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion("1.12.2")
public class HMageCoreLoader implements IFMLLoadingPlugin {

  public static File location;

  @Override
  public String[] getASMTransformerClass() {
    return new String[] { "onimen.anni.hmage.transformer.HMageClassTransformer" };
  }

  @Override
  public String getModContainerClass() {
    return null;
  }

  @Override
  public String getSetupClass() {
    return null;
  }

  @Override
  public void injectData(Map<String, Object> data) {
    if (data.containsKey("coremodLocation")) {
      location = (File) data.get("coremodLocation");
    }
  }


  @Override
  public String getAccessTransformerClass() {
    return null;
  }

}
