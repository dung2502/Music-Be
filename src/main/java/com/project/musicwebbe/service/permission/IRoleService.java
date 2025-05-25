package com.project.musicwebbe.service.permission;

import com.project.musicwebbe.entities.permission.AppRole;
import com.project.musicwebbe.service.IGeneralService;

public interface IRoleService extends IGeneralService<AppRole> {
    AppRole findRoleByRoleName(String roleName);
}
