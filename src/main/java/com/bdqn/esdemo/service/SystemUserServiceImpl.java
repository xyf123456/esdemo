package com.bdqn.esdemo.service;

import com.bdqn.esdemo.controller.vo.SystemUserVo;
import com.bdqn.esdemo.dao.entity.SystemUser;
import com.bdqn.esdemo.dao.mapper.SystemUserMapper;
import com.bdqn.esdemo.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: SystemUserServiceImpl
 * create by:  xyf
 * description: 用户业务接口实现类
 * create time: 2020/3/10 14:34
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {

    @Autowired
    private SystemUserMapper systemUserMapper;

    /**
     * description: 获取所有的用户
     *
     * @return java.util.List<com.bdqn.esdemo.controller.vo.SystemUserVo>
     * @date 2020/3/10 14:20
     */
    @Override
    public List<SystemUserVo> getUsers() {
        List<SystemUser> systemUsers = new ArrayList<>();
        List<SystemUserVo> systemUserVoList = new ArrayList<>();
        systemUsers = systemUserMapper.selectList(null);
        for (SystemUser systemUser : systemUsers) {
            systemUserVoList.add(this.convertFromModel(systemUser));
        }

        return systemUserVoList;
    }

    /**
     * description: 根据id获取用户
     *
     * @param id
     * @return com.bdqn.esdemo.controller.vo.SystemUserVo
     * @date 2020/3/10 16:22
     */
    @Override
    public SystemUserVo getUsebById(String id) {
        SystemUser systemUser = systemUserMapper.selectById(id);
        if (systemUser != null) {
            return this.convertFromModel(systemUser);
        }
        return null;
    }

    private SystemUserVo convertFromModel(SystemUser systemUser) {
//        处理空值的情况
        if (systemUser == null) {
            return null;
        }
        SystemUserVo systemUserVo = new SystemUserVo();
//        BeanUtils.copyProperties(systemUser, systemUserVo);
        systemUserVo.setAge(systemUser.getAge());
        systemUserVo.setCountry(systemUser.getCountry());
        systemUserVo.setBirthday(DateTimeUtil.dateToStr(systemUser.getBirthday()));
        systemUserVo.setId(systemUser.getId());
        systemUserVo.setName(systemUser.getName());
        return systemUserVo;
    }
}
