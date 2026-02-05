package com.github.regyl.gfi.mapper.email;

import com.github.regyl.gfi.model.smtp.EmailModel;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EmailModelToSimpleMailMessageMapperImpl implements Function<EmailModel, SimpleMailMessage> {

    @Override
    public SimpleMailMessage apply(EmailModel model) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(model.getTo());
        message.setSubject(model.getSubject());
        message.setText(model.getText());
        return message;
    }
}
