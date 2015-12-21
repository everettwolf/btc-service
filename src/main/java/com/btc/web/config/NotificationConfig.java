package com.btc.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * Created by Eric on 5/21/15.
 */
@Configuration
class NotificationConfig {

    @Resource
    private NotificationProperties props;

    @Bean
    public JavaMailSender mailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setDefaultEncoding("UTF-8");

        Properties properties = getMailProperties();

        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setSession(buildSession(properties));

        return javaMailSender;
    }

    private Session buildSession(Properties properties) {
        return Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(props.getUsername(), props.getPassword());
                    }
                });
    }

    private Properties getMailProperties() {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", props.getHost());
        properties.put("mail.smtp.socketFactory.port", props.getPort());
        if (props.isSslSocketFactory()) {
            properties.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
        }
        if (props.isSmtpAuth()) {
            properties.put("mail.smtp.auth", "true");
        } else {
            properties.put("mail.smtp.auth", "false");
            properties.put("mail.smtp.starttls.enable", "false");
        }
        properties.put("mail.smtp.port", props.getPort());

        return properties;
    }
}
