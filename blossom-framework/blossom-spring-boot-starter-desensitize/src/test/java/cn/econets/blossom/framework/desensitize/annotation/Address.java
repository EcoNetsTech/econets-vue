package cn.econets.blossom.framework.desensitize.annotation;

import cn.econets.blossom.framework.desensitize.DesensitizeTest;
import cn.econets.blossom.framework.desensitize.core.base.annotation.DesensitizeBy;
import cn.econets.blossom.framework.desensitize.handler.AddressHandler;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

/**
 * 地址
 *
 * 用于 {@link DesensitizeTest} 测试使用
 *
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = AddressHandler.class)
public @interface Address {

    String replacer() default "*";

}
