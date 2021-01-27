package org.inurl.spring.ext.bind;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see org.springframework.web.bind.annotation.RequestParam
 *
 * @author raylax
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModelRequestParam {

    /**
     * @see org.springframework.web.bind.annotation.RequestParam#value()
     */
    @AliasFor("name")
    String value() default "";

    /**
     * @see org.springframework.web.bind.annotation.RequestParam#name()
     */
    @AliasFor("value")
    String name() default "";

}