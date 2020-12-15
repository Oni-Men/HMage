package onimen.anni.hmage.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface FloatOption {

  String id();

  boolean showMenu() default false;

  String name() default "";

  String description() default "";

  float min() default 0F;

  float max() default 100.0F;

  float step() default 1F;
}
