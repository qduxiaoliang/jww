layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'layedit', 'upload'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        layedit = layui.layedit,
        upload = layui.upload;

    //新增则提交到新增url，修改则提交到修改url
    submitUrl = parent.pageOperation === 1 ? "insideInfo/add" : "insideInfo/modify";
    submitType = parent.pageOperation === 1 ? "POST" : "PUT";

    // 获取父页面的pageOperation，判断是查看、新增、修改
    if (parent.pageOperation === 0) { // 查看
        $(".layui-form input").prop("disabled", true);
        $("#type").attr('disabled', true);
        $(".layui-form textarea").prop("disabled", true);
        $('.layui-form button').hide();
    }

    // 初始化富文本编辑器
    // layedit.build('content');

    var loadingIndex;
    // 选完文件后不自动上传
    layui.upload.render({
        elem: '#chooseFile',
        url: 'insideInfo/upload',
        auto: false,
        size: 5120, // 限制附件大小，最大5M
        accept: 'file',
        bindAction: '#upload',
        choose: function (obj) {
            obj.preview(function (index, file, result) {
                attachmentName = file.name;
                $("#attachmentName").val(file.name);
            });
            $("#upload").show();
            $("#uploadTip").show();
        },
        before: function (obj) {
            loadingIndex = base.loading(layer);
            this.data = {
                attachmentId: $("#attachmentId").val()
            }
        },
        done: function (res) {
            layer.close(loadingIndex);
            if (res.code === 200) {
                $("#upload").hide();
                $("#uploadTip").hide();
                base.successMsg(layer, "附件上传成功");
                $("#attachmentName").val(attachmentName);
                if (res.data !== null) {
                    $("#attachmentId").val(res.data.attachmentId);
                }
            } else {
                base.errorMsg(layer, res.message);
            }

        },
        error: function (index, upload) {
            layer.close(loadingIndex);
        }
    });

    // 初始化信息类别下拉列表数据，字典类型值：INSIDEINFO_TYPE
    $.ajax({
        type: 'POST',
        url: 'dic/listByType',
        data: JSON.stringify("INSIDEINFO_TYPE"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        if (parent.typeCode != '' && parent.typeCode == data.data[i].code) {
                            $("#type").append("<option value='" + data.data[i].code + "' selected>" + data.data[i].codeText + "</option>");
                        } else {
                            $("#type").append("<option value='" + data.data[i].code + "'>" + data.data[i].codeText + "</option>");
                        }
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    if (parent.pageOperation === 2 || parent.pageOperation === 0) {
        deptParentId = parent.deptId;
        $.ajax({
            type: "GET",
            url: "insideInfo/query/" + parent.insideInfoId,
            success: function (data) {
                if (data.code === 200) {
                    for (var item in data.data) {
                        $("#" + item).val(data.data[item]);
                    }
                    form.render('select');
                    $("#id").val(parent.insideInfoId);
                } else {
                    top.layer.msg("查询异常！", {icon: 2});
                }
            },
            contentType: "application/json"
        });
    }

    // 监听submit
    form.on('submit(addFilter)', function (data) {
        var loadingIndex = base.loading(layer);
        $.ajax({
            type: submitType,
            url: submitUrl,
            data: JSON.stringify(data.field),
            success: function (data) {
                layer.close(loadingIndex);
                if (data.code === 200) {
                    if (parent.pageOperation === 1) {
                        // 重置表单
                        // $("#roleForm")[0].reset();
                        top.layer.msg('新增成功', {icon: 1});
                    } else {
                        top.layer.msg('修改成功', {icon: 1});
                    }
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

});

