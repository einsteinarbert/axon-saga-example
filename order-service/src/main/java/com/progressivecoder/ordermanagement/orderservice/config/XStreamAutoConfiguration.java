package com.progressivecoder.ordermanagement.orderservice.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Optional;

/**
 * Project: saga-pattern-axon-spring-boot-sample.<br/>
 * Des: <br/>
 * User: HieuTT<br/>
 * Date: 25/10/2022<br/>
 */
@Configuration
public class XStreamAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "ch.frankel.boot.xstream.json", havingValue = "true")
    public HierarchicalStreamDriver driver() {
        return new JsonHierarchicalStreamDriver();
    }

    @Bean
    @ConditionalOnMissingBean(XStream.class)
    public XStream xStream(Optional<HierarchicalStreamDriver> driver) {
        return new XStream(driver.orElse(new XppDriver()));
    }

    @Configuration
    public static class XStreamConverterAutoConfiguration {

        private final XStream xstream;

        private final Collection<Converter> converters;

        public XStreamConverterAutoConfiguration(XStream xstream, Collection<Converter> converters) {
            this.xstream = xstream;
            this.converters = converters;
        }

        @PostConstruct
        public void registerConverters() {
            converters.forEach(xstream::registerConverter);
            xstream.allowTypesByWildcard(new String[] {
                    "com.progressivecoder.ecommerce.**"
            });
        }
    }
}
