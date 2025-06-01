package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.VipPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VipPackageRepository extends JpaRepository<VipPackage, Long> {

}
