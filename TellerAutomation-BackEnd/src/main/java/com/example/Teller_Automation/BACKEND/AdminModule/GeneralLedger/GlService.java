package com.example.Teller_Automation.BACKEND.AdminModule.GeneralLedger;

import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;

public interface GlService {

    EntityResponse<?> findById(Long id);

    EntityResponse<?> findAll();

    EntityResponse<?> create(Gl gl);

    EntityResponse<?> link(Long id, Long Id);


}

