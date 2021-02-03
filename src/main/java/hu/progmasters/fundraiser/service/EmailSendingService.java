package hu.progmasters.fundraiser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@EnableAsync
public class EmailSendingService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSendingService.class);

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSendingService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendHtmlEmail(String to, String body, String topic) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom("fundraiser.progmasters@gmail.com");
            helper.setTo(to);
            helper.setSubject(topic);
            helper.setText(body, true); // true = text/html
            javaMailSender.send(msg);
            logger.info("Email successfully sent to {}", to);
        } catch (MessagingException e) {
            logger.error("Email sending FAILED to {}", to, e);
        }
    }

}
