package com.project.musicwebbe.service.VipPackage.impl;

import com.project.musicwebbe.entities.VipPackage;
import com.project.musicwebbe.repository.VipPackageRepository;
import com.project.musicwebbe.service.VipPackage.IVipPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipPackageService implements IVipPackage {

    @Autowired
    private VipPackageRepository vipPackageRepository;

    @Override
    public List<VipPackage> getAllVipPackages() {
        return vipPackageRepository.findAll();
    }

    @Override
    public List<VipPackage> findAll() {
        return vipPackageRepository.findAll();
    }

    @Override
    public Page<VipPackage> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public VipPackage findById(Long id) {
        return null;
    }

    @Override
    public void save(VipPackage vipPackage) {

    }

    @Override
    public void remove(Long id) {

    }
}
