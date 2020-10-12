package com.darkj24.ioc.annotations;

import com.darkj24.ioc.enums.AutowiringMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Bean {
    public AutowiringMode autowire() default AutowiringMode.NO;
}
