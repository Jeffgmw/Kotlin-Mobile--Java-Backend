package com.example.Teller_Automation.BACKEND.AdminModule.Referral;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "referral_id", unique = true)
    private String referralId;
    @Column(name = "referral_type")
    private String referralType;
    private double amount;
    @Column(name = "source_account")
    private Long sourceAcc;
    @Column(name = "destination_account")
    private Long destAcc;
    @Column(name = "is_completed")
    private boolean isCompleted;
    @Column(name = "teller_id")
    @JsonIgnore
    private Long teller_id;
    @Column(name="date")
    private Date date;
    @Column(name = "admin_id")
    private Long admin_id;

    public Referral(Long sourceAcc, Long destAcc, double amount){
        this.sourceAcc = sourceAcc;
        this.destAcc = destAcc;
        this.amount = amount;

    }
}
