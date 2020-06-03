package onimen.anni.hmage.util;

public class DateUtils {

  public static String getDateString(long timestamp) {
    long now = System.currentTimeMillis();
    long sec = (now - timestamp) / 1000;
    long min = sec / 60;
    long hour = min / 60;
    long day = hour / 24;
    long week = day / 7;
    long month = week / 4;
    long year = month / 12;

    if (year >= 1) {
      return year + " y ago";
    } else if (month >= 1) {
      return month + " month ago";
    } else if (week >= 1) {
      return week + " week ago";
    } else if (day >= 1) {
      return day + " day ago";
    } else if (hour >= 1) {
      return hour + " hour ago";
    } else if (min >= 1) {
      return min + " min ago";
    } else if (sec >= 1) {
      return sec + " sec ago";
    } else {
      return "just now";
    }
  }

}
