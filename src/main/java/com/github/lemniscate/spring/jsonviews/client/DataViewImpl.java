package com.github.lemniscate.spring.jsonviews.client;

import com.github.lemniscate.spring.jsonviews.struct.BaseViewResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @Author dave 1/28/15 1:11 PM
 */
public class DataViewImpl<T> implements DataView<T>{

    private final T data;
    private final Class<? extends BaseView> view;

    public DataViewImpl(T data, Class<? extends BaseView> view) {
        if( Page.class.isAssignableFrom(data.getClass()) ){
            Page<?> page = (Page<?>) data;
            Pageable p = new PageRequest(page.getNumber(), page.getSize());
            data = (T) new BaseViewResource.BaseViewPageImpl(page.getContent(), p, page.getTotalElements());
        }
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
