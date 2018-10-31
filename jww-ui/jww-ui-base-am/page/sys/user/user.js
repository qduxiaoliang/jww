var $;
layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'laypage'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = parent.pageOperation === 1 ? "user/add" : "user/modify";

    if (parent.pageOperation === 1 || parent.pageOperation === 2) {
        //日期
        laydate.render({
            elem: '#birthDay'
        });

        form.on('submit(addUser)', function (data) {
            if (typeof(data.field.enable) === "undefined" || data.field.enable === 'undefined') {
                data.field.enable = 0;
            }

            var role = [];
            $('input[name="role"]:checked').each(function (index, element) {
                role[index] = $(this).val();
            });
            data.field.role = role;

            var loadingIndex = base.loading(layer);
            $.ajax({
                type: 'POST',
                url: submitUrl,
                data: JSON.stringify(data.field),
                success: function (data) {
                    if (data.code === 200) {
                        layer.close(loadingIndex);
                        if (parent.pageOperation === 1) {
                            base.successMsg(layer, '用户新增成功');
                            layer.closeAll("iframe");
                            parent.document.getElementById("search").click();
                        } else if (parent.pageOperation === 2) {
                            setTimeout(function () {
                                base.successMsg(layer, '用户修改成功');
                                layer.closeAll("iframe");
                                parent.document.getElementById("search").click();
                            }, 500);
                        }
                    } else {
                        top.layer.close(index);
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });

            //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            return false;
        });

        $("#deptName").click(function () {
            layui.layer.open({
                type: 2,
                title: '选择部门',
                shadeClose: true,
                shade: 0.5,
                area: ['320px', '70%'],
                content: '/page/sys/dept/deptTree.html' //iframe的url
            });
        });

        form.verify({
            account: function (value, item) {
                if (value !== '') {
                    if (!/^[\S]{4,12}$/.test(value)) {
                        return '账号必须4到20位，且不能出现空格';
                    }
                }
            },
            password: function (value, item) {
                if (value !== '') {
                    if (!/^[\S]{6,12}$/.test(value)) {
                        return '密码必须6到20位，且不能出现空格';
                    }
                }
            }
        });

    }

    if (parent.pageOperation === 0 || parent.pageOperation === 2) {
        // 页面赋值
        $.ajax({
            type: "GET",
            url: "user/query/" + parent.userId,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        for (var item in data.data) {
                            $("#" + item).val(data.data[item]);
                        }
                        form.render('select');
                    }
                } else {
                    top.layer.msg(data.message, {icon: 2});
                }
            },
            contentType: "application/json"
        });

        // 加载部门角色
        if (parent.deptId !== "") {
            loadDeptRoles(parent.deptId);
            $.ajax({
                type: "GET",
                url: "user/queryUserRoles/" + parent.userId,
                success: function (data) {
                    if (data.code === 200) {
                        $.each(data.data, function (idx, obj) {
                            $("input[name='role'][value='" + obj.roleId + "']").attr("checked", true);
                            form.render('checkbox');
                        });
                    } else {
                        top.layer.msg(data.message, {icon: 2});
                    }
                },
                contentType: "application/json"
            });
        }
    }

    if (parent.pageOperation === 0) {
        $("#passwordHrm").hide();
        $(".layui-form input").prop("disabled", true);
        $(".layui-form select").prop("disabled", true);
        form.render('select');
        $('.layui-form button').hide();
    } else if (parent.pageOperation === 2) {
        $("#passwordTip").text("不输入登录密码则保持旧密码不变");
        var accountDom = $("#account");
        accountDom.attr('disabled', true);
        accountDom.addClass("layui-disabled");
    }

    // 选择部门树页面选中后回调函数
    deptTreeCallBack = function (deptId, deptName) {
        $("#deptId").val(deptId);
        $("#deptName").val(deptName);
        loadDeptRoles(deptId);
    };

    function loadDeptRoles(deptId) {
        // 查询角色
        $.ajax({
            type: 'GET',
            url: 'role/queryRoles/' + deptId,
            async: false,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        $("#roleDiv").empty();
                        $.each(data.data, function (index, obj) {
                            $("#roleDiv").append('<input type="checkbox" class="role" name="role" value="' + obj.id + '" lay-skin="primary" title="' + obj.roleName + '">');
                        });
                        form.render('checkbox');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    }
});

