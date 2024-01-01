package org.unibl.etf.sni.backend.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private Integer mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String mailStartTLSEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String mailStartTLSRequired;

    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String mailSMTPProtocol;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);

        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailSMTPProtocol);
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailStartTLSEnable);
        props.put("mail.smtp.starttls.required", mailStartTLSRequired);
        props.put("mail.debug", "true");

        return mailSender;
    }

}
