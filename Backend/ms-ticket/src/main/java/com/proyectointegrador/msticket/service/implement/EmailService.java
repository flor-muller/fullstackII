package com.proyectointegrador.msticket.service.implement;

import com.proyectointegrador.msticket.dto.EmailDTO;
import com.proyectointegrador.msticket.service.interfaces.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Override
    public void sendMail(EmailDTO emailDTO) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(emailDTO.getReceiver());
            helper.setSubject(emailDTO.getSubject());
            Context context = new Context();
            context.setVariable("message", emailDTO.getMessage());
            String contentHTML = templateEngine.process("email", context);
            helper.setText(contentHTML, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}
