package com.example.Teller_Automation.BACKEND.AdminModule.Referral;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReferralRepo extends JpaRepository<Referral, Long> {
    Optional<Referral> findByReferralId(String refId);
}
