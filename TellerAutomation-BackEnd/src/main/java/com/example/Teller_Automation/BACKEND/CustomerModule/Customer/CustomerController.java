package com.example.Teller_Automation.BACKEND.CustomerModule.Customer;


import com.example.Teller_Automation.BACKEND.CustomerModule.account.Account;
import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/Customer")
public class CustomerController {

    private final CustomerServiceImp customerService;

    @Autowired
    public CustomerController(CustomerServiceImp customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/get")
    public EntityResponse<?> findById(@RequestParam Long id) {
        return customerService.findById(id);
    }

    @PostMapping("/create")
    public EntityResponse<?> create(@RequestBody Customer customer) {
//        Customer c = new Customer();
//        c.setFirstName(customer.getFirstName());;
        return customerService.create(customer);
    }

    @PostMapping("/addAccount")
    public EntityResponse<?> addAccount(@RequestParam Long id, @RequestBody Account account) {
        return customerService.addAccount(id, account);
    }

    @PutMapping("/modify")
    public EntityResponse<?> modify(@RequestBody Customer customer) {
        return customerService.modify(customer);
    }
//    @GetMapping("/findTransaction")
//    public EntityResponse<?> findTransaction(@RequestParam Long id) {
//        return customerService.findTransaction(id);
//    }

    @GetMapping("/findAccount")
    public EntityResponse<?> findAccount(@RequestParam Long id) {
        return customerService.findAccount(id);
    }

    @GetMapping("/findByNationalId")
    public EntityResponse<?> findByNationalId(@RequestParam String nationalId){
        return customerService.findByNationalId(nationalId);
    }

}
