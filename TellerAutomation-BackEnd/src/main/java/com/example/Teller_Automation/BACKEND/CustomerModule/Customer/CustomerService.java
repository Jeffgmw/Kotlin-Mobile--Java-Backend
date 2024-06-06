package com.example.Teller_Automation.BACKEND.CustomerModule.Customer;

import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Transaction;
import com.example.Teller_Automation.BACKEND.CustomerModule.account.Account;
import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;

public interface CustomerService {
   EntityResponse<?> create(Customer customer);

    EntityResponse<?> findById(Long id);

    EntityResponse<?> addAccount(Long id, Account account);

    EntityResponse<?> modify(Customer customer);

//    EntityResponse<?> findTransaction(Long id);
    EntityResponse<?> findAccount(Long id);



 EntityResponse<?> findByNationalId(String nationalId);
}
