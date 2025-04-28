package com.skilladmin.service;

import com.skilladmin.model.PlanDetails;

import java.util.List;

public interface PlanDetailsService {

    public PlanDetails createPlan(PlanDetails planDetails);

    public List<PlanDetails> getAllPlans();
    
    public PlanDetails updatePlan(PlanDetails planDetails);
}
