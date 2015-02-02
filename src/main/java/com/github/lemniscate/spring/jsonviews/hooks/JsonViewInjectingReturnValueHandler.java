package com.github.lemniscate.spring.jsonviews.hooks;

import com.github.lemniscate.spring.jsonviews.client.*;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Author dave 2/1/15 1:46 PM
 */
public class JsonViewInjectingReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public JsonViewInjectingReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        Class<? extends BaseView> viewClass = getDeclaredViewClass(returnType);

        if( JsonViewResponseEntity.class.isAssignableFrom(returnValue.getClass()) ){
            JsonViewResponseEntity f = (JsonViewResponseEntity) returnValue;
            DataView<?> dv = f.getDataView();
            returnValue = new ResponseEntity(dv, f.getHeaders(), f.getStatusCode());
        }else if(viewClass != null){
            returnValue = wrapResult(returnValue,viewClass, returnType, returnValue);
        }

        delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
    /**
     * Returns the view class declared on the method, if it exists.
     * Otherwise, returns null.
     * @param returnType
     * @return
     */
    private Class<? extends BaseView> getDeclaredViewClass(MethodParameter returnType) {
        ResponseView annotation = returnType.getMethodAnnotation(ResponseView.class);
        if (annotation != null)
        {
            return annotation.value();
        } else {
            return null;
        }
    }

    private Object wrapResult(Object result, Class<? extends BaseView> viewClass, MethodParameter returnType, Object orig) {
        // TODO clean up the handling of response entities
        if(ResponseEntity.class.isAssignableFrom(returnType.getParameterType())){
            ResponseEntity o = (ResponseEntity) orig;
            return new ResponseEntity(new DataViewImpl(o.getBody(), viewClass), o.getHeaders(), o.getStatusCode());
        }
        DataView response = new DataViewImpl(result, viewClass);
        return response;
    }

}
