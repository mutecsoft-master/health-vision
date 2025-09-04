package com.mutecsoft.healthvision.api.service;

import java.util.List;

import com.mutecsoft.healthvision.common.model.Role;

public interface RoleService {

    List<Role> selectRoleListByUserId(Long userId);
}
