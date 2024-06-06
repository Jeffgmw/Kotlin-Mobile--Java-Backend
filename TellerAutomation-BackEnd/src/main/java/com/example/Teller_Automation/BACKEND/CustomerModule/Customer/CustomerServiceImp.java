package com.example.Teller_Automation.BACKEND.CustomerModule.Customer;

import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Transaction;
import com.example.Teller_Automation.BACKEND.CustomerModule.account.Account;
import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerServiceImp implements CustomerService {

    public CustomerRepo customerRepo;
    @Autowired
    public CustomerServiceImp(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }
    @Override
    public EntityResponse<?> create(Customer customer){
            EntityResponse<Customer> res = new EntityResponse<>();
            try{
                Customer c = customerRepo.save(customer);
                res.setMessage("Customer created successfully");
                res.setStatusCode(HttpStatus.CREATED.value());
                res.setEntity(c);
            }
            catch(Exception e){
                Log.error("Exception {}" + e);
                res.setMessage("Customer created successfully");
                res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                res.setEntity(null);
            }
            return res;
        }

    @Override
    public EntityResponse<?> findById(Long id) {
        EntityResponse<Customer> res = new EntityResponse<>();
        try {
            if(id == null){
                res.setMessage("Please enter valid ");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }
            Optional<Customer> c = customerRepo.findById(id);
            if(c.isPresent()){
                Customer cus = c.get();
                res.setMessage("Customer is retrieved successfully");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(cus);
            }
            else{
                res.setMessage("Customer is not found");
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
    public EntityResponse<?> addAccount(Long id, Account account) {
        EntityResponse<Account> res = new EntityResponse<>();
        try{
            if(id == null){
                res.setMessage("Please enter valid id");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);

            }
            Optional<Customer> c = customerRepo.findById(id);
            if(c.isPresent()){
                Customer curr = c.get();

                if(curr.getCustomerAccount() == null){
                    Set<Account> a = new HashSet<>();
                    a.add(account);
                    curr.setCustomerAccount(a);
                }
                else{
                    curr.getCustomerAccount().add(account);
                }
                customerRepo.save(curr);
                res.setMessage(curr.getFirstName() + " added " + account.toString());
                res.setStatusCode(HttpStatus.OK.value());
                res.setEntity(account);}
            else{
                res.setMessage("Customer not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }


        }catch (Exception e){
            res.setMessage("Error was encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }

    @Override
    public EntityResponse<?> modify(Customer customer) {
        EntityResponse<Customer> res = new EntityResponse<>();
        try {
            Optional<Customer> cus = customerRepo.findById(customer.getId());

            if(cus.isPresent()){
                Customer c = cus.get();

                c.setEmail(customer.getEmail());
                c.setPhoneNumber(customer.getPhoneNumber());
                customerRepo.save(c);
                res.setMessage("Customer details updated successfully");
                res.setStatusCode(HttpStatus.RESET_CONTENT.value());
                res.setEntity(c);

        }
            else{
                res.setMessage("Customer not found.");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }
        }
        catch(Exception e){
            Log.error("Exception {}" + e);
            res.setMessage("Error was encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }
//    @Override
//    public EntityResponse<?> findTransaction(Long id) {
//        EntityResponse<Set<Transaction>> res = new EntityResponse<>();
//        try{
//            if(id <=0 ){
//                res.setMessage("Please enter valid id");
//                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//                res.setEntity(null);
//            }
//            Optional<Customer> customer = customerRepo.findById(id);
//
//            if(customer.isPresent()){
//                Customer c = customer.get();
//                res.setMessage("Transactions from customer " + c.getFirstName() + " " + c.getLastName() + " were fetched successfully");
//                res.setStatusCode(HttpStatus.FOUND.value());
//                res.setEntity(c.getCustomerTransaction());
//            }
//            else{
//                res.setMessage("Transactions not found" );
//                res.setStatusCode(HttpStatus.NOT_FOUND.value());
//                res.setEntity(null);
//            }
//        }
//        catch (Exception e){
//            res.setMessage("Error was encountered");
//            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            res.setEntity(null);
//        }
//        return res;
//    }

    @Override
    public EntityResponse<?> findAccount(Long id) {
        EntityResponse<Set<Account>> res = new EntityResponse<>();
        try{
            if(id <=0 ){
                res.setMessage("Please enter valid id");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }
            Optional<Customer> customer = customerRepo.findById(id);

            if(customer.isPresent()){
                Customer c = customer.get();
                res.setMessage("Accounts from customer " + c.getFirstName() + " " + c.getLastName() + " were fetched successfully");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(c.getCustomerAccount());
            }
            else{
                res.setMessage("Customer not found");
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
    public EntityResponse<?> findByNationalId(String nationalId) {
        EntityResponse<Customer> res = new EntityResponse<>();
        try{
            Optional<Customer> customer = customerRepo.findByNationalId(nationalId);

            if(customer.isPresent()){
                Customer c = customer.get();

                res.setEntity(c);
                res.setMessage("Customer found");
                res.setStatusCode(HttpStatus.FOUND.value());
            }
            else{
                res.setMessage("Customer not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }
        }
        catch (Exception e){

            res.setMessage("Error encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }


}



