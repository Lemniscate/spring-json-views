package com.github.lemniscate.spring.jsonviews.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * @Author dave 1/28/15 1:13 PM
 */
public class JsonViewResponseEntity<T> extends ResponseEntity<DataView<T>> {

    private final Class<? extends BaseView> view;

    public JsonViewResponseEntity(Class<? extends BaseView> view, T body, HttpStatus statusCode) {
        super(new DataViewImpl<T>(body, view), statusCode);
        this.view = view;
    }

    public JsonViewResponseEntity(Class<? extends BaseView> view, T body, MultiValueMap<String, String> headers, HttpStatus statusCode) {
        super(new DataViewImpl<T>(body, view), headers, statusCode);
        this.view = view;
    }

}

