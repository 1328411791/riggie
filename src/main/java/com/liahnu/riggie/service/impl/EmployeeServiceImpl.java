package com.liahnu.riggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liahnu.riggie.dao.EmployeeMapper;
import com.liahnu.riggie.pojo.Employee;
import com.liahnu.riggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee>
        implements EmployeeService {



}
