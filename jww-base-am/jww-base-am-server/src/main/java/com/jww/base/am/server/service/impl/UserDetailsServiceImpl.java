package com.jww.base.am.server.service.impl;

import com.jww.base.am.model.dos.SysUserDO;
import com.jww.base.am.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 简要描述
 *
 * @author wanyong
 * @date 2018/11/6 21:30
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserDO sysUserDO = sysUserService.getByUsername(username);
        if (sysUserDO == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return null;
    }
}
