package com.example.Teller_Automation.BACKEND.CustomerModule.Transaction;

import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;


public interface TransactionService {

    EntityResponse<?> create(Transaction transaction);

    EntityResponse<?> findById(Long id);

    EntityResponse<?> modify(Transaction transaction);

    EntityResponse<?> getAll();

    EntityResponse<?> getWithdraw();

    EntityResponse<?> getDeposit();

    EntityResponse<?> findByTransactionId(String tranId);

    EntityResponse<?> findTransactionByPf(Long pfNumber);

}
