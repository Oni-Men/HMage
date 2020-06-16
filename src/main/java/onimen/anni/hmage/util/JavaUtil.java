package onimen.anni.hmage.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

public class JavaUtil {

  public static double round(double val, int digits) {
    double pow = Math.pow(10, digits);
    return Math.round(pow * val) / pow;
  }

  public static Set<Field> getAllDeclaredField(Class<? extends Object> moduleClass) {
    HashSet<Field> fields = new HashSet<>();

    while (moduleClass != null) {
      fields.addAll(Arrays.asList(moduleClass.getDeclaredFields()));
      moduleClass = moduleClass.getSuperclass();
    }
    return fields;
  }

  public static <T extends Annotation> Map<Field, T> getAnnotatedFields(Set<Field> fields, Class<T> annotationClass) {
    Map<Field, T> annotatedFields = new HashMap<>();
    for (Field field : fields) {
      T annotation = field.getAnnotation(annotationClass);
      if (annotation != null) {
        annotatedFields.put(field, annotation);
      }
    }
    return annotatedFields;
  }

  public static <T extends Annotation> void tryLoopOptionEntrySet(Set<Entry<Field, T>> entrySet,
      ThrowableBiConsumer<Field, T> bi) {
    for (Entry<Field, T> entry : entrySet) {
      Field field = entry.getKey();
      T option = entry.getValue();

      try {
        bi.accept(field, option);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static <T> void tryExecuteConsumer(T arg, ThrowableConsumer<T> execute) {
    try {
      execute.accept(arg);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 与えられたexecuteを実行し、エラーが発生したらnullを返す
   */
  @Nullable
  public static <T, R> R tryExecuteFunction(T arg, ThrowableFunction<T, R> execute) {
    try {
      return execute.apply(arg);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public interface ThrowableConsumer<T> {
    void accept(T t) throws Exception;
  }

  public interface ThrowableBiConsumer<T, U> {
    void accept(T t, U u) throws Exception;
  }

  public interface ThrowableFunction<T, R> {
    R apply(T t) throws Exception;
  }
}
