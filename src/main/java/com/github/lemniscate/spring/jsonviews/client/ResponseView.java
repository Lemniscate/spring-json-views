package com.github.lemniscate.spring.jsonviews.client;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author dave 1/28/15 1:10 PM
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseView {
    public Class<? extends BaseView> value();
}
