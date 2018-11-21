package com.jww.base.am.server.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.base.am.service.SysUserService;
import com.jww.common.core.cache.CacheServiceFactory;
import com.jww.common.core.constant.enums.CacheNamespaceEnum;
import com.jww.common.core.constant.enums.LogOptEnum;
import com.jww.common.core.constant.enums.ResultCodeEnum;
import com.jww.common.core.model.dto.LoginDTO;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.dto.Result;
import com.jww.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
     * @return Result
     * @author wanyong
     * @date 2017-12-27 21:10
     */
    @ApiOperation(value = "获取验证码")
    @GetMapping("/captcha/{captchaId}")
    public Result queryCaptcha(@PathVariable(value = "captchaId", required = false) String captchaId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(116, 37, 4, 5);
        captcha.createCode();
        if (StrUtil.isBlank(captchaId) || !CacheServiceFactory.getCacheService().hasKey(CacheNamespaceEnum.CAPTCHA.value() + captchaId)) {
            captchaId = IdUtil.randomUUID();
            CacheServiceFactory.getCacheService().set(CacheNamespaceEnum.CAPTCHA.value() + captchaId, captcha.getCode(), 120);
        }
        captcha.write(outputStream);
        Map<String, String> map = new HashMap<>(2);
        map.put("captchaId", captchaId);
        map.put("captcha", Base64.encode(outputStream.toByteArray()));
        return ResultUtil.ok(map);
    }

    /**
     * 登陆
     *
     * @param loginDTO 登录对象
     * @return Result
     * @author wanyong
     * @date 2017-11-30 16:14
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    @SysLogOpt(module = "登录接口", value = "用户登录", operationType = LogOptEnum.LOGIN)
    public Result login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info(JSON.toJSONString(loginDTO));
//        // 校验验证码
//        String redisCaptchaValue = (String) CacheUtil.getCache().get(CacheNamespaceEnum.CAPTCHA.value() + loginDTO.getCaptchaId());
//        if (StrUtil.isBlank(redisCaptchaValue)) {
//            throw new LoginException(ResultCodeEnum.LOGIN_FAIL_CAPTCHA_ERROR.message());
//        }
//        if (!redisCaptchaValue.equalsIgnoreCase(loginDTO.getCaptchaValue())) {
//            throw new LoginException(ResultCodeEnum.LOGIN_FAIL_CAPTCHA_ERROR.message());
//        }
//        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginDTO.getAccount(), SecurityUtil.encryptPassword(loginDTO.getPassword()));
//        Subject subject = SecurityUtils.getSubject();
//        try {
//            subject.login(usernamePasswordToken);
//        } catch (LockedAccountException e) {
//            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_LOCKED.getMessage(), e);
//        } catch (DisabledAccountException e) {
//            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_DISABLED.getMessage(), e);
//        } catch (ExpiredCredentialsException e) {
//            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_EXPIRED.getMessage(), e);
//        } catch (UnknownAccountException e) {
//            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_UNKNOWN.getMessage(), e);
//        } catch (IncorrectCredentialsException e) {
//            throw new LoginException(Constants.ResultCodeEnum.LOGIN_FAIL_INCORRECT_CREDENTIALS.getMessage(), e);
//        } catch (Exception e) {
//            throw new LoginException(e);
//        }
//        // 清空验证码缓存
//        CacheUtil.getCache().del(Constants.CacheNamespaceEnum.CAPTCHA.value() + loginDTO.getCaptchaId());
//        // 验证通过，返回前端所需的用户信息
//        SysUserEntity currentUser = (SysUserEntity) WebUtil.getCurrentUser();
//        SysUserEntity sysUserEntity = new SysUserEntity();
//        sysUserEntity.setId(currentUser.getId());
//        sysUserEntity.setAccount(currentUser.getAccount());
//        sysUserEntity.setUserName(currentUser.getUserName());
//        sysUserEntity.setAvatar(currentUser.getAvatar());
//        return ResultUtil.ok(sysUserEntity);
        return ResultUtil.ok();
    }

    /**
     * 登出
     *
     * @return Result
     * @author wanyong
     * @date 2018-01-04 11:36
     */
    @ApiOperation(value = "用户登出")
    @PostMapping("/logout")
    @SysLogOpt(module = "登录接口", value = "用户登出", operationType = LogOptEnum.LOGIN)
    public Result logout() {
//        SecurityUtils.getSubject().logout();
        return ResultUtil.ok();
    }

    /**
     * 未登陆
     *
     * @return Result
     * @author wanyong
     * @date 2017-11-30 16:03
     */
    @RequestMapping(value = "/unlogin", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public Result unlogin() {
        return ResultUtil.fail(ResultCodeEnum.UNLOGIN);
    }

    /**
     * 未授权
     *
     * @return Result
     * @author wanyong
     * @date 2017-11-30 16:03
     */
    @RequestMapping(value = "/unauthorized", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public Result unauthorized() {
        return ResultUtil.fail(ResultCodeEnum.UNAUTHORIZED);
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
    // @RequiresAuthentication
    public Result queryRunasList() {
//        //当前用户不是admin，也不是代理身份，就不能进行切换
//        Long currentUserId = super.getCurrentUserId();
//        if (!AmConstants.USERID_ADMIN.equals(currentUserId)) {
//            Subject subject = SecurityUtils.getSubject();
//            if (!subject.isRunAs()) {
//                throw new BusinessException("对不起，您不能进行身份切换！");
//            }
//        }
        return ResultUtil.ok(sysUserService.listRunas());
    }

    /**
     * 切换身份
     *
     * @param userId 切换后的身份编号
     * @return Result
     * @author shadj
     * @date 2018/1/21 14:29
     */
    @PostMapping("/changeUser")
    // @RequiresAuthentication
    public Result changeUser(@RequestBody Long userId) {
        Assert.notNull(userId);
        //当前用户不是admin，也不是代理身份，就不能进行切换
//        Long currentUserId = super.getCurrentUserId();
//        Subject subject = SecurityUtils.getSubject();
//        if (!AmConstants.USERID_ADMIN.equals(currentUserId)) {
//            if (!subject.isRunAs()) {
//                throw new BusinessException("对不起，您不能进行身份切换！");
//            }
//        }
//        //自己切换为自己，默认提示操作成功
//        if (currentUserId.equals(userId)) {
//            return ResultUtil.ok();
//        }
//        SysUserEntity changeToUser = sysUserService.selectById(userId);
//        SysUserEntity sysUserEntity = new SysUserEntity();
//        sysUserEntity.setId(changeToUser.getId());
//        sysUserEntity.setAccount(changeToUser.getAccount());
//        sysUserEntity.setUserName(changeToUser.getUserName());
//        sysUserEntity.setAvatar(changeToUser.getAvatar());
//        // 如果要切换为admin，则需要判断目前是否为代理身份，如果是则需要循环释放授权
//        if (AmConstants.USERID_ADMIN.equals(userId)) {
//            if (!subject.isRunAs()) {
//                return ResultUtil.ok();
//            }
//            while (subject.isRunAs()) {
//                subject.releaseRunAs();
//            }
//            WebUtil.saveCurrentUser(changeToUser);
//            WebUtil.saveCurrentUserId(changeToUser.getId());
//            return ResultUtil.ok(sysUserEntity);
//        }
//        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
//        String changeToUserName = changeToUser.getAccount();
//        subject.runAs(new SimplePrincipalCollection(changeToUserName, realmName));
//        WebUtil.saveCurrentUser(changeToUser);
//        WebUtil.saveCurrentUserId(changeToUser.getId());
//        sysUserEntity.setIsRunas(1);
//        return ResultUtil.ok(sysUserEntity);
        return null;
    }

}
