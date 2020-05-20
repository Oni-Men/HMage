package onimen.anni.hmage.mojangAPI.dto;

/**
 * 名前の変更情報。
 */
public class NameChangeResponse {

  private String name;

  private Long changedToAt;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getChangedToAt() {
    return changedToAt;
  }

  public void setChangedToAt(Long changedToAt) {
    this.changedToAt = changedToAt;
  }

}
