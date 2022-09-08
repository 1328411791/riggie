package com.liahnu.riggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liahnu.riggie.common.Results;
import com.liahnu.riggie.pojo.Employee;
import com.liahnu.riggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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
    public Results<String> logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("employee");
        return Results.success("退出成功");
    }

    @PostMapping
    @ResponseBody
    public Results<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("add Employee");

        employee.setPassword(DigestUtils.md5DigestAsHex("1234556".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setUpdateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return Results.success("success");
    }

    @GetMapping("/page")
    public Results<Page> pageResults(int page, int pageSize, String name) {
        log.info("page");
        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);

        queryWrapper.orderByDesc(Employee::getUpdateTime);

        pageInfo = employeeService.page(pageInfo, queryWrapper);

        return Results.success(pageInfo);
    }


}
