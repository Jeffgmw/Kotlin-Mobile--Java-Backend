package com.example.Teller_Automation.BACKEND.AdminModule.Referral;

import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@RestController
@RequestMapping("api/v1/referral")
public class ReferralController {
    private final ReferralService referralService;

    public ReferralController(ReferralService referralService) {
        this.referralService = referralService;
    }

    @GetMapping("/getById")
    public EntityResponse<?> findById(@RequestParam Long id){
        return referralService.findById(id);
    }
    @PostMapping("/replenish")
    public EntityResponse<?> replenish(@RequestParam Long tellerId,@RequestBody Referral referral){
        return referralService.createRepl(tellerId, referral);
    }
    @PostMapping("/retrench")
    public EntityResponse<?> retrench(@RequestParam Long tellerId,@RequestBody Referral referral){
        return referralService.createRetr(tellerId, referral);
    }
    @PostMapping("/approve")
    public EntityResponse<?> approve(Referral referral) {
        return referralService.approve(referral);
    }
    @GetMapping("/findByRefId")
    public EntityResponse<?> findByRefId(String id){
        return referralService.findByRefId(id);
    }
    @GetMapping("/getAllReferrals")
    public EntityResponse<List<Referral>> getAll(){
        return referralService.getAll();
    }
    @GetMapping("/getUnapprovedReferrals")
    public EntityResponse<?> getUnapproved(){
        return referralService.getUnapproved();
    }
    @PostMapping("/approveByRefId")
    public EntityResponse<?> approveByRefId(@RequestParam String refId){
        return referralService.approveByRefId(refId);
    }


}
