package com.example.Teller_Automation.BACKEND.AuthenticationModule.Requests;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PasswordResetRequest {
    private String emailAddress;
    private String oldPassword;
    private String password;
    private String confirmPassword;

    public byte[] getOldPassword() {
        return null;
    }

}
