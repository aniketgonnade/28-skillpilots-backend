package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skilladmin.model.BalanceData;

public interface BalanceDataRepository extends JpaRepository<BalanceData, Long>{
	
	@Query("Select b from BalanceData b where b.user_id=:userId and b.balancefor='Compony' ")
	public BalanceData getBalanceByuserId(@Param("userId") Long user_id);
	
	@Query("Select b from BalanceData b where b.user_id=:userId and b.balancefor='College' ")
	public BalanceData getBalanceforCollege(@Param("userId") Long user_id);

}
