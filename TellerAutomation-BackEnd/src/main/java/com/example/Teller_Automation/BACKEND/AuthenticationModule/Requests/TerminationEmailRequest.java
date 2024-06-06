package com.example.Teller_Automation.BACKEND.AuthenticationModule.Requests;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TerminationEmailRequest {
    @NotBlank
    private String emailType;

    @NotBlank
    @Email
    private String recipientEmail;
}
