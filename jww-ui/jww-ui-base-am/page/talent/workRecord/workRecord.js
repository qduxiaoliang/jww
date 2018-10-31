layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table','laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table,
        laydate = layui.laydate,
        submitUrl = "workRecord/add";

    //时间选择器
    laydate.render({
        elem: '#nconnTime'
        ,type: 'datetime'
        ,min: new Date().getTime()
    });

    $("#talentName").val(parent.talentName);
    $("#talentId").val(parent.talentId);
    $("#phone").val(parent.phone);
    $("#workRecordId").val(parent.workRecordId);
    // 初始化人才跟进状态下拉列表数据
    $.ajax({
        type: 'POST',
        url: 'talent/followStateList',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#followState").append("<option value='" + data.data[i].followState + "'>" + data.data[i].followStateName + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

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
                        // 刷新父页面
                        parent.location.reload();
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


});

