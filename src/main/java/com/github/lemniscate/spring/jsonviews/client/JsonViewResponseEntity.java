package com.github.lemniscate.spring.jsonviews.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * @Author dave 1/28/15 1:13 PM
 */
public class JsonViewResponseEntity<T> extends ResponseEntity<T> {

    private final DataView<T> dataView;

    public JsonViewResponseEntity(Class<? extends BaseView> view, T body, HttpStatus statusCode) {
        this(view, body, null, statusCode);
    }

    public JsonViewResponseEntity(Class<? extends BaseView> view, T body, MultiValueMap<String, String> headers, HttpStatus statusCode) {
        super(body, headers, statusCode);
        this.dataView = new DataViewImpl<T>(getBody(),view);
    }

    public DataView<T> getDataView() {
        return dataView;
    }

}

