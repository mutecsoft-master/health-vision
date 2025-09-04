package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mutecsoft.healthvision.common.model.UserRole;

@Mapper
public interface AUserRoleMapper {
    void insertUserRole(UserRole userRole);
    
    void deleteRoleByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
