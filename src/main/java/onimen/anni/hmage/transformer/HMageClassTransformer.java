package onimen.anni.hmage.transformer;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.launchwrapper.IClassTransformer;

public class HMageClassTransformer implements IClassTransformer {

  private ZipFile hmageZip = null;

  public HMageClassTransformer() {
    //    try {
    //      URL url = HMageClassTransformer.class.getProtectionDomain().getCodeSource().getLocation();
    //      URI uri = url.toURI();
    //      File file = new File("D:\\Developments\\workspace\\HMage\\build\\libs\\HMage-1.0.1.jar");
    //      this.hmageZip = new ZipFile(file);
    //      Enumeration<? extends ZipEntry> entries = this.hmageZip.entries();
    //
    //      while (entries.hasMoreElements()) {
    //        String name = entries.nextElement().getName();
    //        System.out.println(name);
    //      }
    //
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //    }
  }

	@Override
  public byte[] transform(String name, String transformedName, byte[] bytes) {
    if (!name.contains("LayerArmorBase"))
      return bytes;
    String nameClass = "onimen/anni/hmage/patch/renderer/entity/layers/LayerArmorBase.class";//String.valueOf(name) + ".class";
    byte[] hmageBytes = getHMageResource(nameClass);
    if (hmageBytes != null) {
      System.out.println("Transform " + name);
      return hmageBytes;
    }
    return bytes;
	}

  private synchronized byte[] getHMageResource(String name) {
    try {
    InputStream in = getHMageResourceStream(name);
    if (in == null)
      return null;
    byte[] bytes = IOUtils.toByteArray(in);
      in.close();
      return bytes;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private synchronized InputStream getHMageResourceStream(String name) {
    if (this.hmageZip == null)
      return null;
    name = StringUtils.removeStart(name, "/");
    ZipEntry entry = this.hmageZip.getEntry(name);
    if (entry == null) {
      return null;
    }
    try {
      InputStream in = this.hmageZip.getInputStream(entry);
      return in;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
