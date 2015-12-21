package com.btc.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by chriswolf on 12/15/15.
 */
@Data
@Component
@ConfigurationProperties("email")
public class NotificationProperties {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private boolean smtpAuth;
    private boolean sslSocketFactory;
}



