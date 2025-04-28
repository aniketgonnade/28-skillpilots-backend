package com.skilladmin.serviceImpl;

import com.skilladmin.model.PlanDetails;
import com.skilladmin.repository.PlanDetailsRepo;
import com.skilladmin.service.PlanDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanDetailsServiceImpl implements PlanDetailsService {
    @Autowired
    private PlanDetailsRepo planDetailsRepo;


    @Override
    public PlanDetails createPlan(PlanDetails planDetails) {

    	
    	
        double gstAmount = planDetails.getAmount() * 0.18;

        double totalAmount = planDetails.getAmount() + gstAmount;
        planDetails.setTotalAmt(totalAmount);
        return planDetailsRepo.save(planDetails);
    }

    @Override
    public List<PlanDetails> getAllPlans() {
        return planDetailsRepo.findAll();
    }

	@Override
	public PlanDetails updatePlan(PlanDetails planDetails) {
	    PlanDetails existingPlan = planDetailsRepo
	            .findById(planDetails.getPlanId())
	            .orElseThrow(() -> new RuntimeException("Plan Not found: " + planDetails.getPlanId()));

	
	        double gstAmount = planDetails.getAmount() * 0.18;
	        double totalAmount = planDetails.getAmount() + gstAmount;
	        existingPlan.setAmount(planDetails.getAmount());
	        existingPlan.setTotalAmt(totalAmount);
	    

	    if (planDetails.getPlanName() != null) {
	        existingPlan.setPlanName(planDetails.getPlanName());
	    }
	    if( planDetails.getUser()!=null) {
	    	existingPlan.setUser(planDetails.getUser());
	    }

	    return planDetailsRepo.save(existingPlan);
	}

}
