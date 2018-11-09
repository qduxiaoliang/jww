layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;

    // 初始化信息类别下拉列表数据，字典类型值：INSIDEINFO_TYPE
    $.ajax({
        type: 'POST',
        url: 'transferLog/queryTransferList',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#toId").append("<option value='" + data.data[i].id + "'>" + data.data[i].userName + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 监听submit
    form.on('submit(transferFilter)', function (data) {
        if(data.field.toId == null || data.field.toId===''){
            base.errorMsg(layer, '请选择转让人');
            return;
        }
        data.field.fromName = parent.checkedIds.data;
        $.ajax({
            type: 'POST',
            url: 'talent/transferBatch',
            data: JSON.stringify(data.field),
            success: function (data) {
                if (data.code === 200) {
                    if (data.data === true) {
                        base.successMsg(layer, "人才转让成功");
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(index);
                    }
                } else {
                    base.errorMsg(layer, data.message);
                }
            }
        });
        // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });
});