package onimen.anni.hmage.mojangAPI;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import onimen.anni.hmage.mojangAPI.dto.NameChangeResponse;
import onimen.anni.hmage.mojangAPI.dto.UuidResponse;
import onimen.anni.hmage.mojangAPI.exception.PlayerNotFoundException;
import onimen.anni.hmage.util.HttpUtil;
import onimen.anni.hmage.util.HttpUtil.Result;

public class MojangAPI {

  /**
   * MCIDからUUIDを取得する。
   *
   * @param mcid mcid
   * @return uuid
   * @throws IOException 通信エラーが発生した場合
   * @throws PlayerNotFoundException 指定したユーザーが存在しない場合
   */
  public static UuidResponse getUUIDFromMCID(String mcid) throws IOException, PlayerNotFoundException {
    String url = "https://api.mojang.com/users/profiles/minecraft/" + mcid
        + "?at=" + (int) (System.currentTimeMillis() / 1000);
    Result result = HttpUtil.get(url);
    //ステータス200以外
    if (result.getResponseStatus() != 200) { throw new PlayerNotFoundException(); }

    Gson gson = new Gson();
    return gson.fromJson(result.getResponse().stream().collect(Collectors.joining()), UuidResponse.class);
  }

  /**
   * UUIDから指定したプレイヤーの名前変更履歴を取得する。
   *
   * @param uuid uuid
   * @return 名前変更履歴
   * @throws IOException
   * @throws PlayerNotFoundException
   */
  public static List<NameChangeResponse> getNameHistory(String uuid) throws IOException, PlayerNotFoundException {
    String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";
    Result result = HttpUtil.get(url);
    //ステータス200以外
    if (result.getResponseStatus() != 200) { throw new PlayerNotFoundException(); }

    Gson gson = new Gson();
    Type listType = new TypeToken<List<NameChangeResponse>>() {
    }.getType();
    return gson.fromJson(result.getResponse().stream().collect(Collectors.joining()), listType);
  }
}
