package onimen.anni.hmage.module.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface FloatOption {

  String id();

  String name() default "";

  String description() default "";

  float min() default 0F;

  float max() default 100.0F;

  float step() default 1F;
}
