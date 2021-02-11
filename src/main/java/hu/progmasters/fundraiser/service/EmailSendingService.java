package hu.progmasters.fundraiser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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

    //TODO - Review: Ez működik a szerveren is? Ha nem, akkor a deploy leírásban van egy GYIK, arról,
    // hogy tudsz resource-ból fájlt beolvasni ( a vége felé )

    @Value("${frontend-url}")
    private String frontendUrl;

    @Autowired
    public EmailSendingService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendHtmlEmail(String to, String subject, String body) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom("fundraiser.progmasters@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = text/html
            javaMailSender.send(msg);
            logger.info("Email successfully sent to {}", to);
        } catch (MessagingException e) {
            logger.error("Email sending FAILED to {}", to, e);
        }
    }

    @Async
    public void sendConfirmationEmail(String to, String confirmationCode, String goalName, int amount) {
        String confirmationLink = frontendUrl + "/transfer-confirmation/" + confirmationCode;
        String htmlContent = String.format(emailTemplate, confirmationCode, goalName, amount, confirmationLink);
        String subject = "Transfer confirmation";
        sendHtmlEmail(to, subject, htmlContent);
    }

    private final String emailTemplate =
            "<p style=\"background-color:DodgerBlue;font-size:50px;font-weight:bold;\">PROGmasters Fundraiser</p>" +
            "<p>A transfer was initiated from your account. Your confirmation code is <b>%s</b>. " +
            "If you really want to support %s with %d, please type this code on the confirmation page or click the link below:</p>" +
            "<p style=\"font-size:20px;font-weight:bold;\">%s</p>";

}
