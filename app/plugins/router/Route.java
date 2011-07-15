package plugins.router;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Route {

    String value() default "";

    int priority() default -1;

    String format() default "";

    String accept() default "";

    String method() default "*";
}
