package com.btc.web.props;

public class PropertiesSourceFactory {
	
	private IProperty coreProperty;
	
	public PropertiesSourceFactory(IProperty coreProperty) {
		this.coreProperty = coreProperty;
	}
	
	public PropertiesPropertySource newSource() {
		return new PropertiesPropertySource(coreProperty);
	}

}
