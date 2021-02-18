package hu.progmasters.fundraiser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@EnableAsync
public class EmailSendingService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSendingService.class);

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Value("${frontend-url}")
    private String frontendUrl;

    @Autowired
    public EmailSendingService(JavaMailSender javaMailSender, SpringTemplateEngine thymeleafTemplateEngine) {
        this.javaMailSender = javaMailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
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
            //TODO - Review: Ez csak ötletelés, de lehet hogy nem ártana tárolni az üzenetet,
            // ha esetleg nem tudjuk elküldeni, majd később újra próbálni ;)
            logger.error("Email sending FAILED to {}", to, e);
        }
    }

    @Async
    public void sendConfirmationEmail(String to, String confirmationCode, String goalName, double amount) {
        String confirmationLink = frontendUrl + "/transfer-confirmation/" + confirmationCode;

        Context ctx = new Context();
        ctx.setVariable("goalName", goalName);
        ctx.setVariable("amount", amount);
        ctx.setVariable("confirmationCode", confirmationCode);
        ctx.setVariable("confirmationLink", confirmationLink);
        String htmlContent = thymeleafTemplateEngine.process("transfer-confirmation.html", ctx);

        String subject = "Transfer confirmation";
        sendHtmlEmail(to, subject, htmlContent);
    }

}
