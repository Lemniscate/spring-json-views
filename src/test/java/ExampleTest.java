import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lemniscate.spring.jsonviews.converter.ViewAwareJsonMessageConverter;
import com.github.lemniscate.spring.jsonviews.hooks.JsonViewHandlerDecorator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author dave 1/28/15 2:24 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@ContextConfiguration(classes = {ExampleTest.Config.class, JacksonAutoConfiguration.class})
@WebAppConfiguration
public class ExampleTest {

    private MockMvc mvc;

    @Inject
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testNoView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/dummy1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(is("{\"name\":\"dave\",\"foobar\":\"pass\"}")));
    }

    @Test
    public void testAnnotated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/dummy2").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(is("{\"name\":\"dave\"}")));
    }

    @Test
    public void testJsonViewResponseEntity() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/dummy3").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(is("{\"name\":\"dave\"}")));
    }

    @Test
    public void testDataView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/dummy4").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(is("{\"name\":\"dave\"}")));
    }

    @EnableWebMvc
    @Configuration
    public static class Config extends WebMvcConfigurerAdapter {

        @Inject
        private ObjectMapper mapper;

        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            super.extendMessageConverters(converters);
            ViewAwareJsonMessageConverter.configureMessageConverters(mapper, converters);
        }

        @Bean
        public ExampleController controller(){
            return new ExampleController();
        }

        @Bean
        public JsonViewHandlerDecorator jsonViewHandlerDecorator(){
            return new JsonViewHandlerDecorator();
        }

    }
}
