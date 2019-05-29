package com.yty.libframe.base;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * set the content view for activity or fragment
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ContentView {
    /**
     * the layout id
     * @return a valid positive integer value of a layout resource id
     */
    int value();
}
