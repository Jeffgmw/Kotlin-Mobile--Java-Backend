package com.example.Teller_Automation.BACKEND.CustomerModule.account;


import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Deposit;
import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Transaction;
import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Withdrawal;
import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;

public interface AccountService {

    EntityResponse<?> create(Account account);

    EntityResponse<?> findById(Long id);

    EntityResponse<?> findAll();

    EntityResponse<?> addTransaction(Long id, Transaction transaction);
    EntityResponse<?> findTransaction(Long id);

    EntityResponse<?> deposit(Long id, Transaction transaction);
    EntityResponse<?> depositReq(Long id, Deposit deposit);

    EntityResponse<?> withdrawReq(Long id, Withdrawal withdrawal);

    EntityResponse<?> findByAccno(Long accno);

    EntityResponse<?> approve(Long tellerid, Transaction transaction);

    EntityResponse<?> approvedep(Long tellerid, Deposit deposit);


//    EntityResponse<?> approve(Long tellerid, Transaction transaction);
}
