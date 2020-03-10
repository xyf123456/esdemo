package com.bdqn.esdemo.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * ClassName: SystemUserVo
 * create by:  xyf
 * description: 请求用户
 * create time: 2020/3/10 14:14
 */
@Data
public class SystemUserVo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 国家
     */
    private String country;

    /**
     * 生日
     */
    private String birthday;
}
