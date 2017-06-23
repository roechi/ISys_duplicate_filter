package isys.duplicatefilter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

import static java.util.Collections.singletonList;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter messageConverter = new StringHttpMessageConverter();
        messageConverter.setSupportedMediaTypes(singletonList(new MediaType(MediaType.APPLICATION_JSON, Charset.forName("UTF-8"))));
        converters.add(messageConverter);
        super.configureMessageConverters(converters);
    }
}
