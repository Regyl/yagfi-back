package com.github.regyl.gfi.service.smtp;

import com.github.regyl.gfi.model.smtp.EmailModel;

public interface EmailService {

    void send(EmailModel model);
}
