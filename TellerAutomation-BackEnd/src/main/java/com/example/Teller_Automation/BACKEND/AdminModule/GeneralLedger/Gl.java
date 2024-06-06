package com.example.Teller_Automation.BACKEND.AdminModule.GeneralLedger;

import com.example.Teller_Automation.BACKEND.AdminModule.Teller.Teller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "GeneralLedger")
@AllArgsConstructor
@NoArgsConstructor
public class Gl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "account_number")
    private Long accno;

    private double balance;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gl")
    private Teller teller;
}

