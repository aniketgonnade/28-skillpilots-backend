package com.skilladmin.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.BalanceData;
import com.skilladmin.model.College;
import com.skilladmin.model.Company;
import com.skilladmin.model.Department;
import com.skilladmin.model.PackagesData;
import com.skilladmin.repository.BalanceDataRepository;
import com.skilladmin.repository.CollegeRepository;
import com.skilladmin.repository.CompanyRepository;
import com.skilladmin.repository.PackageRepository;
import com.skilladmin.service.PackageService;

@Service
public class PackaagesServiceImpl implements PackageService{

	@Autowired
	private PackageRepository packageRepository;
	@Autowired
	private BalanceDataRepository balanceDataRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private CollegeRepository collegeRepository;
	
	@Override
	public PackagesData createPackData(PackagesData packagesData) {
		return packageRepository.save(packagesData);
	}


	@Override
	public List<PackagesData> getPackages() {
		return packageRepository.findAll();
	}


	@Override
	public Optional<PackagesData> getPackagesById(Long package_id) {
		return packageRepository.findById(package_id);
		
	}


	@Override
	public void deletePackageById(Long package_id) {
		packageRepository.deleteById(package_id);
	}


	@Override
	public PackagesData assignPackagetoComp(Long package_id,String email_id) {
		
		try {
            Optional<Company> companyOptional = Optional.ofNullable(companyRepository.findByEmail(email_id));
            if (!companyOptional.isPresent()) {
                throw new IllegalArgumentException("Company not found");
            }

            Company company = companyOptional.get();
            Optional<BalanceData> balanceOptional = Optional.ofNullable(balanceDataRepository.getBalanceByuserId(company.getCompany_id()));
            if (!balanceOptional.isPresent()) {
                throw new IllegalArgumentException("Balance data not found"); 
            }

            BalanceData balance = balanceOptional.get();
            Optional<PackagesData> packagesDataOptional = Optional.ofNullable(packageRepository.assignPackage(package_id));
            if (!packagesDataOptional.isPresent()) {
                throw new IllegalArgumentException("Package not found");
            }

            PackagesData packagesData = packagesDataOptional.get();
            balance.setNo_of_dept(packagesData.getNo_of_dept());
            balance.setNo_of_internships(packagesData.getNo_of_internships());
            balance.setPackage_id(packagesData.getPackage_id());
            
            balanceDataRepository.save(balance);
            
            return packagesData;
        } catch (Exception e) {
            throw e; 
        }

}


	@Override
	public PackagesData assignPackagetoColl(Long package_id, String email_id) {
		
		try {
			Optional<College> college = Optional.ofNullable(collegeRepository.findByEmail(email_id));
			if(!college.isPresent()) {
				throw new IllegalArgumentException("College Not found");
			}
		College college2 = college.get();
		Optional<BalanceData> balanceOptional = Optional.ofNullable(balanceDataRepository.getBalanceforCollege(college2.getCollege_id()));
		
		if(!balanceOptional.isPresent()) {
			throw new IllegalArgumentException("Balance Not found");
		}
		BalanceData balanceData = balanceOptional.get();
		
		Optional<PackagesData> packagedata = Optional.ofNullable(packageRepository.assignPackage(package_id));
		
		if(!packagedata.isPresent()) {
			throw new IllegalAccessError("Package Not found");
		}
		PackagesData packagesData = packagedata.get();
		balanceData.setNo_of_dept(packagesData.getNo_of_dept());
		balanceData.setNo_of_internships(packagesData.getNo_of_internships());
		balanceData.setPackage_id(packagesData.getPackage_id());
        balanceData.setUpdation_date(new Date());
        balanceDataRepository.save(balanceData);
        
        return packagesData;
		}
		catch(Exception e) {
			throw e;
		}
		
	}}
