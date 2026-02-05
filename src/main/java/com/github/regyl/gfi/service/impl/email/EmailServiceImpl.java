package com.github.regyl.gfi.service.impl.email;

import com.github.regyl.gfi.model.smtp.EmailModel;
import com.github.regyl.gfi.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final MailSender emailSender;
    private final Function<EmailModel, SimpleMailMessage> mapper;

    @Override
    public void send(EmailModel model) {
        SimpleMailMessage message = mapper.apply(model);
        emailSender.send(message);
    }
}
