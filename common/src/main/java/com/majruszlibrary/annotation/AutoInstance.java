package com.majruszlibrary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Classes marked with this annotation will be automatically instanced inside ModHelper class. */
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface AutoInstance {}
