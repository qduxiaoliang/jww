layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table,
        laydate = layui.laydate,
        submitUrl = "interviewRecord/add";

    companyId = "";
    positionId = "";
    pageOperation = 0;

    $("#talentName").val(parent.talentName);
    $("#talentId").val(parent.talentId);
    var currUser = layui.data("JWW_UMP").CUURENT_USER;
    $("#userId").val(currUser.userId);
    $("#userName").val(currUser.userName);

    // 初始化面试企业下拉列表数据
    $.ajax({
        type: 'POST',
        url: 'company/queryList',
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

    // 面试下拉框监听
    form.on('select(companyId)', function (data) {
        var positionIdDom = $("#positionId");
        if (data.value === "") {
            positionIdDom.empty();
            form.render('select');
            return false;
        }
        $.ajax({
            type: 'POST',
            url: 'companyPosition/queryListByCompanyId',
            data: data.value,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        positionIdDom.empty();
                        if (data.data.length > 0) {
                            positionIdDom.append("<option value=''>请选择</option>");
                            for (var i = 0; i < data.data.length; i++) {
                                positionIdDom.append("<option value='" + data.data[i].id + "'>" + data.data[i].positionName + "</option>");
                            }
                        }
                        form.render('select');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    });

    // 初始化信息来源，字典类型值：INFO_SRC
    $.ajax({
        type: 'POST',
        url: 'dic/listByType',
        data: JSON.stringify("INTERVIEW_FLOW"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#interviewFlow").append("<option value='" + data.data[i].code + "'>" + data.data[i].codeText + "</option>");
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


    //企业查看详情
    $("#company_detail").click(function () {
        companyId = $("#companyId").val();
        if (companyId === null || companyId === undefined || companyId.length === 0) {
            layui.layer.msg("请先选择面试企业", {icon: 5, time: 2000});
            return;
        }
        var index = layui.layer.open({
            title: "查看合作企业",
            type: 2,
            offset: ['0px', '0px'],
            area: ['100%', '100%'],
            content: "/page/setting/company/company.html"
        });
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function () {
            layui.layer.full(index);
        });
    });
    //职位查看详情
    $("#position_detail").click(function () {
        positionId = $("#positionId").val();
        if (positionId === null || positionId === undefined || positionId.length === 0) {
            layui.layer.msg("请先选择面试职位", {icon: 5, time: 2000});
            return;
        }
        var index = layui.layer.open({
            title: "查看职位",
            type: 2,
            offset: ['0px', '0px'],
            area: ['100%', '100%'],
            content: "/page/setting/companyPosition/companyPosition.html"
        });
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function () {
            layui.layer.full(index);
        });
    });
});

