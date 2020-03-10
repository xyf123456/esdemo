package com.bdqn.esdemo.service;

import com.bdqn.esdemo.controller.vo.SystemUserVo;

import java.util.List;

/**
 * ClassName: SystemUserService
 * create by:  xyf
 * description: 用户业务接口
 * create time: 2020/3/10 14:18
 */
public interface SystemUserService {

    /**
     * description: 获取所有的用户
     * @date  2020/3/10 14:20
     * @param
     * @return java.util.List<com.bdqn.esdemo.controller.vo.SystemUserVo>
     */
    List<SystemUserVo> getUsers();

    /**
     * description: 根据id获取用户
     * @date  2020/3/10 16:22
     * @param	id
     * @return com.bdqn.esdemo.controller.vo.SystemUserVo
     */
    SystemUserVo getUsebById(String id);
}
