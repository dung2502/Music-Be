package com.project.musicwebbe.controller.publicController;

import com.project.musicwebbe.entities.VipPackage;
import com.project.musicwebbe.service.VipPackage.impl.VipPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/vip-package")
public class VipPackageController {

    @Autowired
    private VipPackageService vipPackageService;

    @GetMapping("/all")
    public ResponseEntity<List<VipPackage>> getAllVipPackages() {
        List<VipPackage> packages = vipPackageService.getAllVipPackages();
        return ResponseEntity.ok(packages);
    }
}
