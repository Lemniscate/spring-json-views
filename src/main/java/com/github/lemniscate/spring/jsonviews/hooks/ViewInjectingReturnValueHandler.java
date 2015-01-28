package com.github.lemniscate.spring.jsonviews.hooks;

import com.github.lemniscate.spring.jsonviews.client.BaseView;
import com.github.lemniscate.spring.jsonviews.client.DataView;
import com.github.lemniscate.spring.jsonviews.client.DataViewImpl;
import com.github.lemniscate.spring.jsonviews.client.ResponseView;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Author dave 1/28/15 1:07 PM
 */
public class ViewInjectingReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public ViewInjectingReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {

        Class<? extends BaseView> viewClass = getDeclaredViewClass(returnType);
        if (viewClass != null){
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
