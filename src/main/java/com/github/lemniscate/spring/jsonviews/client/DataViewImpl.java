package com.github.lemniscate.spring.jsonviews.client;

/**
 * @Author dave 1/28/15 1:11 PM
 */
public class DataViewImpl<T> implements DataView<T>{

    private final T data;
    private final Class<? extends BaseView> view;

    public DataViewImpl(T data, Class<? extends BaseView> view) {
        this.data = data;
        this.view = view;
    }

    @Override
    public boolean hasView() {
        return view != null;
    }

    @Override
    public Class<? extends BaseView> getView() {
        return view;
    }

    @Override
    public T getData() {
        return data;
    }
}
