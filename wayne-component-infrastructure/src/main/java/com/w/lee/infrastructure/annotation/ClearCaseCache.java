package com.w.lee.infrastructure.annotation;

import java.lang.annotation.*;

/**
 * 延时双删
 *
 * @author w.lee
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface ClearCaseCache {
    /**
     * 实验ID字段名称
     */
    String caseCodeFieldName() default "";

    /**
     * 实验主键ID字段名称
     */
    String caseIdFieldName() default "";

}