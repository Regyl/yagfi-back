package com.github.regyl.gfi.service.email;

import com.github.regyl.gfi.model.smtp.EmailModel;

public interface EmailService {

    void send(EmailModel model);
}
