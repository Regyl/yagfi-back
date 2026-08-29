package com.github.regyl.gfi.mapper.email;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.model.smtp.EmailModel;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mail.SimpleMailMessage;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultUnitTest
class EmailModelToSimpleMailMessageMapperImplTest {

    @InjectMocks
    private EmailModelToSimpleMailMessageMapperImpl emailModelToSimpleMailMessageMapper;

    @Test
    void apply_fullyPopulatedModel_mapsAllFieldsToSimpleMailMessage() {
        EmailModel model = new EmailModel("user@example.com", "Welcome", "Thanks for joining!");

        SimpleMailMessage result = emailModelToSimpleMailMessageMapper.apply(model);

        assertThat(result).isNotNull();
        assertThat(result.getTo()).containsExactly("user@example.com");
        assertThat(result.getSubject()).isEqualTo("Welcome");
        assertThat(result.getText()).isEqualTo("Thanks for joining!");
    }

}