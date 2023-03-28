package com.example.desporto24.service;

import com.example.desporto24.email.EmailSender;
import com.sun.mail.smtp.SMTPTransport;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static com.example.desporto24.constant.EmailConstant.*;
import static javax.mail.Message.RecipientType.*;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    public void sendNewEmail(String username, String email,String link) throws MessagingException {
        Message message = createEmail(username,email,link);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message,message.getAllRecipients());
        smtpTransport.close();
    }

    private Session getEmailSession(){
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST,GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH,true);
        properties.put(SMTP_PORT,DEFAULT_PORT);
        properties.put(SMTP_STRATTLS_ENABLE,true);
        properties.put(SMTP_STARTTLS_REQUIRED,true);
        return Session.getInstance(properties,null);
    }

    private Message createEmail(String username,String email,String link) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email,false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Olá "+username+". \n \n Sê bemvindo à nossa aplicação! \n \n Para finalizares a tua inscrição na nossa aplicação por favor clica aqui "+link+". \n \n Com os melhores cumprimentos.");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    @Override
    public Message send(String to, String email) throws MessagingException {
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
}
