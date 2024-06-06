package com.example.Teller_Automation.BACKEND.AdminModule.Referral;

import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;

import java.util.List;

public interface ReferralService {

    EntityResponse<?> findById(Long id);

    EntityResponse<?> createRetr(Long tellerId, Referral referral);

    EntityResponse<?> createRepl(Long tellerId, Referral referral);

    EntityResponse<?> approve(Referral referral);

    EntityResponse<?> findByRefId(String id);

    EntityResponse<List<Referral>> getAll();

    EntityResponse<?> getUnapproved();

    EntityResponse<?> approveByRefId(String refId);
}
