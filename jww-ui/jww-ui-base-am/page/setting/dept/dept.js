layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'tree'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    //新增则提交到新增url，修改则提交到修改url
    submitUrl = parent.pageOperation === 1 ? "profession/add" : "profession/modify";
    submitType = parent.pageOperation === 1 ? "POST" : "PUT";
    parentId = parent.parentId;


    // 初始化所属行业下拉列表数据
    $.ajax({
        type: 'POST',
        url: 'profession/queryProfessionList',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        if (parentId != '' && parentId === data.data[i].id) {
                            $("#parentId").append("<option value='" + data.data[i].id + "' selected>" + data.data[i].name + "</option>");
                        } else {
                            $("#parentId").append("<option value='" + data.data[i].id + "'>" + data.data[i].name + "</option>");
                        }
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    if (parent.pageOperation === 1 || parent.pageOperation === 2) {
        form.on("submit(adddept)", function (data) {
            var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
            // alert(JSON.stringify(data.field));
            $.ajax({
                type: submitType,
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
                        }, 500);
                    } else {
                        top.layer.close(index);
                        layer.msg(data.message, {icon: 2});
                    }
                },
                contentType: "application/json"
            });

            return false;
        });
    }
    if (parent.pageOperation === 2) {
        $.ajax({
            type: "GET",
            url: "profession/query/" + parent.deptId,
            success: function (data) {
                if (data.code === 200) {
                    var rest = data.data;
                    //循环实体
                    for (var i in rest) {
                        // console.log(i + '='+ rest[i]+ ' '+$("." + i).attr("type"));
                        //文本框赋值
                        if ($("." + i).attr("type") === "text" || $("." + i).attr("type") === "hidden") {
                            $("." + i).val(rest[i]);
                            // if(parent.pageOperation===0){
                            //     $("." + i).prop("placeholder","");
                            // }
                            //复选框改变状态
                        } else if ($("." + i).attr("type") === "checkbox") {
                            if (rest[i] === 0) {
                                $("." + i).removeAttr("checked");
                                form.render('checkbox');
                            }
                        }
                    }
                } else {
                    top.layer.msg("查询异常！", {icon: 2});
                }
            },
            contentType: "application/json"
        });
    }
});
