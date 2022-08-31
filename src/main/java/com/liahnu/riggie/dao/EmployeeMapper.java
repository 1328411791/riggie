package com.liahnu.riggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liahnu.riggie.pojo.Category;
import com.liahnu.riggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {


}
