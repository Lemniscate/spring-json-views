package com.github.lemniscate.spring.jsonviews.struct;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.lemniscate.spring.jsonviews.client.BaseView;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.hal.Jackson2HalModule;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A serializable-friendly implementation of {@link Resource}
 */
public class BaseViewResource<T> extends Resource<T> {

    /**
     * You should NEVER use this; It exists purely to help serialization.
     */
    @Deprecated
    public BaseViewResource(){
        super(null);
    }

    public BaseViewResource(T content, List<Link> links) {
        super(content, convertLinks(links) );
    }

    public BaseViewResource(T content, Link... links) {
        this(content, Arrays.asList(links));
    }

    private static List<Link> convertLinks(List<Link> orig){
        List<Link> result = new ArrayList<>();
        if( orig != null ) {
            for (Link l : orig) {
                result.add(new BaseViewLink(l));
            }
        }
        return result;
    }

    // TODO look at {@link ResourceSupportMixin} and refactor these out?
    @XmlElement(name = "link")
    @JsonProperty("_links")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY, using = Jackson2HalModule.HalLinkListSerializer.class)
    @JsonDeserialize(using = Jackson2HalModule.HalLinkListDeserializer.class)
    @JsonView(BaseView.class)
    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }

    @JsonView(BaseView.class)
    @Override
    public T getContent() {
        return super.getContent();
    }

    /**
     * A serializable-friendly implementation of {@link Link}
     */
    public static class BaseViewLink extends Link {

        public BaseViewLink(Link original){
            super(original.getHref(), original.getRel());
        }

        @JsonView(BaseView.class)
        @Override
        public String getHref() {
            return super.getHref();
        }

        @JsonView(BaseView.class)
        @Override
        public String getRel() {
            return super.getRel();
        }
    }

    /**
     * A serializable-friendly implementation of {@link PageImpl}
     */
    public static class BaseViewPageImpl<T> extends PageImpl<T> {
        public BaseViewPageImpl(List<T> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }

        public BaseViewPageImpl(List<T> content) {
            super(content);
        }

        @JsonView(BaseView.class)
        @Override
        public int getTotalPages() {
            return super.getTotalPages();
        }

        @JsonView(BaseView.class)
        @Override
        public long getTotalElements() {
            return super.getTotalElements();
        }

        @JsonView(BaseView.class)
        @Override
        public boolean hasNext() {
            return super.hasNext();
        }

        @JsonView(BaseView.class)
        @Override
        public boolean isLast() {
            return super.isLast();
        }

        @JsonView(BaseView.class)
        @Override
        public int getNumber() {
            return super.getNumber();
        }

        @JsonView(BaseView.class)
        @Override
        public int getSize() {
            return super.getSize();
        }

        @JsonView(BaseView.class)
        @Override
        public int getNumberOfElements() {
            return super.getNumberOfElements();
        }

        @JsonView(BaseView.class)
        @Override
        public boolean hasPrevious() {
            return super.hasPrevious();
        }

        @JsonView(BaseView.class)
        @Override
        public boolean isFirst() {
            return super.isFirst();
        }

        @JsonView(BaseView.class)
        @Override
        public List<T> getContent() {
            return super.getContent();
        }
    }
}
