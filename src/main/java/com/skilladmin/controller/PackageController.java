package com.skilladmin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skilladmin.model.PackagesData;
import com.skilladmin.service.PackageService;

@RestController
@CrossOrigin(origins = {"*"})
public class PackageController {

    @Autowired
    private PackageService packageService;

    @PostMapping("/create")
    public ResponseEntity<Object> createPackages(@RequestBody PackagesData packagesData) {


        HashMap<String, Object> response = new HashMap<>();

        packagesData.setCreation_date(new Date());
        // packagesData.setExpiration_date(null)

        PackagesData createPackData = packageService.createPackData(packagesData);
        response.put("packgedata", createPackData);
        response.put("msg", "Succes");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/getPackages")
    public ResponseEntity<Object> getPackages() {

        HashMap<String, Object> response = new HashMap<>();
        List<PackagesData> packages = packageService.getPackages();
        if (packages != null) {
            response.put("msg", "Succes");
            response.put("packgedata", packages);
        } else {
            response.put("msg", "Not found");

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/updatePackage")
    public ResponseEntity<String> updatePackages(@RequestParam("package_id") Long package_id, @RequestBody PackagesData packagesDat) {
        return packageService.getPackagesById(package_id).map(packagesData -> {
                    packagesData.setPackage_name(packagesDat.getPackage_name());
                    packagesData.setNo_of_internships(packagesDat.getNo_of_internships());
                    packagesData.setPackage_desc(packagesDat.getPackage_desc());
                    packagesData.setValidity(packagesDat.getNo_of_dept());
                    packagesData.setUpdation_date(new Date());
                    packageService.createPackData(packagesData);
                    return ResponseEntity.ok("Package updated successfully");

                })
                .orElseGet(() -> ResponseEntity.status(404).body("Package not found"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePackage(@RequestParam("package_id") Long package_id) {

        return packageService.getPackagesById(package_id).map(packagesData -> {
                    packageService.deletePackageById(package_id);
                    return ResponseEntity.ok("Package deleted successfully");

                })
                .orElseGet(() -> ResponseEntity.status(404).body("Package not found"));

    }

    @PostMapping("/assign")
    public ResponseEntity<?> assignPackageToCompany(@RequestParam Long packageId, @RequestParam String email_id) {
        try {
            PackagesData assignedPackage = packageService.assignPackagetoComp(packageId, email_id);
            return new ResponseEntity<>(assignedPackage, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while assigning the package.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/assignCol")
    public ResponseEntity<?> assignPackageToColl(@RequestParam Long packageId, @RequestParam String email_id) {
        try {
            PackagesData assignedPackage = packageService.assignPackagetoColl(packageId, email_id);
            return new ResponseEntity<>(assignedPackage, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while assigning the package.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getPackage")
    public ResponseEntity<Object> getPackageByIs(@RequestParam Long package_id) {
        HashMap<String, Object> response = new HashMap<>();

        Optional<PackagesData> packagesById = packageService.getPackagesById(package_id);

        PackagesData packagesData = packagesById.get();
        if (packagesData != null) {
            response.put("msg", "Succes");
            response.put("packgedata", packagesData);
        } else {
            response.put("msg", "Not found");

        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
	

