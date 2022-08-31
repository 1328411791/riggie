package com.liahnu.riggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liahnu.riggie.common.Results;
import com.liahnu.riggie.pojo.Employee;
import com.liahnu.riggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;


    @PostMapping("login")
    @ResponseBody
    public Results<Employee> login(@RequestBody Employee employee,
                                   HttpServletRequest httpServletRequest){
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername())
                .eq(Employee::getPassword,password);
        Employee emp= employeeService.getOne(queryWrapper);
        if(emp!=null){
            return Results.error("error");
        }
        if(!emp.getPassword().equals(password)) {
            return Results.error("密码错误");
        }
        if(emp.getStatus()==0){
            return Results.error("账号已禁用");
        }

        httpServletRequest.getSession().setAttribute("employee",emp);
        return Results.success(emp);

    }




    @PostMapping("/logout")
    public Results logout(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().removeAttribute("employee");
        return Results.success("退出成功");
    }


}
