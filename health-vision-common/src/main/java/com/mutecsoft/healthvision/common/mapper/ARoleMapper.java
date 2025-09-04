package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mutecsoft.healthvision.common.model.Role;

@Mapper
public interface ARoleMapper {

	List<Role> selectRoleListByUserId(Long userId);

    Role selectRoleByRoleNm(String roleNm);

}
