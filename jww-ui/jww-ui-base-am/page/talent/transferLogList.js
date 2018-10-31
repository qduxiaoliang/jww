layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;
    //日期
    layui.laydate.render({
        elem: '#createTime',
        range: '~'
    });

    // 初始化转入人下拉框
    $.ajax({
        type: 'POST',
        url: 'transferLog/fromUserList',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#fromId").append("<option value='" + data.data[i].fromId + "'>" + data.data[i].fromName + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 初始化转入人下拉框
    $.ajax({
        type: 'POST',
        url: 'transferLog/toUserList',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#toId").append("<option value='" + data.data[i].toId + "'>" + data.data[i].toName + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 初始化人才状态下拉列表数据，字典类型值：TALENT_STATE
    $.ajax({
        type: 'POST',
        url: 'dic/listByType',
        data: JSON.stringify("TALENT_STATE"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#talentState").append("<option value='" + data.data[i].id + "'>" + data.data[i].codeText + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 渲染表格
    var tableIns = table.render({
        //设置表头
        cols: [[
            {field: 'talentName', title: '人才姓名', align: 'center'},
            {field: 'createTime', title: '录入时间', width: 170, align: 'center'},
            {field: 'lconnTime', title: '最后联系', width: 170, align: 'center'},
            {field: 'createTime', title: '转让时间', width: 170, align: 'center'},
            {field: 'fromName', title: '转出人', align: 'center'},
            {field: 'toName', title: '转入人', align: 'center'},
            {field: 'transferReason', title: '转让原因', align: 'center'}
        ]],
        url: 'transferLog/queryListPage',
        method: 'post',
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        },
        elem: '#roleTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });

    // 查询
    form.on('submit(searchFilter)', function (data) {
        if (base.fastClickCheck(".search_btn")) {
            var searchKey = $(".search_input").val();
            tableIns.reload({
                where: { //设定异步数据接口的额外参数，任意设
                    condition: data.field
                },
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });
        }
    });


});