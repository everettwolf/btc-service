package com.btc.web.config;

import com.btc.web.model.repository.PropertyRepository;
import com.btc.web.props.PropertiesSourceFactory;
import com.btc.web.props.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * Created by Eric on 5/21/15.
 */
@Configuration
public class PropertyConfig {

    private static final String STD_CONFIG_LOCATION = "/META-INF/spring/config.properties";
    private static final String ENV_CONFIG_LOCATION = "/META-INF/spring/config.%s.properties";

    @Autowired
    private StandardEnvironment env;

    private final static Logger logger = LoggerFactory.getLogger(PropertyConfig.class);

    @Autowired
    ApplicationContext context;


    @Bean
    public PropertiesSourceFactory corePropertiesSourceFactory() throws Exception {
        return new PropertiesSourceFactory(getCorePropertyRepository());
    }

    @Bean
    public PropertyService propertyService() throws Exception {

        MutablePropertySources sources = env.getPropertySources();

        sources.addFirst(new ResourcePropertySource(STD_CONFIG_LOCATION));
        String[] profiles = env.getActiveProfiles();
        for (String profile : profiles) {
            String configName = String.format(ENV_CONFIG_LOCATION, profile);
            if (new ClassPathResource(configName).exists() ) {
                logger.info("Creating ResourcePropertySource properties for {}", configName);
                sources.addFirst(new ResourcePropertySource(configName));
            } else {
                logger.info("Resource at {} does not exist", configName);
            }
        }
        sources.addFirst(corePropertiesSourceFactory().newSource());

        PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(sources);
        resolver.setConversionService(env.getConversionService());

        PropertyService service = new PropertyService();
        service.setPropertyResolver(resolver);
        service.setPropertySources(sources);

        return service;
    }

    private PropertyRepository getCorePropertyRepository() {
        return context.getAutowireCapableBeanFactory().getBean(PropertyRepository.class);
    }

}
