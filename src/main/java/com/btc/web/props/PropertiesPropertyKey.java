package com.btc.web.props;

public enum PropertiesPropertyKey implements IPropertyKey {

    SYSTEM_EMAIL_FROM("system.email.from", "noreply@us.usana.com"),
    EMAIL_ENABLED("email.enabled", "false"),
    FILE_STORAGE_ROOT("file.storage.root", "/data/plexus/micro_svc/"),
    SECURITY_LOG_ACTIONS("security.log.actions", "false");


	private String keyname;
	private String defaultValue;
	
	private PropertiesPropertyKey(String name) {
		this.keyname = name;
		this.defaultValue = null;
	}
	
	private PropertiesPropertyKey(String name, String defaultValue) {
		this.keyname = name;
		this.defaultValue = defaultValue;
	}
	
	private PropertiesPropertyKey(String name, Boolean defaultValue) {
		this.keyname = name;
		this.defaultValue = defaultValue.toString();
	}
	
	public String keyName() {
		return keyname;
	}
	
	public String defaultValue() {
		return defaultValue;
	}
}
