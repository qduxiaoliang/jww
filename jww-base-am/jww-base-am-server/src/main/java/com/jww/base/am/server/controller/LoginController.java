package com.jww.base.am.server.controller;

import com.jww.base.am.api.SysUserService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.model.entity.SysUserEntity;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.common.core.Constants;
import com.jww.common.core.exception.BusinessException;
import com.jww.common.core.exception.LoginException;
import com.jww.common.core.model.LoginModel;
import com.jww.common.core.util.SecurityUtil;
import com.jww.common.redis.util.CacheUtil;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.ResultModel;
import com.jww.common.web.util.ResultUtil;
import com.jww.common.web.util.WebUtil;
import com.xiaoleilu.hutool.captcha.CaptchaUtil;
import com.xiaoleilu.hutool.captcha.CircleCaptcha;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆控制器
 *
 * @author wanyong
 * @date 2017-11-30
 **/
@Slf4j
@RestController
@Api(value = "登录接口", description = "登录接口")
public class LoginController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * z
     * 获取验证码
     *
     * @param captchaId 验证码ID
     * @return ResultModel
     * @author wanyong
     * @date 2017-12-27 21:10
     */
    @ApiOperation(value = "获取验证码")
    @GetMapping("/captcha/{captchaId}")
    public ResultModel queryCaptcha(@PathVariable(value = "captchaId", required = false) String captchaId) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(116, 37, 4, 5);
        captcha.createCode();
        if (StrUtil.isBlank(captchaId) || !CacheUtil.getCache().exists(Constants.CacheNamespaceEnum.CAPTCHA.value() + captchaId)) {
            captchaId = RandomUtil.simpleUUID();
        }
        CacheUtil.getCache().set(Constants.CacheNamespaceEnum.CAPTCHA.value() + captchaId, captcha.getCode(), 120);
        captcha.write(outputStream);
        Map<String, String> map = new HashMap<String, String>(2);
        map.put("captchaId", captchaId);
        map.put("captcha", Base64.encode(outputStream.toByteArray()));
        return ResultUtil.ok(map);
    }

    /**
     * 登陆
     *
     * @param loginModel 登录对象
     * @return ResultModel
     * @author wanyong
     * @date 2017-11-30 16:14
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    @SysLogOpt(module = "登录接口", value = "用户登录", operationType = Constants.LogOptEnum.LOGIN)
    public ResultModel login(@Valid @RequestBody LoginModel loginModel) {
        // 校验验证码
        String redisCaptchaValue = (String) CacheUtil.getCache().get(Constants.CacheNamespaceEnum.CAPTCHA.value() + loginModel.getCaptchaId());
        if (StrUtil.isBlank(redisCaptchaValue)) {
            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_CAPTCHA_ERROR.getMessage());
        }
        if (!redisCaptchaValue.equalsIgnoreCase(loginModel.getCaptchaValue())) {
            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_CAPTCHA_ERROR.getMessage());
        }
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginModel.getAccount(), SecurityUtil.encryptPassword(loginModel.getPassword()));
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
        } catch (LockedAccountException e) {
            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_LOCKED.getMessage(), e);
        } catch (DisabledAccountException e) {
            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_DISABLED.getMessage(), e);
        } catch (ExpiredCredentialsException e) {
            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_EXPIRED.getMessage(), e);
        } catch (UnknownAccountException e) {
            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_UNKNOWN.getMessage(), e);
        } catch (IncorrectCredentialsException e) {
            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_INCORRECT_CREDENTIALS.getMessage(), e);
        } catch (Exception e) {
            throw new LoginException(e);
        }
        // 清空验证码缓存
        CacheUtil.getCache().del(Constants.CacheNamespaceEnum.CAPTCHA.value() + loginModel.getCaptchaId());
        // 验证通过，返回前端所需的用户信息
        SysUserEntity currentUser = (SysUserEntity) WebUtil.getCurrentUser();
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setId(currentUser.getId());
        sysUserEntity.setAccount(currentUser.getAccount());
        sysUserEntity.setUserName(currentUser.getUserName());
        sysUserEntity.setAvatar(currentUser.getAvatar());
        return ResultUtil.ok(sysUserEntity);
    }

    /**
     * 登出
     *
     * @return ResultModel
     * @author wanyong
     * @date 2018-01-04 11:36
     */
    @ApiOperation(value = "用户登出")
    @PostMapping("/logout")
    @SysLogOpt(module = "登录接口", value = "用户登出", operationType = Constants.LogOptEnum.LOGIN)
    public ResultModel logout() {
        SecurityUtils.getSubject().logout();
        return ResultUtil.ok();
    }

    /**
     * 未登陆
     *
     * @return ResultModel
     * @author wanyong
     * @date 2017-11-30 16:03
     */
    @RequestMapping(value = "/unlogin", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public ResultModel unlogin() {
        return ResultUtil.fail(Constants.ResultCodeEnum.UNLOGIN);
    }

    /**
     * 未授权
     *
     * @return ResultModel
     * @author wanyong
     * @date 2017-11-30 16:03
     */
    @RequestMapping(value = "/unauthorized", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public ResultModel unauthorized() {
        return ResultUtil.fail(Constants.ResultCodeEnum.UNAUTHORIZED);
    }

    /**
     * 获取身份切换的用户列表
     *
     * @param
     * @return
     * @author shadj
     * @date 2018/1/20 20:12
     */
    @PostMapping("/queryRunasList")
    @RequiresAuthentication
    public ResultModel queryRunasList() {
        //当前用户不是admin，也不是代理身份，就不能进行切换
        Long currentUserId = super.getCurrentUserId();
        if (!AmConstants.USERID_ADMIN.equals(currentUserId)) {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isRunAs()) {
                throw new BusinessException("对不起，您不能进行身份切换！");
            }
        }
        return ResultUtil.ok(sysUserService.queryRunasList());
    }

    /**
     * 切换身份
     *
     * @param userId 切换后的身份编号
     * @return ResultModel
     * @author shadj
     * @date 2018/1/21 14:29
     */
    @PostMapping("/changeUser")
    @RequiresAuthentication
    public ResultModel changeUser(@RequestBody Long userId) {
        Assert.notNull(userId);
        //当前用户不是admin，也不是代理身份，就不能进行切换
        Long currentUserId = super.getCurrentUserId();
        Subject subject = SecurityUtils.getSubject();
        if (!AmConstants.USERID_ADMIN.equals(currentUserId)) {
            if (!subject.isRunAs()) {
                throw new BusinessException("对不起，您不能进行身份切换！");
            }
        }
        //自己切换为自己，默认提示操作成功
        if (currentUserId.equals(userId)) {
            return ResultUtil.ok();
        }
        SysUserEntity changeToUser = sysUserService.selectById(userId);
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setId(changeToUser.getId());
        sysUserEntity.setAccount(changeToUser.getAccount());
        sysUserEntity.setUserName(changeToUser.getUserName());
        sysUserEntity.setAvatar(changeToUser.getAvatar());
        // 如果要切换为admin，则需要判断目前是否为代理身份，如果是则需要循环释放授权
        if (AmConstants.USERID_ADMIN.equals(userId)) {
            if (!subject.isRunAs()) {
                return ResultUtil.ok();
            }
            while (subject.isRunAs()) {
                subject.releaseRunAs();
            }
            WebUtil.saveCurrentUser(changeToUser);
            WebUtil.saveCurrentUserId(changeToUser.getId());
            return ResultUtil.ok(sysUserEntity);
        }
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        String changeToUserName = changeToUser.getAccount();
        subject.runAs(new SimplePrincipalCollection(changeToUserName, realmName));
        WebUtil.saveCurrentUser(changeToUser);
        WebUtil.saveCurrentUserId(changeToUser.getId());
        sysUserEntity.setIsRunas(1);
        return ResultUtil.ok(sysUserEntity);
    }

}
