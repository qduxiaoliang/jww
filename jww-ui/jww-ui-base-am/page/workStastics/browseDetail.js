layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table,
        statisticsDateValue = '',
        userId = '';

    if (parent.checkedBrowserStatisticsDate !== undefined && parent.checkedBrowserStatisticsDate !== null) {
        statisticsDateValue = parent.checkedBrowserStatisticsDate;
    }
    // 初始化统计日期
    laydate.render({
        elem: '#statisticsDate',
        range: '~',
        value: statisticsDateValue
    });

    if (parent.checkedUserId !== undefined && parent.checkedUserId !== null) {
        userId = parent.checkedUserId;
    }

    // 初始化员工下拉列表数据
    $.ajax({
        type: 'POST',
        url: 'user/selectUsers',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#userId").append("<option value='" + data.data[i].id + "'>" + data.data[i].userName + "</option>");
                    }
                    $("#userId").val(userId);
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
            {type: 'numbers'},
            {field: 'userName', title: '员工姓名', align: 'center'},
            {field: 'talentName', title: '浏览人才', align: 'center'},
            {field: 'createTime', title: '浏览时间', align: 'center'}
        ]],
        url: 'browseDetail/detailListPage',
        method: 'post',
        where: {
            condition: {
                statisticsDate: statisticsDateValue,
                userId: userId
            }
        },
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        },
        elem: '#browseDetailTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });

    // 查询
    $(".search_btn").click(function () {
        if (base.fastClickCheck(".search_btn")) {
            var statisticsDate = $("#statisticsDate").val();
            tableIns.reload({
                where: { //设定异步数据接口的额外参数，任意设
                    condition: {
                        statisticsDate: statisticsDate,
                        userId: $("#userId").val()
                    }
                },
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });
        }
    });


});