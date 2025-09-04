package com.mutecsoft.healthvision.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.api.service.RoleService;
import com.mutecsoft.healthvision.common.mapper.RoleMapper;
import com.mutecsoft.healthvision.common.model.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    public List<Role> selectRoleListByUserId(Long userId) {
        return roleMapper.selectRoleListByUserId(userId);
    }
}
