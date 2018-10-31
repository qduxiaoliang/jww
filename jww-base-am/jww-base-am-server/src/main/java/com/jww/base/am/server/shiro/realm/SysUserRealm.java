package com.jww.base.am.server.shiro.realm;

import com.alibaba.fastjson.JSON;
import com.jww.base.am.api.SysAuthorizeService;
import com.jww.base.am.api.SysUserService;
import com.jww.base.am.model.entity.SysUserEntity;
import com.jww.common.web.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * shiro权限获取
 *
 * @author wanyong
 * @date 2017/11/29 15:00
 */
@Slf4j
@Component
public class SysUserRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysAuthorizeService sysAuthorizeService;

    /**
     * 权限验证
     *
     * @param principals
     * @return AuthorizationInfo
     * @author wanyong
     * @date 2017-12-25 19:52
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Long userId = WebUtil.getCurrentUserId();
        List<String> permissionList = sysAuthorizeService.queryPermissionsByUserId(userId);
        permissionList.stream().forEach(permission -> {
            simpleAuthorizationInfo.addStringPermission(permission);
        });
        log.info("userId:{},simpleAuthorizationInfo:{}", userId, JSON.toJSONString(simpleAuthorizationInfo.getStringPermissions()));
        return simpleAuthorizationInfo;
    }

    /**
     * 登录验证
     *
     * @param token
     * @return AuthenticationInfo
     * @author wanyong
     * @date 2017-12-25 19:51
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        SysUserEntity sysUserEntity = sysUserService.queryByAccount(usernamePasswordToken.getUsername());
        if (null == sysUserEntity) {
            throw new UnknownAccountException();
        }
        if (!sysUserEntity.getPassword().equals(new String(usernamePasswordToken.getPassword()))) {
            throw new IncorrectCredentialsException();
        }
        WebUtil.saveCurrentUser(sysUserEntity);
        WebUtil.saveCurrentUserId(sysUserEntity.getId());
        return new SimpleAuthenticationInfo(sysUserEntity.getAccount(), sysUserEntity.getPassword(), sysUserEntity.getUserName());
    }
}