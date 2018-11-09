layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = "leave/add";

    //日期
    laydate.render({
        elem: '#leaveDate',
        type: 'date',
        range: "~",
        min: new Date().getTime()
    });

    // 获取父页面的pageOperation，判断是查看、新增、修改
    if (parent.pageOperation === 1) { // 新增
    } else if (parent.pageOperation === 2) { // 修改
        submitUrl = "leave/modify";
    } else { // 查看
        $(".layui-form input").prop("disabled", true);
        $(".layui-form select").prop("disabled", true);
        $(".layui-form textarea").prop("disabled", true);
        $('.layui-form button').hide();
    }
    //如果是管理员，则需要加载请假人，否则请假人只能是自己
    var userId = layui.data("JWW_UMP").CUURENT_USER.id;
    if (userId === 1) {
        //查询所有用户
        $.ajax({
            //async: false,
            type: 'POST',
            url: 'user/list',
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        for (var i = 0; i < data.data.length; i++) {
                            $("#userId").append("<option value='" + data.data[i].id + "'>" + data.data[i].userName + "</option>");
                        }
                        form.render('select');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    } else {
        $("#userId").append("<option value='" + userId + "'>" + layui.data("JWW_UMP").CUURENT_USER.userName + "</option>");
        form.render('select');
    }

    //初始化请假类型
    $.ajax({
        async: false,
        type: 'POST',
        url: 'dic/listByType',
        data: JSON.stringify("LEAVE_TYPE"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#type").append("<option value='" + data.data[i].code + "'>" + data.data[i].codeText + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    if (parent.pageOperation === 2 || parent.pageOperation === 0) {
        $("#id").val(parent.leaveId);
        // 查询请假数据，初始化页面属性值
        $.ajax({
            type: 'GET',
            url: 'leave/query/' + parent.leaveId,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        $("#userId").val(data.data.userId);
                        $("#type").val(data.data.type);
                        $("#leaveDate").val(data.data.startTime + " ~ " + data.data.endTime);
                        $("#remark").val(data.data.remark);
                        form.render();
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    }


    // 监听submit
    if (parent.pageOperation === 1 || parent.pageOperation === 2) {
        form.on('submit(addFilter)', function (data) {
            var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
            $.ajax({
                type: 'POST',
                url: submitUrl,
                data: JSON.stringify(data.field),
                success: function (data) {
                    if (data.code === 200) {
                        //弹出loading
                        setTimeout(function () {
                            top.layer.close(index);
                            top.layer.msg("操作成功！", {icon: 1});
                            layer.closeAll("iframe");
                            parent.document.getElementById("search").click();
                        }, 1000);
                    } else {
                        top.layer.close(index);
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });

            // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
            return false;
        });
    }
});

