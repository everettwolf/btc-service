package com.btc.web.notification;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

/**
 * Created by Eric on 9/15/14.
 */
interface Notification {
    void send(Map<String, Object> model, String mailTo, String subject, boolean emailEnabled, List<DataSource> attachments, Map<String, String> imageResources) throws MessagingException;
}
