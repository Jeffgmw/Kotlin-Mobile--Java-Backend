package com.example.Teller_Automation.BACKEND.AuthenticationModule.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusDTO {
    private Long sn;
    private String status;
    private String remarks;
}
