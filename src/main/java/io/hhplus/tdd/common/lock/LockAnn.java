package io.hhplus.tdd.common.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LockAnn {
    long waitTime() default 2000L;
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
    LockKey lockKey() default LockKey.USER;
}
