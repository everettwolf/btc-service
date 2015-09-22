package com.btc.web.props;

import org.springframework.core.env.MapPropertySource;

public class PropertiesPropertySource extends MapPropertySource {

	public static final String PROP_SOURCE_NAME = "Properties";
    
    public PropertiesPropertySource(IProperty property) {
		super(PROP_SOURCE_NAME, property.propertiesAsMap());
	}
    
}
