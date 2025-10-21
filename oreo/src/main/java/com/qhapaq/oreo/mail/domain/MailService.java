package com.oreoinsight.mail.domain;

import com.oreoinsight.common.exception.MailDeliveryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${MAIL_USERNAME}")
    private String from;

    /**
     * Envía un correo con cuerpo HTML o texto plano.
     */
    public void sendMail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            boolean isHtml = body.contains("<html>") || body.contains("<body>");
            helper.setText(body, isHtml);

            mailSender.send(message);
            log.info("Correo enviado exitosamente a {}", to);
        } catch (MessagingException e) {
            log.error("Error al enviar correo a {}: {}", to, e.getMessage());
            throw new MailDeliveryException("No se pudo enviar el correo a " + to);
        }
    }

    /**
     * Carga una plantilla HTML desde /resources/templates y reemplaza variables.
     */
    public String loadTemplate(String templateName) {
        try {
            var resource = new ClassPathResource("templates/" + templateName);
            return Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new MailDeliveryException("Error al cargar plantilla: " + templateName);
        }
    }

    /**
     * Envía un correo HTML usando una plantilla con placeholders.
     */
    public void sendTemplateMail(String to, String subject, String templateName, String[][] placeholders) {
        try {
            String html = loadTemplate(templateName);
            // Reemplazar cada variable ${key} con su valor
            for (String[] kv : placeholders) {
                html = html.replace("${" + kv[0] + "}", kv[1]);
            }
            sendMail(to, subject, html);
        } catch (Exception e) {
            log.error("Error al enviar correo con plantilla a {}: {}", to, e.getMessage());
            throw new MailDeliveryException("No se pudo enviar correo con plantilla a " + to);
        }
    }
}