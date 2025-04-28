package com.skilladmin.service;

import java.util.List;
import java.util.Optional;

import com.skilladmin.model.PackagesData;

public interface PackageService {
	
	
	public PackagesData createPackData(PackagesData packagesData);
	
	public List<PackagesData> getPackages();

	public Optional<PackagesData> getPackagesById(Long package_id);
	
	public void deletePackageById(Long package_id);
	
	public PackagesData  assignPackagetoComp(Long package_id,String email_id);
	
	public PackagesData  assignPackagetoColl(Long package_id,String email_id);
	
	
	
	

}
