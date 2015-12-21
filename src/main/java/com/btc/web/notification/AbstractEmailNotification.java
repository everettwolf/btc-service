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

    public void send(Map<String, Object> model, List<DataSource> attachments, Map<String, String> imageResources) throws MessagingException {

        if (Strings.isNullOrEmpty(getMailTo())) {
            logger.info("No recipients, nothing will be sent");
            return;
        }

        logNotification(model);

        if (getEmailEnabled()) {
            try {
                emailer.sendMessage(getMailTo(), getSubject(model), getMessage(model), attachments, imageResources);
            } catch (IOException e) {
                logger.error("Unable to send Notification", e);
            }
        } else {
            logger.info("Emailing is disabled, nothing will be sent");
        }
    }

    private void logNotification(Map<String, Object> model) {
        logger.info("Email Notification Details:");
        logger.info("To: {}", Arrays.toString(Emailer.parseAddressList(getMailTo())));
        logger.info("Subject: {}", getSubject(model));
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

    protected abstract String getSubject(Map<String, Object> model);

    protected abstract String getMailTo();

    protected abstract Boolean getEmailEnabled();
}
