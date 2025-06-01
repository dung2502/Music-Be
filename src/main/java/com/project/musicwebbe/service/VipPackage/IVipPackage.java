package com.project.musicwebbe.service.VipPackage;

import com.project.musicwebbe.entities.VipPackage;
import com.project.musicwebbe.service.IGeneralService;

import java.util.List;

public interface IVipPackage extends IGeneralService<VipPackage> {
    List<VipPackage> getAllVipPackages();

}
