package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.Role;

@Mapper
public interface RoleMapper {

    List<Role> selectRoleListByUserId(Long userId);

    Role selectRoleByRoleNm(String roleNm);
}
