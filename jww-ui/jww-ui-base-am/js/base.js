/*
 @Author: wanyong
 @Time: 2017-12-03
 @Tittle: base
 @Description: 基础配置
 */
layui.define(['jquery'], function (exports) {
    var $ = layui.jquery;

    // 设置屏幕缩放比
    function setScreenZoom() {
        var ratio = 1;
        var screenWidth = window.screen.width;
        if (screenWidth > 1400 && screenWidth <= 1600) {
            ratio = ratio * 0.9; // 缩放90%
        } else if (screenWidth > 1280 && screenWidth <= 1400) {
            ratio = ratio * 0.8; // 缩放80%
        } else if (screenWidth <= 1280) {
            ratio = ratio * 0.7; // 缩放70%
        }
        document.body.style.zoom = ratio;
        document.body.style.cssText += '; -moz-transform: scale(' + ratio + ');-moz-transform-origin: 0 0; ';
    }

    setScreenZoom();

    // 初始化页面权限
    var permissions = window.sessionStorage.getItem("JWW_UMP_USER_PERMISSIONS");
    var permissionDoms = $("permission");
    permissionDoms.each(function () {
        if (permissions && permissions.indexOf($(this).attr("value")) < 0) {
            $(this).hide();
        }
    });

    // 设置jquery的ajax请求
    $.ajaxSetup({
        dataType: 'json',
        contentType: "application/json",
        beforeSend: function (evt, request, settings) {
            // request.url = 'http://localhost:8090/' + request.url;
            request.url = '/' + request.url;
        },
        dataFilter: function (result) {
            try {
                var resultObj = JSON.parse(result);
                // 没有登录
                if (resultObj.code === 401) {
                    window.top.location.href = "/page/login/login.html";
                    return null;
                }
                return result;
            } catch (e) {
                return result;
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            switch (jqXHR.status) {
                case (404):
                    console.log("未找到请求的资源");
                    break;
                case (405):
                    window.location.href = "page/login/login.html";
                    break;
            }
        }
    });

    var obj = {
        loading: function (layer) {
            return layer.msg('数据请求中...', {
                icon: 16,
                shade: 0.01,
                time: 60000
            });
        },
        fastClickCheck: function (elem) {
            if ($(elem).attr("clickPassValue") === "1") {
                $(elem).attr("clickPassValue", "0");
                setTimeout(function () {
                    $(elem).attr("clickPassValue", "1");
                }, 2000);
                return true;
            } else {
                layui.layer.tips("您点击太快，稍等两秒再点击吧！", elem, {tips: [3, '#009688'], time: 1000});
                return false;
            }
        },
        // 根据参数名称，获取window.location.href的参数
        getUrlParam: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r !== null)return decodeURI(r[2]);
            return null;
        },
        successMsg: function (layer, msg) {
            return layer.msg(msg, {icon: 1, time: 2000});
        },
        errorMsg: function (layer, msg) {
            return layer.msg(msg, {icon: 2, time: 2000});
        },
        normalMsg: function (layer, msg) {
            return layer.msg(msg, {icon: 0, time: 2000});
        },
        isAdmin: function () {
            var permissions = window.sessionStorage.getItem("JWW_UMP_USER_PERMISSIONS");
            if (permissions && permissions.indexOf('sys:admin') >= 0) {
                return true;
            }
            return false;
        }
    };

    exports("base", obj);
});