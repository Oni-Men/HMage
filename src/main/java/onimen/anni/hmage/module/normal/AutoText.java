package onimen.anni.hmage.module.normal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import onimen.anni.hmage.util.JavaUtil;

public class AutoText extends AbstractModule {

  private static final Pattern linePattern = Pattern.compile("(?<id>[\\p{XDigit}-]+):(?<code>[0-9]+)=(?<message>.+)");

  public static Path saveData;
  private HashMap<UUID, TextBinding> bindedTexts;
  private Set<Integer> downKeyCodes;

  public AutoText() {
    bindedTexts = new HashMap<>();
    downKeyCodes = new HashSet<>();
    this.load();

  }

  public void load() {
    if (saveData == null)
      return;
    try {
      Files.createFile(saveData);
    } catch (IOException e) {
    }
    JavaUtil.tryExecuteConsumer(saveData, path -> {
      BufferedReader buffer = Files.newBufferedReader(path, Charset.forName("utf-8"));
      for (String line = ""; (line = buffer.readLine()) != null;) {
        Matcher matcher = linePattern.matcher(line);
        if (matcher.matches()) {
          String message = matcher.group("message");
          Integer keyCode = JavaUtil.tryExecuteFunction(matcher.group("code"),
              k -> Integer.parseInt(k));
          this.bindedTexts.put(UUID.fromString(matcher.group("id")), new TextBinding(keyCode, message));
        }
      }
    });
  }

  public void save() {
    if (saveData == null)
      return;
    try {
      Files.createFile(saveData);
    } catch (IOException e) {
    }
    JavaUtil.tryExecuteConsumer(saveData, path -> {
      BufferedWriter buffer = Files.newBufferedWriter(path, Charset.forName("utf-8"));
      buffer.write("");
      bindedTexts.forEach((id, binding) -> {
        JavaUtil.tryExecuteConsumer(buffer, buf -> {
          buf.append(String.format("%s:%d=%s", id.toString(), binding.keyCode, binding.text));
          buf.newLine();
        });
      });
      buffer.flush();
      buffer.close();
    });
  }

  public HashMap<UUID, TextBinding> getBindMap() {
    return this.bindedTexts;
  }

  public TextBinding addTextBinding(int code, String msg) {
    return this.bindedTexts.computeIfAbsent(UUID.randomUUID(), k -> new TextBinding(k, code, msg));
  }

  public void updateBindMessage(UUID id, String msg) {
    this.bindedTexts.computeIfPresent(id, (k, v) -> {
      v.text = msg;
      return v;
    });
    this.save();
  }

  public void updateBindKey(UUID id, int code) {
    this.bindedTexts.computeIfPresent(id, (k, v) -> {
      v.keyCode = code;
      return v;
    });
    this.save();
  }

  public void removeTextBinding(UUID id) {
    this.bindedTexts.remove(id);
  }

  @Override
  public String getId() {
    return "hmage.module.auto-text";
  }

  @SubscribeEvent
  public void onInputUpdate(InputEvent event) {
    if (!canBehave())
      return;
    Minecraft mc = Minecraft.getMinecraft();
    for (TextBinding binding : bindedTexts.values()) {
      int key = binding.keyCode;
      //キーを押していて、まだ押しているキーを保持するセットに存在しない場合
      if (Keyboard.isKeyDown(key)) {
        if (!downKeyCodes.contains(key)) {
          //キーを押したら、押しているキーを保持するセットに追加
          downKeyCodes.add(key);

          String msg = binding.text;
          mc.ingameGUI.getChatGUI().addToSentMessages(msg);
          mc.player.sendChatMessage(msg);
        }
      } else {
        //キーを離したら削除
        downKeyCodes.remove(key);
      }

    }
  }

  public static class TextBinding {
    public final UUID id;
    public Integer keyCode;
    public String text;

    public TextBinding(Integer keyCode, String text) {
      this(UUID.randomUUID(), keyCode, text);
    }

    public TextBinding(UUID id, Integer keyCode, String text) {
      this.id = id;
      this.keyCode = keyCode;
      this.text = text;
    }
  }
}
