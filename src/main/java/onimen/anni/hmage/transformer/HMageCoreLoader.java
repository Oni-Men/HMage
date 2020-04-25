package onimen.anni.hmage.transformer;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class HMageCoreLoader implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "onimen.anni.hmage.transformer.RedTintTransformer" };
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

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
