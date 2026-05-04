package ch.zhaw.freelancer4u.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ch.zhaw.freelancer4u.model.Mail;

@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendMail(Mail mail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail.getTo());
            message.setSubject(mail.getSubject());
            message.setText(mail.getMessage());
            mailSender.send(message);
            logger.info("Mail erfolgreich versendet an: {}", mail.getTo());
            return true;
        } catch (Exception e) {
            logger.error("Fehler beim Versenden der Mail an {}: {}", mail.getTo(), e.getMessage());
            return false;
        }
    }
}
