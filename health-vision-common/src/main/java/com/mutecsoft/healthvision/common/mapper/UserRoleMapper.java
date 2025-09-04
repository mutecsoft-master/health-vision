package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.UserRole;

@Mapper
public interface UserRoleMapper {
    void insertUserRole(UserRole userRole);
}
