package onimen.anni.hmage.transformer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.objectweb.asm.Opcodes;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.HMage;

@SideOnly(Side.CLIENT)
public class HMageClassTransformer implements IClassTransformer, Opcodes {

  private static final String[] replaceList = new String[] {
      "net.minecraft.client.gui.GuiScreen"
  };

  @Override
  public byte[] transform(String name, String transformedName, byte[] bytes) {
    if (Arrays.<String> asList(replaceList).contains(transformedName)) {
      try {
        return replaceClass(bytes, name, transformedName);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return bytes;
  }

  private byte[] replaceClass(byte[] bytes, String name, String transformedName) throws IOException {
    ZipFile zipFile = null;
    InputStream streamIn = null;

    if (HMageCoreLoader.location == null) {
      HMage.logger.error("CoreMod Location is null: " + transformedName);
      return bytes;
    }

    String readClass = transformedName.replace(".", "/") + ".class";

    try {
      zipFile = new ZipFile(HMageCoreLoader.location);
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      ZipEntry zipEntry = null;

      while (entries.hasMoreElements()) {
        ZipEntry next = entries.nextElement();
        if (next.getName().contentEquals(readClass)) {
          zipEntry = next;
          break;
        }
      }

      if (zipEntry != null) {
        streamIn = zipFile.getInputStream(zipEntry);
        bytes = new byte[(int) zipEntry.getSize()];
        streamIn.read(bytes);
      } else {
        HMage.logger.error("File not found: " + readClass);
      }
      return bytes;
    } catch (Exception e) {
      HMage.logger.error("Failed to replace class:" + transformedName);
      return bytes;
    } finally {
      if (streamIn != null) {
        streamIn.close();
      }
      if (zipFile != null) {
        zipFile.close();
      }
    }

  }
}
