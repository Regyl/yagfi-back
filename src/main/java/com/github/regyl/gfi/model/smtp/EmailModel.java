package com.github.regyl.gfi.model.smtp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailModel {

    private final String to;
    private final String subject;
    private final String text;
}
