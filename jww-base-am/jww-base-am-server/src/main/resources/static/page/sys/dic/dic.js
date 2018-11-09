layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', "treecheck"], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        submitUrl = "dic/add";

    // 获取父页面的pageOperation，判断是查看、新增、修改
    if (parent.pageOperation === 1) { // 新增
    } else if (parent.pageOperation === 2) { // 修改
        submitUrl = "dic/modify";
    } else { // 查看
        $(".layui-form input").prop("disabled", true);
        $(".layui-form select").prop("disabled", true);
        $('.layui-form button').hide();
    }

    // 初始化字典类型下拉框
    $.ajax({
        type: 'POST',
        url: 'dic/typeList',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    $("#type").append("<option value=''>请选择</option>");
                    for (var i = 0; i < data.data.length; i++) {
                        $("#type").append("<option value='" + data.data[i].type + "'>" + data.data[i].typeText + "</option>");
                    }
                    if (parent.checkedDicType !== undefined && parent.checkedDicType !== null) {
                        $("#type").val(parent.checkedDicType);
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    if (parent.pageOperation === 2 || parent.pageOperation === 0) {
        $("#id").val(parent.checkedDicId);
        // 查询字典
        $.ajax({
            type: 'POST',
            url: 'dic/query',
            data: JSON.stringify(parent.checkedDicId),
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        $("#typeText").val(data.data.typeText);
                        $("#codeText").val(data.data.codeText);
                        $("#code").val(data.data.code);
                        $("#sortNo").val(data.data.sortNo);
                        $(':radio[name="enable"][value="' + data.data.enable + '"]').attr("checked", "checked");
                        form.render('radio');
                        $("#remark").val(data.data.remark);
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    }

    // 监听submit
    form.on('submit(addFilter)', function (data) {
        var loadingIndex = base.loading(layer);
        $.ajax({
            type: 'POST',
            url: submitUrl,
            data: JSON.stringify(data.field),
            success: function (data) {
                layer.close(loadingIndex);
                if (data.code === 200) {
                    base.successMsg(layer, "操作成功");
                    layer.closeAll("iframe");
                    parent.document.getElementById("search").click();
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });

        // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    // 字典类型select监听
    form.on('select(typeFilter)', function (data) {
        $("#typeText").val($(data.elem).find("option:selected").text());
    });


});

