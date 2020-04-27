package com.day.springcloud.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/* 这个注解的作用？？ */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class Dept implements Serializable {
    private Long deptno;
    private String dname;
    /* 来自哪个数据库(由于微服务架构，可以一个服务对应一个数据库) */
    private String db_source;
}
