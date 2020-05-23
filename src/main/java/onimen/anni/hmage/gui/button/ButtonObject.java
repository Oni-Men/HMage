package onimen.anni.hmage.gui.button;

import java.util.List;

import net.minecraft.client.gui.GuiButton;

public interface ButtonObject {

  /**
   * ボタン左のタイトルを取得する。
   *
   * @return タイトル
   */
  String getTitle();

  /**
   * ボタン内のテキストを取得する。
   *
   * @return ボタン内のテキスト
   */
  String getButtonText();

  /**
   * ボタンがクリックされたときの処理を実行する。
   *
   * @param button クリックしたボタン
   */
  void actionPerformed(GuiButton button);

  /**
   * 説明を取得する。
   *
   * @return ツールチップに表示する説明
   */
  List<String> getDescription();
}
