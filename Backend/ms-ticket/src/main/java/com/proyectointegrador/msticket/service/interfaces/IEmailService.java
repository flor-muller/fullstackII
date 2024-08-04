package com.proyectointegrador.msticket.service.interfaces;

import com.proyectointegrador.msticket.dto.EmailDTO;
import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendMail(EmailDTO emailDTO) throws MessagingException;
}
