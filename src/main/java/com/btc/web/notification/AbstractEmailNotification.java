package com.btc.web.notification;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Eric on 9/15/14.
 */
public abstract class AbstractEmailNotification implements Notification {

    private final static Logger logger = LoggerFactory.getLogger(AbstractEmailNotification.class);

    @Resource
    private Emailer emailer;

    public void send(Map<String, Object> model, String mailTo, String subject, boolean emailEnabled, List<DataSource> attachments, Map<String, String> imageResources) throws MessagingException {

        if (Strings.isNullOrEmpty(mailTo)) {
            logger.info("No recipients, nothing will be sent");
            return;
        }

        logNotification(model, mailTo, subject);

        if (emailEnabled) {
            try {
                emailer.sendMessage(mailTo, getSubject(model, subject), getMessage(model), attachments, imageResources);
            } catch (IOException e) {
                logger.error("Unable to send Notification", e);
            }
        } else {
            logger.info("Emailing is disabled, nothing will be sent");
        }
    }

    private void logNotification(Map<String, Object> model, String mailTo, String subject) {
        logger.info("Email Notification Details:");
        logger.info("To: {}", Arrays.toString(Emailer.parseAddressList(mailTo)));
        logger.info("Subject: {}", getSubject(model, subject));
        try {
            logger.info("Body: \n{}", getMessage(model));
        } catch (IOException e1) {
            logger.error(e1.getMessage());
        }
    }

    public Emailer getEmailer() {
        return emailer;
    }

    public void setEmailer(Emailer emailer) {
        this.emailer = emailer;
    }

    protected abstract String getMessage(Map<String, Object> model) throws IOException;

    protected abstract String getSubject(Map<String, Object> model, String emailAlertSubject);

}
