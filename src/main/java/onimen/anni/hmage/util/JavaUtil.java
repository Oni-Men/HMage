package onimen.anni.hmage.util;

public class JavaUtil {

  public static double round(double val, int digits) {
    double pow = Math.pow(10, digits);
    return Math.round(pow * val) / pow;
  }
}
