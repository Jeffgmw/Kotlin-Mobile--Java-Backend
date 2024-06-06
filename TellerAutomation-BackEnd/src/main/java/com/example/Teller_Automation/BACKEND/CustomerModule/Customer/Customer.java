package com.example.Teller_Automation.BACKEND.CustomerModule.Customer;

import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Transaction;
import com.example.Teller_Automation.BACKEND.CustomerModule.account.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
public class Customer {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;
    @Column(name = "national_id")
    private String nationalId;

    @Column(name="phone")
    private String phoneNumber;

    @OneToMany(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name ="customer_id")
 Set<Account> customerAccount;
//
//    @OneToMany(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name ="customer_id")
//    Set<Transaction> customerTransaction;

    public Customer(String firstName, String lastName  ){
     this.firstName = firstName;
    }
















}
