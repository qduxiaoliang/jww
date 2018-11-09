var tab;
layui.config({
    base: "js/"
}).use(['base', 'bodyTab', 'form', 'element', 'layer', 'jquery'], function () {
    var base = layui.base,
        form = layui.form,
        layer = layui.layer,
        element = layui.element,
        $ = layui.jquery;

    var headerDom = $(".layui-header");
    var sideDom = $(".layui-side");

    //渲染用户名和头像
    var currentUser = layui.data("JWW_UMP").CUURENT_USER;
    if (typeof(currentUser) === "undefined") {
        window.location.href = "/page/login/login.html";
    }
    var fullName = layui.data("JWW_UMP").CUURENT_USER.fullName;
    $("span[name='fullName']").text(fullName);
    var userId = layui.data("JWW_UMP").CUURENT_USER.id;

    tab = layui.bodyTab({
        openTabNum: "50",  // 最大可打开窗口数量
        url: "index/menuTree" // 获取菜单json地址
    });

    // 获取用户权限
    $.ajax({
        type: "GET",
        url: "index/permissions",
        success: function (data) {
            if (data.code === 200) {
                // 设置用户权限到浏览器本地sessionStorage中
                window.sessionStorage.setItem("JWW_UMP_USER_PERMISSIONS", data.data);
            }
        },
        contentType: "application/json"
    });

    //更换皮肤
    function skins() {
        var skin = window.sessionStorage.getItem("skin");
        if (skin) {  // 如果更换过皮肤
            headerDom.removeClass("layui-bg-black layui-bg-cyan layui-bg-green");
            headerDom.addClass(skin);
            sideDom.removeClass("layui-bg-black layui-bg-cyan layui-bg-green");
            sideDom.addClass(skin);
        }
    }

    skins();
    
    $(".changeSkin").click(function () {
        layer.open({
            title: "更换皮肤",
            area: ["320px", "180px"],
            type: "1",
            content: '<div class="skins_box">' +
            '<form class="layui-form">' +
            '<div class="layui-form-item">' +
            '<input type="radio" name="skin" value="layui-bg-cyan" title="默认" lay-filter="skinFilter" checked="">' +
            '<input type="radio" name="skin" value="layui-bg-black" title="黑色" lay-filter="skinFilter">' +
            '<input type="radio" name="skin" value="layui-bg-green" title="墨绿" lay-filter="skinFilter">' +
            '</div>' +
            '<div class="layui-form-item skinBtn">' +
            '<a href="javascript:;" class="layui-btn layui-btn-small layui-btn-normal" lay-submit="" lay-filter="changeSkin">确定更换</a>' +
            '<a href="javascript:;" class="layui-btn layui-btn-small layui-btn-primary" lay-submit="" lay-filter="noChangeSkin">我再想想</a>' +
            '</div>' +
            '</form>' +
            '</div>',
            success: function (index, layero) {
                var skin = window.sessionStorage.getItem("skin");
                if (skin) {
                    $(".skins_box input[value=" + skin + "]").attr("checked", "checked");
                }
                form.render();
                form.on('radio(skinFilter)', function (data) {
                    headerDom.removeClass("layui-bg-black layui-bg-cyan layui-bg-green");
                    headerDom.addClass(data.value);
                    sideDom.removeClass("layui-bg-black layui-bg-cyan layui-bg-green");
                    sideDom.addClass(data.value);
                });

                form.on("submit(changeSkin)", function (data) {
                    window.sessionStorage.setItem("skin", data.field.skin);
                    layer.closeAll("page");
                });

                form.on("submit(noChangeSkin)", function () {
                    skins();
                    layer.closeAll("page");
                });
            },
            cancel: function () {
                skins();
            }
        })
    });

    //退出
    $(".signOut").click(function () {
        layer.confirm('确定退出系统吗？', {icon: 3, title: '确认'}, function (index) {
            var loadingIndex = base.loading(layer);
            $.ajax({
                type: 'POST',
                url: 'logout',
                success: function (data) {
                    layer.close(loadingIndex);
                    if (data.code === 200) {
                        layui.data('JWW_UMP', {
                            key: 'CUURENT_USER', remove: true
                        });
                        window.sessionStorage.removeItem("menu");
                        menu = [];
                        window.sessionStorage.removeItem("curmenu");
                        window.top.location.href = "/page/login/login.html";
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
            layer.close(index);
        });
    });

    //隐藏左侧导航
    $(".hideMenu").click(function () {
        $(".layui-layout-admin").toggleClass("showMenu");
        //渲染顶部窗口
        tab.tabMove();
    });

    //渲染左侧菜单
    tab.render();

    //锁屏
    function lockPage() {
        layer.open({
            title: false,
            type: 1,
            content: '	<div class="admin-header-lock" id="lock-box">' +
            '<div class="admin-header-lock-img"><img src="images/defaultFace.jpg"/></div>' +
            '<div class="admin-header-lock-name" id="lockUserName">Jww</div>' +
            '<div class="input_btn">' +
            '<input type="password" class="admin-header-lock-input layui-input" autocomplete="off" placeholder="请输入密码解锁.." name="lockPwd" id="lockPwd" />' +
            '<button class="layui-btn" id="unlock">解锁</button>' +
            '</div>' +
            '<p>请输入“123456”，否则不会解锁成功哦！！！</p>' +
            '</div>',
            closeBtn: 0,
            shade: 0.9
        });
        $(".admin-header-lock-input").focus();
    }

    //控制切换身份按钮显示
    if(userId == 1 || currentUser.isRunas == 1){
        $(".changeUser").css('display','block');
    }
    //切换身份
    $(".changeUser").on("click", function () {
        layer.open({
            title: "用户切换",
            type: 2,
            area: ['320px', '300px'],
            fixed: false,
            content: '/page/login/changeUser.html'
        });
    });

    $(".lockcms").on("click", function () {
        window.sessionStorage.setItem("lockcms", true);
        lockPage();
    });
    // 判断是否显示锁屏
    if (window.sessionStorage.getItem("lockcms") === "true") {
        lockPage();
    }
    // 解锁
    $("body").on("click", "#unlock", function () {
        if ($(this).siblings(".admin-header-lock-input").val() === '') {
            layer.msg("请输入解锁密码！", {icon: 0});
            $(this).siblings(".admin-header-lock-input").focus();
        } else {
            if ($(this).siblings(".admin-header-lock-input").val() === "123456") {
                window.sessionStorage.setItem("lockcms", false);
                $(this).siblings(".admin-header-lock-input").val('');
                layer.closeAll("page");
            } else {
                layer.msg("密码错误，请重新输入！", {icon: 2});
                $(this).siblings(".admin-header-lock-input").val('').focus();
            }
        }
    });
    $(document).on('keydown', function () {
        if (event.keyCode === 13) {
            $("#unlock").click();
        }
    });

    //手机设备的简单适配
    var treeMobile = $('.site-tree-mobile'),
        shadeMobile = $('.site-mobile-shade');

    treeMobile.on('click', function () {
        $('body').addClass('site-mobile');
    });

    shadeMobile.on('click', function () {
        $('body').removeClass('site-mobile');
    });

    // 新增新窗口
    $("body").on("click", ".layui-nav .layui-nav-item a", function () {
        //如果不存在子级
        /*if ($(this).siblings().length === 0) {
         addTab($(this));
         $('body').removeClass('site-mobile');  //移动端点击菜单关闭菜单层
         }*/
        if ($(this).attr("data-url") !== undefined) {
            $("#navName").text($(".layui-nav-itemed").children("a:first-child").find("cite").text() + " >> " + $(this).find("cite").text());
            $("#contentIframe").attr("src", $(this).attr("data-url"));
        }
        $(this).parent("li").siblings().removeClass("layui-nav-itemed");
    });

    // 公告层
    /*function showNotice() {
     layer.open({
     type: 1,
     title: "系统公告",
     closeBtn: false,
     area: '310px',
     shade: 0.8,
     id: 'LAY_layuipro',
     btn: ['知道了'],
     moveType: 1,
     content: '<div style="padding:15px 20px; text-align:justify; line-height: 22px; text-indent:2em;border-bottom:1px solid #e2e2e2;"><p>1. 放假通知：2017年12月放假一个月</p></div>',
     success: function (layero) {
     var btn = layero.find('.layui-layer-btn');
     btn.css('text-align', 'center');
     btn.on("click", function () {
     window.sessionStorage.setItem("showNotice", "true");
     });
     if ($(window).width() > 432) {  //如果页面宽度不足以显示顶部“系统公告”按钮，则不提示
     btn.on("click", function () {
     layer.tips('系统公告躲在了这里', '#showNotice', {
     tips: 3
     });
     })
     }
     }
     });
     }*/

    //判断是否处于锁屏状态(如果关闭以后则未关闭浏览器之前不再显示)
    /*if (window.sessionStorage.getItem("lockcms") !== "true" && window.sessionStorage.getItem("showNotice") !== "true") {
     showNotice();
     }
     $(".showNotice").on("click", function () {
     showNotice();
     });*/

    //刷新后还原打开的窗口
    if (window.sessionStorage.getItem("menu") !== null) {
        menu = JSON.parse(window.sessionStorage.getItem("menu"));
        curmenu = window.sessionStorage.getItem("curmenu");
        var openTitle = '';
        for (var i = 0; i < menu.length; i++) {
            openTitle = '';
            if (menu[i].icon) {
                if (menu[i].icon.split("-")[0] === 'icon') {
                    openTitle += '<i class="iconfont ' + menu[i].icon + '"></i>';
                } else {
                    openTitle += '<i class="layui-icon">' + menu[i].icon + '</i>';
                }
            }
            openTitle += '<cite>' + menu[i].title + '</cite>';
            openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="' + menu[i].layId + '">&#x1006;</i>';
            element.tabAdd("bodyTab", {
                title: openTitle,
                content: "<iframe src='" + menu[i].href + "' data-id='" + menu[i].layId + "'></frame>",
                id: menu[i].layId
            });
            //定位到刷新前的窗口
            if (curmenu !== "undefined") {
                if (curmenu === '' || curmenu === "null") {  //定位到后台首页
                    element.tabChange("bodyTab", '');
                } else if (JSON.parse(curmenu).title === menu[i].title) {  //定位到刷新前的页面
                    element.tabChange("bodyTab", menu[i].layId);
                }
            } else {
                element.tabChange("bodyTab", menu[menu.length - 1].layId);
            }
        }
        //渲染顶部窗口
        tab.tabMove();
    }

    //刷新当前
    $(".refresh").on("click", function () {  //此处新增禁止连续点击刷新一是为了降低服务器压力，另外一个就是为了防止超快点击造成chrome本身的一些js文件的报错(不过貌似这个问题还是存在，不过概率小了很多)
        if ($(this).hasClass("refreshThis")) {
            $(this).removeClass("refreshThis");
            $(".clildFrame .layui-tab-item.layui-show").find("iframe")[0].contentWindow.location.reload(true);
            setTimeout(function () {
                $(".refresh").addClass("refreshThis");
            }, 5000)
        } else {
            layer.msg("您刷新速度太快，稍等五秒再刷新吧！", {icon: 0});
        }
    });

    //关闭其他
    $(".closePageOther").on("click", function () {
        if ($("#top_tabs li").length > 2 && $("#top_tabs li.layui-this cite").text() != "后台首页") {
            var menu = JSON.parse(window.sessionStorage.getItem("menu"));
            $("#top_tabs li").each(function () {
                if ($(this).attr("lay-id") !== '' && !$(this).hasClass("layui-this")) {
                    element.tabDelete("bodyTab", $(this).attr("lay-id")).init();
                    //此处将当前窗口重新获取放入session，避免一个个删除来回循环造成的不必要工作量
                    for (var i = 0; i < menu.length; i++) {
                        if ($("#top_tabs li.layui-this cite").text() === menu[i].title) {
                            menu.splice(0, menu.length, menu[i]);
                            window.sessionStorage.setItem("menu", JSON.stringify(menu));
                        }
                    }
                }
            })
        } else if ($("#top_tabs li.layui-this cite").text() === "后台首页" && $("#top_tabs li").length > 1) {
            $("#top_tabs li").each(function () {
                if ($(this).attr("lay-id") !== '' && !$(this).hasClass("layui-this")) {
                    element.tabDelete("bodyTab", $(this).attr("lay-id")).init();
                    window.sessionStorage.removeItem("menu");
                    menu = [];
                    window.sessionStorage.removeItem("curmenu");
                }
            })
        } else {
            layer.msg("没有可以关闭的窗口了@_@", {icon: 2});
        }
        //渲染顶部窗口
        tab.tabMove();
    });

    //关闭全部
    $(".closePageAll").on("click", function () {
        if ($("#top_tabs li").length > 1) {
            $("#top_tabs li").each(function () {
                if ($(this).attr("lay-id") !== '') {
                    element.tabDelete("bodyTab", $(this).attr("lay-id")).init();
                    window.sessionStorage.removeItem("menu");
                    menu = [];
                    window.sessionStorage.removeItem("curmenu");
                }
            })
        } else {
            layer.msg("没有可以关闭的窗口了@_@", {icon: 2});
        }
        //渲染顶部窗口
        tab.tabMove();
    })
});

//打开新窗口
function addTab(_this) {
    tab.tabAdd(_this);
}

