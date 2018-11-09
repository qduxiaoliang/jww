layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;

    //查询所有用户
    $.ajax({
        type: 'POST',
        url: 'queryRunasList',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#userId").append("<option value='" + data.data[i].id + "'>" + data.data[i].userName + "</option>");
                    }
                    form.render('select');
                }
            }
        }
    });

    // 监听submit
    form.on('submit(transferFilter)', function (data) {
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        $.ajax({
            type: 'POST',
            url: 'changeUser',
            data: JSON.stringify($("#userId").val()),
            success: function (data) {
                if (data.code === 200) {
                    //弹出loading
                    layui.data('JWW_UMP', {
                        key: 'CUURENT_USER', value: data.data
                    });
                    top.layer.close(index);
                    top.layer.msg("操作成功！", {icon: 1});
                    setTimeout(function () {
                        layer.closeAll("iframe");
                        //刷新父页面
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

