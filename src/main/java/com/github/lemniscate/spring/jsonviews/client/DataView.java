package com.github.lemniscate.spring.jsonviews.client;

/**
 * @Author dave 1/28/15 1:10 PM
 */
public interface DataView<T> {
    boolean hasView();
    Class<? extends BaseView> getView();
    T getData();
}
