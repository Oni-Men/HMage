package onimen.anni.hmage.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HttpUtil {

  /**
   * 指定したURLにGetリクエストを送信する。
   *
   * @param urlStr url
   * @return 返却値
   * @throws IOException 通信エラーが発生した場合
   */
  public static Result get(String urlStr) throws IOException {
    URL url = new URL(urlStr);
    HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
    //ステータスコードを確認
    int responseCode = openConnection.getResponseCode();
    if (responseCode != 200) { return new Result(responseCode, Collections.emptyList()); }

    //レスポンスBodyを取得
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()))) {
      lines.add(reader.readLine());
    }
    return new Result(200, lines);
  }

  public static class Result {
    private final int responseStatus;

    private final List<String> response;

    public Result(int responseStatus, List<String> response) {
      this.responseStatus = responseStatus;
      this.response = response;
    }

    public int getResponseStatus() {
      return responseStatus;
    }

    public List<String> getResponse() {
      return response;
    }

  }
}
