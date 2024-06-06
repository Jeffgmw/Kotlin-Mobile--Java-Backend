package com.example.Teller_Automation.BACKEND.AdminModule.Referral;
import com.example.Teller_Automation.BACKEND.AdminModule.GeneralLedger.Gl;
import com.example.Teller_Automation.BACKEND.AdminModule.GeneralLedger.GlRepo;
import com.example.Teller_Automation.BACKEND.AdminModule.Teller.Teller;
import com.example.Teller_Automation.BACKEND.AdminModule.Teller.TellerRepo;
import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ReferralServiceImp implements ReferralService {

    @Autowired
    private final ReferralRepo referralRepo;
    @Autowired
    GlRepo glRepo;
    @Autowired
    TellerRepo tellerRepo;

    public ReferralServiceImp(ReferralRepo referralRepo) {
        this.referralRepo = referralRepo;
    }

    @Override
    public EntityResponse<?> findById(Long id) {
        EntityResponse<Referral> res = new EntityResponse<>();
        try {
            Optional<Referral> referral = referralRepo.findById(id);
            if (referral.isPresent()) {
                Referral r = referral.get();
                res.setMessage("Referral found");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(r);
            } else {
                res.setMessage("Referral not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }

        } catch (Exception e) {
            res.setMessage("Error is encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }


    @Override
    public EntityResponse<?> createRetr(Long tellerId, Referral referral) {
        EntityResponse<Referral> res = new EntityResponse<>();
        try {
            Optional<Teller> teller = tellerRepo.findById(tellerId);
            if (teller.isPresent()) {
                Teller t = teller.get();
                Gl g = t.getGl();
                referral.setCompleted(false);
                String refId = "REF" + generateString();
                referral.setReferralId(refId);
                referral.setReferralType("Retrenchment");
                referral.setTeller_id(tellerId);
                Long srcAcc = referral.getSourceAcc();
                Long destAcc = referral.getDestAcc();

                List<Gl> gls = glRepo.findAll();
                boolean srcAccExists = gls.stream().anyMatch(gl -> gl.getAccno().equals(srcAcc));
                boolean destAccExists = gls.stream().anyMatch(gl -> gl.getAccno().equals(destAcc));
//                if(g.getAccno() == srcAcc){
                if (destAccExists) {
                    Referral ref = referralRepo.save(referral);
                    res.setMessage("Referral created successfully.");
                    res.setStatusCode(HttpStatus.CREATED.value());
                    res.setEntity(ref);
                } else {
                    res.setMessage("Destination account does not exist");
                    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    res.setEntity(null);
                }
//            }
//                else {
//                    res.setMessage("Wrong source account");
//                    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//                    res.setEntity(null);
//                }
            }
            else{
                res.setMessage("Teller does not exist");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }
        } catch (Exception e) {
            res.setMessage("Error was encountered: " + e.getMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }

    @Override
    public EntityResponse<?> createRepl(Long tellerId, Referral referral) {
        EntityResponse<Referral> res = new EntityResponse<>();
        try {
            Optional<Teller> teller = tellerRepo.findById(tellerId);
            if (teller.isPresent()) {
                Teller t = teller.get();
                Gl g = t.getGl();
                referral.setCompleted(false);
                String refId = "REF" + generateString();
                referral.setReferralId(refId);
                referral.setReferralType("Replenishment");
                referral.setTeller_id(tellerId);
                Long srcAcc = referral.getSourceAcc();
                Long destAcc = referral.getDestAcc();

                List<Gl> gls = glRepo.findAll();
                boolean srcAccExists = gls.stream().anyMatch(gl -> gl.getAccno().equals(srcAcc));
                //boolean destAccExists = gls.stream().anyMatch(gl -> gl.getAccno().equals(destAcc));
//                if(g.getAccno() == destAcc){
                if (srcAccExists) {
                    Referral ref = referralRepo.save(referral);
                    res.setMessage("Referral created successfully.");
                    res.setStatusCode(HttpStatus.CREATED.value());
                    res.setEntity(ref);
                } else {
                    res.setMessage("Wrong source account");
                    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    res.setEntity(null);
                }}
//                else{
//                    res.setMessage("Wrong destination account");
//                    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//                    res.setEntity(null);
//                }

            else{
                res.setMessage("Teller does not exist");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }} catch (Exception e) {
            res.setMessage("Error was encountered: " + e.getMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }



    @Override
    public EntityResponse<?> approve(Referral referral) {
        EntityResponse<Referral> res = new EntityResponse<>();
        try{
            Optional<Referral> ref = referralRepo.findByReferralId(referral.getReferralId());
            if(ref.isPresent()){
                Referral r = ref.get();
                Optional<Gl> glDes = glRepo.findByAccno(referral.getDestAcc());
                Optional<Gl> glSrc = glRepo.findByAccno(referral.getSourceAcc());
                r.setCompleted(true);
                r.setAmount(referral.getAmount());
                if(glDes.isPresent() && glSrc.isPresent()){
                    Gl des = glDes.get();
                    Gl src = glSrc.get();
                    double srcBaln = src.getBalance();
                    double desBaln = des.getBalance();

                    double dec = srcBaln - referral.getAmount();
                    double inc = desBaln + referral.getAmount();

                    src.setBalance(dec);
                    des.setBalance(inc);
                    referralRepo.save(r);
                    glRepo.save(des);
                    glRepo.save(src);

                    res.setMessage("Referral approved successfully");
                    res.setStatusCode(HttpStatus.OK.value());
                    res.setEntity(r);

                }
                else{
                    res.setMessage("Gls do not exist");
                    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    res.setEntity(null);
                }

            }
            else{
                res.setMessage("Referral does not exist");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
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

    @Override
    public EntityResponse<?> findByRefId(String id){
        EntityResponse<Referral> res = new EntityResponse<>();
        try{
            Optional<Referral> referral = referralRepo.findByReferralId(id);
            if(referral.isPresent()){
                Referral ref = referral.get();
                res.setMessage("Referral found");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(ref);
            }
            else{
                res.setMessage("Referral not found");
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

    private String generateString() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int LENGTH = 8;
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            randomString.append(CHARACTERS.charAt(index));
        }
        return randomString.toString();
    }

    @Override
    public EntityResponse<?> approveByRefId(String refId){
        EntityResponse<Referral> res = new EntityResponse<>();
        try{
            Optional<Referral> referral = referralRepo.findByReferralId(refId);

            if(referral.isPresent()){
                Referral r = referral.get();
                Optional<Gl> glDes = glRepo.findByAccno(r.getDestAcc());
                Optional<Gl> glSrc = glRepo.findByAccno(r.getSourceAcc());
                if(!r.isCompleted()){
                    r.setCompleted(true);
                    if(glDes.isPresent() && glSrc.isPresent()){
                        Gl des = glDes.get();
                        Gl src = glSrc.get();
                        double srcBaln = src.getBalance();
                        double desBaln = des.getBalance();

                        double dec = srcBaln - r.getAmount();
                        double inc = desBaln + r.getAmount();

                        src.setBalance(dec);
                        des.setBalance(inc);
                        referralRepo.save(r);
                        glRepo.save(des);
                        glRepo.save(src);

                        res.setMessage("Referral approved successfully");
                        res.setEntity(r);
                        res.setStatusCode(HttpStatus.OK.value());
                    }
                    else{
                        res.setMessage("Gl Source or Gl Destination ");
                        res.setEntity(null);
                        res.setStatusCode(HttpStatus.NOT_FOUND.value());
                    }}
                else{
                    res.setMessage("Referral already approved");
                    res.setEntity(null);
                    res.setStatusCode(HttpStatus.OK.value());
                }

            }else{
                res.setMessage("Referral does not exist");
                res.setEntity(null);
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
            }

        }
        catch (Exception e){
            res.setMessage("Error encountered");
            res.setEntity(null);
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return res;
    }

    @Override
    public EntityResponse<List<Referral>> getAll(){
        EntityResponse<List<Referral>> res = new EntityResponse<>();
        try{
            List<Referral> referrals = referralRepo.findAll();
            if(!referrals.isEmpty()){
                res.setMessage("All the referrals fetched");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(referrals);
            }
            else{
                res.setMessage("No referrals found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }
        }
        catch (Exception e) {
            res.setMessage("Error encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }

    @Override
    public EntityResponse<?> getUnapproved(){
        EntityResponse<List<Referral>> res = new EntityResponse<>();
        try{
            List<Referral> unapproved = new ArrayList<>();
            List<Referral> referrals = referralRepo.findAll();
            if(!referrals.isEmpty()) {
                for (Referral referral : referrals) {
                    if (!referral.isCompleted()) {
                        unapproved.add(referral);
                    }
                }
                res.setMessage("All the unapproved referrals fetched");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(unapproved);
            }

            else{
                res.setMessage("No referrals found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }
        }
        catch (Exception e) {
            res.setMessage("Error encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }



}



//private long generateRandom() {
//    Random random = new Random();
//    long randomNumber = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
//    return randomNumber;
//}


