package com.example.Teller_Automation.BACKEND.CustomerModule.account;

import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Deposit;
import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Transaction;
import com.example.Teller_Automation.BACKEND.CustomerModule.Transaction.Withdrawal;
import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {

    private final AccountServiceImp accountServices;

    public AccountController(AccountServiceImp accountServices) {
        this.accountServices = accountServices;
    }

    @PostMapping("/create")
    public EntityResponse<?> create(Account account) {
        return accountServices.create(account);
    }

    @PostMapping("addTransaction")
    public EntityResponse<?> addTransaction(@RequestParam Long id, @RequestBody Transaction transaction) {
        return accountServices.addTransaction(id, transaction);
    }

    @GetMapping("/get")
    public EntityResponse<?> findById(@RequestParam Long id) {
        return accountServices.findById(id);
    }
    @GetMapping("/getTransaction")
    public EntityResponse<?> findTransaction(@RequestParam Long id){
        return accountServices.findTransaction(id);
    }

    @PostMapping("/deposit")
    public EntityResponse<?> depositReq(@RequestParam Long id, @RequestBody Deposit deposit){
        return accountServices.depositReq(id, deposit);

    }

    @PostMapping("/withdrawReq")
    public EntityResponse<?> withdrawReq (@RequestParam Long id, @RequestBody Withdrawal withdrawal){
        return accountServices.withdrawReq(id, withdrawal);
    }

    @GetMapping("/findByAccno")
    public EntityResponse<?> findByAccno(@RequestParam Long accno){
        return accountServices.findByAccno(accno);}

    @PostMapping("/approve")
    public EntityResponse<?> approve(@RequestParam Long tellerid, @RequestBody Withdrawal withdraw) {
        return accountServices.approve(tellerid, withdraw);
    }

    @PostMapping("/approveDep")
    EntityResponse<?> approvedep(@RequestParam Long tellerid, @RequestBody Deposit deposit){
        return accountServices.approvedep(tellerid, deposit);
    }


//    @PostMapping("/approve")
//    public EntityResponse<?> approve(Long tellerid, Long transid) {
//        return accountServices.approve(tellerid, transid);
//    }
}