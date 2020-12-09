package onimen.anni.hmage.module;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SSClipboard extends AbstractModule {

  @Override
  public String getId() {
    return "hmage.module.ss-clipboard";
  }

  @SubscribeEvent
  public void onScreenShot(ScreenshotEvent event) {
    if(!canBehave()) {return;}
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    ImageSelection imageSelection = new ImageSelection(event.getImage());
    clipboard.setContents(imageSelection, null);
  }

  static class ImageSelection implements Transferable {

    private BufferedImage image;
    private DataFlavor[] flavor;

    public ImageSelection(BufferedImage image) {
      this.image = image;
      this.flavor = new DataFlavor[] { DataFlavor.imageFlavor };
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
      return flavor;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
      return this.flavor[0].equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if (!this.isDataFlavorSupported(flavor)) { throw new UnsupportedFlavorException(flavor); }
      return image;
    }

  }

}
