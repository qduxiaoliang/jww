layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table;

    // 初始化统计日期
    laydate.render({
        elem: '#statisticsDate',
        range: '~'
    });

    // 渲染表格
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers'},
            {
                field: 'userName',
                title: '员工姓名',
                align: 'center',
                event: "detail",
                style: 'color: #3366FF;cursor: pointer;'
            },
            {field: 'count', title: '浏览人才量', align: 'center'}
        ]],
        url: 'browseDetail/statisticsListPage',
        method: 'post',
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        },
        elem: '#browseStatisticsTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });

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
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    //监听工具条
    table.on('tool(tableFilter)', function (obj) { // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        var layEvent = obj.event; // 获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { // 查看
            checkedUserId = data.userId;
            checkedBrowserStatisticsDate = $("#statisticsDate").val();
            layui.layer.open({
                title: "浏览明细",
                type: 2,
                area: ['100%', '100%'],
                offset: ['0px', '0px'],
                content: "browseDetail.html"
            });
        }
    });

    // 查询
    $(".search_btn").click(function () {
        if (base.fastClickCheck(".search_btn")) {
            var userId = $("#userId").val();
            var statisticsDate = $("#statisticsDate").val();
            tableIns.reload({
                where: { //设定异步数据接口的额外参数，任意设
                    condition: {
                        userId: userId,
                        statisticsDate: statisticsDate
                    }
                },
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });
        }
    });


});