layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'tree'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;

    //新增则提交到新增url，修改则提交到修改url
    submitUrl = parent.pageOperation === 1 ? "companyPosition/add" : "companyPosition/modify";
    submitType = parent.pageOperation === 1 ? "POST" : "PUT";

    // 初始化所属企业下拉列表数据
    $.ajax({
        type: 'POST',
        url: 'company/queryList',
        async: false,
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#companyId").append("<option value='" + data.data[i].id + "'>" + data.data[i].companyName + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    if (parent.pageOperation === 0) {
        $(".layui-form input").prop("readonly", true);
        $(".layui-form textarea").prop("readonly", true);
        $(".layui-form select").prop("disabled", true);
        $('.layui-form button').hide();
    }

    if (parent.pageOperation === 1 || parent.pageOperation === 2) {
        form.on("submit(addposition)", function (data) {
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
    if (parent.pageOperation === 0 || parent.pageOperation === 2) {
        $.ajax({
            type: "GET",
            url: "companyPosition/query/" + parent.positionId,
            async: false,
            success: function (data) {
                if (data.code === 200) {
                    var rest = data.data;
                    //循环实体
                    for (var i in rest) {
                        // console.log(i + '='+ rest[i]+ ' '+$("." + i).attr("type"));
                        //文本框赋值
                        if ($("." + i).attr("type") === "text" || $("." + i).attr("type") === "hidden" || (typeof($("." + i).attr("class")) === "string" && $("." + i).attr("class").indexOf("layui-textarea") >= 0)) {
                            $("." + i).val(rest[i]);
                            if (parent.pageOperation === 0) {
                                $("." + i).prop("placeholder", "");
                            }
                            //复选框改变状态
                        } else if ($("." + i).attr("type") === "checkbox") {
                            if (rest[i] == 0) {
                                $("." + i).removeAttr("checked");
                                form.render('checkbox');
                            }
                        } else if ((typeof($("." + i).attr("class")) === "string" && $("." + i).attr("class").indexOf("select") >= 0)) {
                            $("." + i + " option[value='" + rest[i] + "']").prop("selected", true);
                            form.render('select');
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
