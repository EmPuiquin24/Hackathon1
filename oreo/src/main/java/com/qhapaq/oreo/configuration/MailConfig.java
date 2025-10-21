package com.oreoinsight.mail.domain;

import com.oreoinsight.common.exception.MailDeliveryException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${MAIL_USERNAME}")
    private String from;

    /**
     * Envía un correo simple (texto o HTML) al destinatario.
     *
     * @param to      Destinatario
     * @param subject Asunto del correo
     * @param body    Cuerpo del mensaje (texto plano o HTML)
     */
    public void sendMail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            // Detecta si el cuerpo parece HTML
            boolean isHtml = body.contains("<html>") || body.contains("<body>");
            helper.setText(body, isHtml);

            mailSender.send(message);
            log.info("Correo enviado exitosamente a {}", to);
        } catch (MessagingException e) {
            log.error("Error al enviar correo a {}: {}", to, e.getMessage());
            throw new MailDeliveryException("No se pudo enviar el correo a " + to);
        }
    }
}