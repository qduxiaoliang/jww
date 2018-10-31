layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'tree'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    //新增则提交到新增url，修改则提交到修改url
    submitUrl = parent.pageOperation === 1 ? "company/add" : "company/modify";
    submitType = parent.pageOperation === 1 ? "POST" : "PUT";
    companyParentId = "";
    provinceId = "";
    cityId = "";
    // 初始化所属省份下拉列表数据，字典类型值：TALENT_STATE
    $.ajax({
        type: 'POST',
        url: 'area/list',
        data: JSON.stringify("1"),
        async: false,
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#provinceId").append("<option value='" + data.data[i].id + "'>" + data.data[i].province + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 所属省份下拉框监听
    form.on('select(provinceIdFilter)', function (data) {
        var cityIdDom = $("#cityId");
        if (data.value === "") {
            cityIdDom.empty();
            form.render('select');
            return false;
        }
        loadCity(data.value);
    });

    if (parent.pageOperation === 0) {
        $(".layui-form input").prop("readonly", true);
        $(".layui-form textarea").prop("readonly", true);
        $(".layui-form select").prop("disabled", true);
        $('.layui-form button').hide();
    }

    if (parent.pageOperation === 1 || parent.pageOperation === 2) {
        form.on("submit(addcompany)", function (data) {
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
        companyParentId = parent.companyId;
        $.ajax({
            type: "GET",
            url: "company/query/" + parent.companyId,
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
                            if (i === 'cityId') {
                                cityId = rest[i];
                            } else if (i === 'provinceId') {
                                provinceId = rest[i];
                            }
                            // $("." + i).find("option[value='"+ rest[i] +"']").attr("selected",true);
                            form.render('select');
                        }
                    }
                } else {
                    top.layer.msg("查询异常！", {icon: 2});
                }
            },
            contentType: "application/json"
        });
        loadCity(provinceId);
        $("#cityId option[value='" + cityId + "']").prop("selected", true);

    }

    function loadCity(provinceId) {
        // 初始化城市下拉列表数据
        $.ajax({
            type: 'POST',
            url: 'area/list',
            data: provinceId,
            async: false,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        $("#cityId").empty();
                        if (data.data.length > 0) {
                            for (var i = 0; i < data.data.length; i++) {
                                $("#cityId").append("<option value='" + data.data[i].id + "'>" + data.data[i].city + "</option>");
                            }
                        }
                        form.render('select');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    }
})
