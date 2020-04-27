package com.day.springcloud.controller;

import com.day.springcloud.entity.Dept;
import com.day.springcloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    @PostMapping("/dept/add")
    public boolean add(@RequestBody Dept dept) {
        return deptService.add(dept);
    }

    @HystrixCommand(fallbackMethod = "fallBack_Get")
    @GetMapping("/dept/get/{id}")
    public Dept get(@PathVariable ("id") Long id) {
        Dept dept = deptService.get(id);
        if (dept == null) {
            throw new RuntimeException("ID: " + id + "未找到");
        }
        return dept;
    }

    public Dept fallBack_Get(@PathVariable("id") Long id) {
        Dept dept = new Dept();
        dept.setDeptno(id);
        dept.setDname("没有找到");
        dept.setDb_source("无");
        return dept;
    }

    @GetMapping("/dept/list")
    public List<Dept> list() {
        return deptService.list();
    }

}
