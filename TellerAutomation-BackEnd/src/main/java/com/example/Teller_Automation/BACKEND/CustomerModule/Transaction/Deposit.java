package com.example.Teller_Automation.BACKEND.CustomerModule.Transaction;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Entity
@DiscriminatorValue("deposit")
public class Deposit extends Transaction {
    // Getters and setters for specific properties
    @Setter
    private double amount;

    //  public Deposit() {
//    super();
//  }
    private String depositer;
    private String depositerNo;
    private String depositerId;
}

