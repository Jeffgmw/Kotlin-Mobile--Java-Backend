package com.example.Teller_Automation.BACKEND.AdminModule.GeneralLedger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GlRepo extends JpaRepository<Gl, Long> {

    Optional<Gl> findByAccno(Long id);


}

