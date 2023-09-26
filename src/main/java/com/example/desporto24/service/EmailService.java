package com.example.desporto24.service;
import com.example.desporto24.email.EmailSender;
import com.sun.mail.smtp.SMTPTransport;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

import static com.example.desporto24.constant.EmailConstant.*;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender {

    private Session getEmailSession(){
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST,GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH,true);
        properties.put(SMTP_PORT,DEFAULT_PORT);
        properties.put(SMTP_STRATTLS_ENABLE,true);
        properties.put(SMTP_STARTTLS_REQUIRED,true);
        return Session.getInstance(properties,null);
    }


    public MimeMessage send(String to, String email) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(getEmailSession());
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(email, true);
        helper.setTo(to);
        helper.setSubject(EMAIL_SUBJECT);
        helper.setFrom(new InternetAddress(FROM_EMAIL));
        helper.setSentDate(new Date());
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        smtpTransport.close();
        return mimeMessage;
    }

    /*
    public void send(String to, String email) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email);
            message.setTo(to);
            emailSender.send(message);
            log.info("Email enviado para {}", to);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }
     */
}

