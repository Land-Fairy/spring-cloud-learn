package com.day.springcloud.controller;

import com.day.springcloud.entity.Dept;
import com.day.springcloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Service;
import java.util.List;

@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    @Autowired
    private DiscoveryClient client;

    @PostMapping("/dept/add")
    public boolean add(@RequestBody Dept dept) {
        return deptService.add(dept);
    }

    @GetMapping("/dept/get/{id}")
    public Dept get(@PathVariable ("id") Long id) {
        return deptService.get(id);
    }

    @GetMapping("/dept/list")
    public List<Dept> list() {
        return deptService.list();
    }

    @GetMapping("/dept/discovery")
    public Object discovery(){
        /* 打印所有的 服务 */
        List<String> list = client.getServices();
        System.out.println(list);

        /* 打印 名称为 MICROSERVICECLOUD-DEPT 的服务信息 */
        List<ServiceInstance> srvList = client.getInstances("MICROSERVICECLOUD-DEPT");
        for (ServiceInstance svc: srvList) {
            System.out.println(svc.getServiceId() +"\t" +
                    svc.getHost() + "\t" +
                    svc.getPort()+ "\t" +
                    svc.getUri());
        }
        return this.client;
    }
}
