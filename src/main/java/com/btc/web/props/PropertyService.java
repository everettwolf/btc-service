package com.btc.web.props;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import javax.annotation.PostConstruct;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class PropertyService {

	private final static Logger logger = LoggerFactory.getLogger(PropertyService.class);
	
	private PropertySourcesPropertyResolver propertyResolver;
	private MutablePropertySources propertySources;

	@Autowired
	PropertiesSourceFactory corePropertiesSourceFactory;
	
	@PostConstruct
	public void postConstruct() {
		log();
	}

	public synchronized void reloadProperties() {	
		propertySources.remove(PropertiesPropertySource.PROP_SOURCE_NAME);
		propertySources.addFirst(corePropertiesSourceFactory.newSource());
	}

	public void log() {

        if (propertySources == null) {
            return;
        }

		Map<String,Object> sorted = Maps.newTreeMap();

		Iterator<PropertySource<?>> iter = propertySources.iterator();
		while (iter.hasNext()) {
			PropertySource<?> propSource = iter.next();
			if (propSource instanceof EnumerablePropertySource) {
				logger.info("{} Properties:", propSource.getName());
				logger.info("----------------------------");
				EnumerablePropertySource eps = (EnumerablePropertySource) propSource;
				String[] propNames = eps.getPropertyNames();
				for (String property : propNames) {
					if (!sorted.containsKey(property)) {
						sorted.put(property, eps.getProperty(property));
					}
					logger.info("{} = {}", property, eps.getProperty(property));
				}
			}
		}
		
		logger.info("Effective Properties:");
		logger.info("----------------------------");
		for (Map.Entry<String, Object> entry : sorted.entrySet()) {
			logger.info("{} = {}", entry.getKey(), entry.getValue());
		}
	}
	
	private <T> Optional<T> getValue(String propertyName, Class<T> propClass) {
		T result = propertyResolver.getProperty(propertyName, propClass);
		return Optional.fromNullable(result);
	}
	
	private<T> T getValue(IPropertyKey key, Class<T> propClass) {
		Optional<T> propValue = getValue(key.keyName(), propClass);
		return propValue.isPresent() ? propValue.get() : propertyResolver.getConversionService().convert(key.defaultValue(), propClass);
	}

	public String getStringProperty(String propertyName) {
		return getValue(propertyName, String.class).orNull();
	}
	
	public Float getFloatProperty(String propertyName) {
		return getValue(propertyName, Float.class).orNull();
	}
	
	public Double getDoubleProperty(String propertyName) {
		return getValue(propertyName, Double.class).orNull();
	}

	public Integer getIntProperty(String propertyName) {
		return getValue(propertyName, Integer.class).orNull();
	}

	public Long getLongProperty(String propertyName) {
		return getValue(propertyName, Long.class).orNull();
	}
	
	public Boolean getBooleanProperty(String propertyName) {
		return getValue(propertyName, Boolean.class).orNull();
	}

	public Date getDateProperty(String propertyName) throws ParseException {
		return getValue(propertyName, Date.class).orNull();
	}
	
	public String getStringProperty(IPropertyKey propertyKey) {
		return getValue(propertyKey, String.class);
	}

	public Float getFloatProperty(IPropertyKey propertyKey) {
		return getValue(propertyKey, Float.class);
	}
	
	public Double getDoubleProperty(IPropertyKey propertyKey) {
		return getValue(propertyKey, Double.class);
	}

	public Integer getIntProperty(IPropertyKey propertyKey) {
		return getValue(propertyKey, Integer.class);
	}
	
	public Long getLongProperty(IPropertyKey propertyKey) {
		return getValue(propertyKey, Long.class);
	}
	
	public Boolean getBooleanProperty(IPropertyKey propertyKey) {
		return getValue(propertyKey, Boolean.class);
	}

	public File getFileProperty(IPropertyKey propertyKey) {
		return getValue(propertyKey, File.class);
	}
	
	public File getFileProperty(IPropertyKey parentFolderProperty, IPropertyKey childFolder) {
		File parent = getFileProperty(parentFolderProperty);
		String child = getStringProperty(childFolder);
		return new File(parent, child);
	}

	public PropertySourcesPropertyResolver getPropertyResolver() {
		return propertyResolver;
	}

	public void setPropertyResolver(PropertySourcesPropertyResolver propertyResolver) {
		this.propertyResolver = propertyResolver;
	}

	public MutablePropertySources getPropertySources() {
		return propertySources;
	}

	public void setPropertySources(MutablePropertySources propertySources) {
		this.propertySources = propertySources;
	}

	public Date getDateProperty(String propertyName, String datePattern) throws ParseException {
		String dateString = getStringProperty(propertyName);
		if (dateString == null) {
			return null;
		}
		return new SimpleDateFormat(datePattern).parse(dateString);
	}
	
}
