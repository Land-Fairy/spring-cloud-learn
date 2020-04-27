package com.day.springcloud.entity.service;

import com.day.springcloud.entity.Dept;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 千万不要忘记添加
 */
@Component
public class DeptClientServiceFallbackFactory implements FallbackFactory<DeptClientService> {
    public DeptClientService create(Throwable cause) {
        return new DeptClientService() {
            public Dept get(long id) {
                Dept dept = new Dept();
                dept.setDname("未找到信息，客户端提供的降级");
                return dept;
            }

            public List<Dept> list() {
                return null;
            }

            public boolean add(Dept dept) {
                return false;
            }
        };
    }
}
