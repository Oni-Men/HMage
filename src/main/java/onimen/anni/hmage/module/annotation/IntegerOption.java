package onimen.anni.hmage.module.annotation;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface IntegerOption {

  String id();

  String name();

  int min() default 0;

  int max() default Integer.MAX_VALUE;

  int step() default 1;

}
