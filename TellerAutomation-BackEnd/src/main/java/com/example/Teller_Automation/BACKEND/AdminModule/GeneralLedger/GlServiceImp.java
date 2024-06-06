package com.example.Teller_Automation.BACKEND.AdminModule.GeneralLedger;

import com.example.Teller_Automation.BACKEND.AdminModule.Teller.Teller;
import com.example.Teller_Automation.BACKEND.AdminModule.Teller.TellerRepo;
import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
@Service
public class GlServiceImp implements GlService{

    private final GlRepo glRepo;
    private final TellerRepo tellerRepo;
    @Autowired
    public GlServiceImp(GlRepo glRepo, TellerRepo tellerRepo) {
        this.glRepo = glRepo;
        this.tellerRepo = tellerRepo;
    }

    @Override
    public EntityResponse<?> findById(Long id) {
        EntityResponse<Gl> res = new EntityResponse<>();
        try{
            Optional<Gl> gl = glRepo.findById(id);
            if(gl.isPresent()){
                Gl g = gl.get();

                res.setMessage("Gl found successfully");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(g);
            }
            else{
                res.setMessage("Gl not found successfully");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }
        }
        catch(Exception e){
            res.setMessage("Error was encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return  res;
    }

    @Override
    public EntityResponse<?> findAll() {
        EntityResponse<List<Gl>> res = new EntityResponse<>();
        try{
            List<Gl> glList = glRepo.findAll();
            if(!glList.isEmpty()){

                res.setMessage("Gl List found successfully");
                res.setStatusCode(HttpStatus.FOUND.value());
                res.setEntity(glList);
            }
            else{
                res.setMessage("Gl List not found successfully");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                res.setEntity(null);
            }
        }
        catch(Exception e){
            res.setMessage("Error was encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }

    @Override
    public EntityResponse<?> create(Gl gl) {
        EntityResponse<Gl> res = new EntityResponse<>();
        try{
            if(gl == null){
                res.setMessage("Please enter valid gl details");
                res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                res.setEntity(null);
            }
            else{
                glRepo.save(gl);
                res.setMessage("Gl created successfully");
                res.setStatusCode(HttpStatus.CREATED.value());
                res.setEntity(gl);
            }
        }catch(Exception e){
            res.setMessage("Error was encountered");
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setEntity(null);
        }
        return res;
    }
    @Override
    public EntityResponse<?> link(Long id, Long Id){
        EntityResponse<?> res = new EntityResponse<>();
        try{
            Optional<Gl> gl = glRepo.findById(id);

            Optional<Teller> teller = tellerRepo.findById(Id);

            if(gl.isPresent() && teller.isPresent()){
                Gl g = gl.get();
                Teller t = teller.get();

                g.setTeller(t);
                t.setGl(g);
                tellerRepo.save(t);
                glRepo.save(g);
                res.setMessage("Linkage was successful");
                res.setEntity(null);
                res.setStatusCode(HttpStatus.OK.value());
            }
            else{
                res.setMessage("Linkage not successful");
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
}

