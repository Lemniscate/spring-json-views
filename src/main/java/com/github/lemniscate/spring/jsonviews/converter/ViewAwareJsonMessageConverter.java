package com.github.lemniscate.spring.jsonviews.converter;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.lemniscate.spring.jsonviews.client.BaseView;
import com.github.lemniscate.spring.jsonviews.client.DataView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @Author dave 1/28/15 1:15 PM
 */
public class ViewAwareJsonMessageConverter extends MappingJackson2HttpMessageConverter {

    private static final Logger log = LoggerFactory.getLogger(ViewAwareJsonMessageConverter.class);

    public ViewAwareJsonMessageConverter(ObjectMapper objectMapper) {
        super();
        setObjectMapper(objectMapper);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
        throws IOException, HttpMessageNotWritableException {
        if (object instanceof DataView && ((DataView) object).hasView())
        {
            writeView((DataView) object, outputMessage);
        } else {
            super.writeInternal(object, outputMessage);
        }
    }
    protected void writeView(DataView view, HttpOutputMessage outputMessage)
        throws IOException, HttpMessageNotWritableException {
        ObjectWriter writer = getObjectMapper().writerWithView(view.getView());

        try {
            Object body = view.getData();

            if( body instanceof Resource){
                body = new BaseViewResource((Resource) body);
            }

            writer.writeValue(outputMessage.getBody(), body);
        }catch (IOException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }


    public static void configureMessageConverters(ObjectMapper objectMapper, List<HttpMessageConverter<?>> converters) {
        Iterator<HttpMessageConverter<?>> itr = converters.iterator();
        while(itr.hasNext() ){
            HttpMessageConverter<?> next = itr.next();
            if( MappingJackson2HttpMessageConverter.class.isAssignableFrom(next.getClass()) ){
                itr.remove();
                log.info("Removed alternate message converter of type {}", next.getClass().getSimpleName());
            }
        }

        converters.add(new ViewAwareJsonMessageConverter(objectMapper));
        log.info("Configured {}", ViewAwareJsonMessageConverter.class.getSimpleName());
    }


    public static class BaseViewResource extends Resource{

        public BaseViewResource(Resource resource) {
            super(resource.getContent(), resource.getLinks());
        }

        @JsonView(BaseView.class)
        @Override
        public List<Link> getLinks() {
            return super.getLinks();
        }

        @JsonView(BaseView.class)
        @Override
        public Object getContent() {
            return super.getContent();
        }
    }

}


