package com.btc.web.notification;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Eric on 9/29/14.
 */
@Component
public class EmailAlertNotification extends AbstractEmailNotification {

    @Value("${scheduler.email.alert.subject}")
    private String emailAlertSubject;

    @Value("${scheduler.email.alert.recipient.list}")
    private String emailAlertRecipientList;

    @Value("${scheduler.email.enabled}")
    private Boolean emailEnabled;

    private Resource emailTemplate = new ClassPathResource("META-INF/email/alert_message.html");

    public String getMessage(Map<String, Object> model) throws UnsupportedEncodingException, IOException {
        return Emailer.renderTemplate(emailTemplate, model);
    }

    public Resource getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(Resource emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    @Override
    public String getSubject(Map<String, Object> model) {
        return Emailer.renderTemplate(emailAlertSubject, model);
    }

    @Override
    public String getMailTo() {
        return emailAlertRecipientList;
    }

    @Override
    public Boolean getEmailEnabled() {
        return emailEnabled;
    }


}