package com.app.base.utils.network.annotation;

import com.app.base.utils.network.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetWork {
    NetType netType() default NetType.AUTO;
}
