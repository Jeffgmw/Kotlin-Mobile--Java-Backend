package com.example.Teller_Automation.BACKEND.CustomerModule.account;

import com.example.Teller_Automation.BACKEND.AdminModule.GeneralLedger.Gl;
import com.example.Teller_Automation.BACKEND.AdminModule.GeneralLedger.GlRepo;
import com.example.Teller_Automation.BACKEND.AdminModule.Teller.Teller;
import com.example.Teller_Automation.BACKEND.AdminModule.Teller.TellerRepo;
import com.example.Teller_Automation.BACKEND.CustomerModule.Customer.Customer;
import com.example.Teller_Automation.BACKEND.CustomerModule.Customer.CustomerRepo;
import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Deposit;
import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Transaction;
import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.TransactionRepo;
import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Withdrawal;
import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountServiceImp implements AccountService{

    AccountRepo accountRepo;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    TellerRepo tellerRepo;
    @Autowired
    GlRepo glRepo;

    @Autowired
    public AccountServiceImp(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }


    @Override
    public EntityResponse<?> create(Account account) {
        EntityResponse<Account> res = new EntityResponse<>();
        try{
            Account acc = accountRepo.save(account);
            res.setMessage("Account created successfully");
            res.setStatusCode(HttpStatus.CREATED.value());
            res.setEntity(acc);
        }
        catch(Exception e){
            Log.error("Exception {}" + e);
            res.setMessage("Error encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }

    @Override
    public EntityResponse<?> findById(Long id) {
        EntityResponse<Account> res = new EntityResponse<>();
        try {
            if(id == null){
                res.setMessage("Please enter valid ");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }
            Optional<Account> a = accountRepo.findById(id);
            if(a.isPresent()){
                Account acc = a.get();
                res.setMessage("Account " + acc.getAccno() + " is retrieved successfully");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(acc);
            }
            else{
                res.setMessage("Account is not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }
        }
        catch (Exception e){
            res.setMessage("Error was encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }

    @Override
    public EntityResponse<?> findAll() {
        return null;

    }

    @Override
    public EntityResponse<?> addTransaction(Long id, Transaction transaction) {
        EntityResponse<Transaction> res = new EntityResponse<>();
        try{
            if(id == null){
                res.setMessage("Please enter valid id");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }

            Optional<Account> acc = accountRepo.findById(id);
            if(acc.isPresent()){
                Account a = acc.get();

                if(a.getTransaction() == null){
                    Set<Transaction> t = new HashSet<>();
                    t.add(transaction);
                    a.setTransaction(t);
                }
                else{
                    a.getTransaction().add(transaction);
                }
                accountRepo.save(a);
                res.setMessage("Transaction " + a.getTransaction() + " on " + a.getAccno() + " was initiated successfully");
                res.setStatusCode(HttpStatus.CREATED.value());
                res.setEntity(transaction);

            }
            else{
                res.setMessage("Account not found");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }

        }
        catch (Exception e){
            Log.error("Exception {}" + e);
            res.setMessage("Error was encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);

        }
        return res;
    }
    @Override
    public EntityResponse<?> findTransaction(Long id){
        EntityResponse<Set<Transaction>> res = new EntityResponse<>();
        try{
            if(id <=0 ){
                res.setMessage("Please enter valid id");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }
            Optional<Account> acc = accountRepo.findById(id);

            if(acc.isPresent()){
                Account a = acc.get();
                res.setMessage("Transactions from account " + a.getAccno() + " were fetched successfully");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(a.getTransaction());
            }
            else{
                res.setMessage("Transactions not found" );
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }
        }
        catch (Exception e){
            res.setMessage("Error was encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }
    @Override
    public EntityResponse<?> deposit(Long id, Transaction transaction) {
        EntityResponse<Account> res = new EntityResponse<>();
        try {
            if(id <=0 && transaction == null){
                res.setMessage("Please enter valid transaction details");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }
            else {
                Optional<Account> acc = accountRepo.findById(id);
                if (acc.isPresent()) {
                    Account a = acc.get();


                    if(a.getTransaction()==null){
                        Set<Transaction> s= new HashSet<>();
                        s.add(transaction);
                        a.setTransaction(s);}
                    else{
                        a.getTransaction().add(transaction);
                    }
                    Account save = accountRepo.save(a);
                    res.setMessage("Deposit pending");
                    res.setStatusCode(HttpStatus.OK.value());
                    res.setEntity(save);
                    if(transaction.isCompleted()){

                        double total = a.getBalance() + transaction.getAmount();
                        a.setBalance(total);
                        Account t = accountRepo.save(a);
                    }else{
                        res.setMessage("Transaction not approved");
                        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        res.setEntity(null);
                    }
                }else{
                    res.setMessage("Account not found");
                    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    res.setEntity(null);
                }
            }
        }
        catch(Exception e){
            Log.error("Exception {}" + e);
            res.setMessage("Error encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }
    @Override
    public EntityResponse<?> depositReq(Long id, Deposit deposit) {
        EntityResponse<Transaction> res = new EntityResponse<>();
        try {
            if(id <=0 && deposit == null ){
                res.setMessage("Please enter valid transaction details");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }
            else {
                Optional<Account> acc = accountRepo.findById(id);
                if (acc.isPresent()) {
                    Account a = acc.get();

                    deposit.setCompleted(false);

                    if(a.getTransaction()==null){
                        Set<Transaction> s= new HashSet<>();
                        s.add(deposit);
                        a.setTransaction(s);}
                    else{
                        a.getTransaction().add(deposit);
                    }
                    Account save = accountRepo.save(a);
                    res.setMessage("Deposit request successful");
                    res.setStatusCode(HttpStatus.OK.value());
                    res.setEntity(deposit);

                }else{
                    res.setMessage("Account not found");
                    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    res.setEntity(null);
                }
            }
        }
        catch(Exception e){
            Log.error("Exception {}" + e);
            res.setMessage("Error encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }

    @Override
    public EntityResponse<?> withdrawReq(Long accountId, Withdrawal withdrawal) {
        EntityResponse<Transaction> res = new EntityResponse<>();
        try {
            if (accountId <= 0 || withdrawal.getAmount() <= 0) {
                res.setMessage("Please enter valid details");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return res;
            }

            Optional<Account> accOptional = accountRepo.findById(accountId);
            if (accOptional.isPresent()) {
//                res.setMessage("Account not found");
//                res.setStatusCode(HttpStatus.NOT_FOUND.value());
//                return res;

                Account account = accOptional.get();
                double balance = account.getBalance();
                double withdrawalAmount = withdrawal.getAmount();

                if (balance < withdrawalAmount) {
                    res.setMessage("Insufficient balance");
                    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    return res;
                }
                withdrawal.setCompleted(false);

//            // Create withdrawal transaction
//            withdrawal.setAmount(withdrawalAmount);
//            withdrawal.setDate(new Date());
//            withdrawal.setCompleted(false);
//            withdrawal.setAccount_id(accountId);
//
//            // Save withdrawal transaction
//            transactionRepo.save(withdrawal);
                if(account.getTransaction()==null){
                    Set<Transaction> s= new HashSet<>();
                    s.add(withdrawal);
                    account.setTransaction(s);}
                else{
                    account.getTransaction().add(withdrawal);
                }
                Account save = accountRepo.save(account);
                res.setMessage("Withdrawal request successful");
                res.setStatusCode(HttpStatus.OK.value());
                res.setEntity(withdrawal);

            }else{
                res.setMessage("Account not found");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }
            // Update account balance
            //account.setBalance(balance - withdrawalAmount);
            //account.setTransaction(withdrawal); // Assuming addTransaction method exists
//            if(account.getTransaction() == null){
//                            Set<Transaction> setT = new HashSet<>();
//                            setT.add(withdrawal);
//                            account.setTransaction(setT);
//                        }
//                        else{
//                            account.getTransaction().add(withdrawal);
//                        }

            // Save account
//            accountRepo.save(account);

        } catch (Exception e) {
            res.setMessage("Error encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return res;
    }



//    @Override
//    public EntityResponse<?> withdrawReq(Long accountId, Withdrawal withdrawal) {
//        EntityResponse<Transaction> res = new EntityResponse<>();
//        try {
//            if (accountId <= 0 || withdrawal.getAmount() <= 0) {
//                res.setMessage("Please enter valid details");
//                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//                return res;
//            }
//
//            Optional<Account> accOptional = accountRepo.findById(accountId);
//            if (accOptional.isEmpty()) {
//                res.setMessage("Account not found");
//                res.setStatusCode(HttpStatus.NOT_FOUND.value());
//                return res;
//            }
//
//            Account account = accOptional.get();
//            double balance = account.getBalance();
//            double withdrawalAmount = withdrawal.getAmount();
//
//            if (balance < withdrawalAmount) {
//                res.setMessage("Insufficient balance");
//                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//                return res;
//            }
//
//            // Create withdrawal transaction
//            withdrawal.setAmount(withdrawalAmount);
//            withdrawal.setDate(new Date());
//            withdrawal.setCompleted(false);
//            withdrawal.setAccount_id(accountId);
//
//            // Save withdrawal transaction
//            transactionRepo.save(withdrawal);
//
//            // Update account balance
//            account.setBalance(balance - withdrawalAmount);
//            //account.setTransaction(withdrawal); // Assuming addTransaction method exists
////            if(account.getTransaction() == null){
////                            Set<Transaction> setT = new HashSet<>();
////                            setT.add(withdrawal);
////                            account.setTransaction(setT);
////                        }
////                        else{
////                            account.getTransaction().add(withdrawal);
////                        }
//
//            // Save account
//            accountRepo.save(account);
//
//            res.setMessage("Withdrawal request successful");
//            res.setStatusCode(HttpStatus.CREATED.value());
//            res.setEntity(withdrawal);
//        } catch (Exception e) {
//            res.setMessage("Error encountered");
//            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//
//        return res;
//    }

    public EntityResponse<?> findByAccno(Long accno){
        EntityResponse<Customer> res = new EntityResponse<>();
        try{
            Optional<Account> acc = accountRepo.findByAccno(accno);

            if(acc.isPresent()) {
                Account a = acc.get();

                Optional<Customer> customer = customerRepo.findById(a.getCustomer_id());
                if(customer.isPresent()){
                    Customer c = customer.get();

                res.setMessage("Account details of " + accno + "retrieved successfully");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(c);
            }}
            else{
                res.setMessage("Account is not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }
        }
        catch (Exception e){
            res.setMessage("Error was encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }


    @Override
    public EntityResponse<?> approve(Long tellerid, Transaction transaction) {
        EntityResponse<Transaction> res = new EntityResponse<>();
        try {
            Optional<Transaction> tran = transactionRepo.findByTransactionId(transaction.getTransactionId());
            Optional<Teller> teller = tellerRepo.findById(tellerid);
            if (teller.isPresent() && tran.isPresent()) {
                Transaction trans = tran.get();
                Teller t = teller.get();
                Gl gl = t.getGl();
//
                double amt = transaction.getAmount();

                Optional<Account> acc = accountRepo.findById(trans.getAccount_id());
                if (acc.isPresent()){
                    Account a = acc.get();

                    if (trans.isCompleted()) {
                        res.setMessage("Transaction is already complete");
                        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        res.setEntity(null);
                    } else {
                        trans.setCompleted(true);
                        trans.setTeller_id(tellerid);
                        trans.setAmount(transaction.getAmount());

                        if (trans instanceof Withdrawal) {
                            double total = a.getBalance() - amt;
                            double gltotal = gl.getBalance() -amt;
                            gl.setBalance(gltotal);
                            a.setBalance(total);
                        } else if (trans instanceof Deposit) {
                            double total = a.getBalance() + amt;
                            double gltotal = gl.getBalance() + amt;
                            Deposit depo = (Deposit)trans;

                            gl.setBalance(gltotal);
                            a.setBalance(total);
                        }
                        // Save both transaction and account changes
                        accountRepo.save(a);
                        transactionRepo.save(trans);
                        glRepo.save(gl);
                        res.setMessage("Transaction approved");
                        res.setStatusCode(HttpStatus.OK.value());
                        res.setEntity(trans);
                    }
                } else {
                    res.setMessage("Associated account not found");
                    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    res.setEntity(null);
                }
            } else {
                res.setMessage("Transaction or teller not found");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }
        } catch (Exception e) {
            res.setMessage("Error encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }

    @Override
    public EntityResponse<?> approvedep(Long tellerid, Deposit deposit){
        EntityResponse<Transaction> res = new EntityResponse<>();
        try{
            Optional<Teller> teller = tellerRepo.findById(tellerid);
            Optional<Transaction> dep = transactionRepo.findByTransactionId(deposit.getTransactionId());

            if(teller.isPresent() && dep.isPresent()){
                Teller t = teller.get();
                Transaction d = dep.get();
                Gl gl = t.getGl();
                Double glBaln = gl.getBalance();
                Double amount = deposit.getAmount();
                Optional<Account> account = accountRepo.findById(d.getAccount_id());
                if(account.isPresent()){
                    Account acc = account.get();
                    if(d.isCompleted()){
                        res.setMessage("Deposit transaction is already approved.");
                        res.setStatusCode(HttpStatus.OK.value());
                        res.setEntity(null);
                    }
                    else{
                        Double baln = acc.getBalance();
                        Double total = baln + amount;
                        Double glTotal = glBaln + amount;

                        d.setCompleted(true);
                        d.setAmount(amount);
                        d.setTeller_id(tellerid);

                        acc.setBalance(total);
                        gl.setBalance(glTotal);

                        transactionRepo.save(d);
                        accountRepo.save(acc);
                        glRepo.save(gl);

                        res.setMessage("Transaction approved successfully.");
                        res.setStatusCode(HttpStatus.OK.value());
                        res.setEntity(d);

                    }

                }
                else{
                    res.setMessage("Account not found");
                    res.setEntity(null);
                    res.setStatusCode(HttpStatus.NOT_FOUND.value());
                }
            }
            else{
                res.setMessage("Teller or Transaction not found");
                res.setEntity(null);
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
        }
        catch (Exception e){
            res.setMessage("Error encountered.");
            res.setEntity(null);
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return res;
    }







//    @Override
//    public EntityResponse<?> approve(Long tellerid, Long transid) {
//        EntityResponse<Transaction> res = new EntityResponse<>();
//        try {
//            Optional<Transaction> transaction = transactionRepo.findById(transid);
//            Optional<Teller> teller = tellerRepo.findById(tellerid);
//
//            if (transaction.isPresent() && teller.isPresent()) {
//                Transaction trans = transaction.get();
//                res.setMessage("Transaction found");
//                res.setStatusCode(HttpStatus.FOUND.value());
//                res.setEntity(trans);
//
//                Optional<Account> acc = accountRepo.findById(trans.getAccount_id());
//                if (acc.isPresent()) {
//                    Account a = acc.get();
//
//                    if (trans.isCompleted()) {
//                        res.setMessage("Transaction is already complete");
//                        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//                        res.setEntity(null);
//                    } else {
//                        trans.setCompleted(true);
//                        trans.setTeller_id(tellerid);
//
//                        if (trans instanceof Withdrawal) {
//                            double total = a.getBalance() - trans.getAmount();
//                            a.setBalance(total);
//                        } else if (trans instanceof Deposit) {
//                            double total = a.getBalance() + trans.getAmount();
//                            a.setBalance(total);
//                        }
//                        // Save both transaction and account changes
//                        accountRepo.save(a);
//                        transactionRepo.save(trans);
//                    }
//                } else {
//                    res.setMessage("Associated account not found");
//                    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//                    res.setEntity(null);
//                }
//            } else {
//                res.setMessage("Transaction or teller not found");
//                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//                res.setEntity(null);
//            }
//        } catch (Exception e) {
//            res.setMessage("Error encountered");
//            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            res.setEntity(null);
//        }
//        return res;
//    }




}
