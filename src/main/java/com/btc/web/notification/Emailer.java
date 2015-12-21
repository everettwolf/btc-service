package com.btc.web.notification;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by Eric on 9/15/14.
 */
@Component
public class Emailer {

    private final static Logger logger = LoggerFactory.getLogger(Emailer.class);
    private final static Splitter RECIPIENT_LIST_SPLITTER = Splitter.on(CharMatcher.anyOf(";, ")).trimResults().omitEmptyStrings();

    @javax.annotation.Resource
    private transient JavaMailSender javaMailSender;

    @Value("${email.from}")
    private String emailFrom;

    public void sendMessage(String mailTo, String subject, String message, List<DataSource> attachments, Map<String, String> imageResources) throws UnsupportedEncodingException, MessagingException {

        if (Strings.isNullOrEmpty(mailTo)) {
            logger.warn("No valid recipients. Email is not sent.");
        } else {
            sendMessage(parseAddressList(mailTo), subject, message, attachments, imageResources);
        }
    }

    private void sendMessage(String[] mailTo, String subject, String message, List<DataSource> attachments, Map<String, String> imageResources) throws MessagingException, UnsupportedEncodingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        InternetAddress[] internetAddresses = InternetAddress.parse(Joiner.on(",").join(mailTo));

        mimeMessageHelper.setFrom(new InternetAddress(emailFrom));
        mimeMessageHelper.setTo(internetAddresses);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message, true);

        if (attachments != null && attachments.size() > 0) {
            for (DataSource dataSource : attachments) {
                System.out.println("adding " + dataSource.getName());
                mimeMessageHelper.addAttachment(dataSource.getName(), dataSource);
            }
        }

        if (imageResources != null && imageResources.size() > 0) {
            Set<String> keys = imageResources.keySet();
            for (String key : keys) {
                System.out.println("adding image " + key);
                Resource imageResource = new ClassPathResource("META-INF/images/" + imageResources.get(key));
                mimeMessageHelper.addInline(key, imageResource);
            }
        }

        javaMailSender.send(mimeMessageHelper.getMimeMessage());

        logger.info("email sent");
    }

    public static String[] parseAddressList(String addressList) {
        return Iterables.toArray(RECIPIENT_LIST_SPLITTER.split(addressList), String.class);
    }

    public static String renderTemplate(String template, Map<String, Object> model) {
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            template = template.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }
        return template;
    }

    public static String renderTemplate(Resource template, Map<String, Object> model) throws IOException {
        String asString = CharStreams.toString(new InputStreamReader(template.getInputStream(), "UTF-8"));
        return renderTemplate(asString, model);
    }

}
