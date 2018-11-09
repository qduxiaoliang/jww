layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table;

    //日期
    laydate.render({
        elem: '#checkDate'
        , range: '~'
    });

    // 渲染表格
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers'},
            {field: 'checkDate', title: '日期', align: 'center'},
            {
                field: 'weekDate', title: '星期', align: 'center', templet: function (d) {
                var day = new Date(Date.parse(d.checkDate));
                var weekArr = new Array('星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六');
                return weekArr[day.getDay()];
            }
            },
            {field: 'userName', title: '员工', align: 'center'},
            {field: 'cinTime', title: '签到', width: 175, align: 'center'},
            {field: 'coutTime', title: '签退', width: 175, align: 'center'},
            {
                field: 'state',
                title: '状态',
                align: 'center',
                templet: '<div>{{d.state == 1 ? "正常" : (d.state == 2 ? "迟到": (d.state == 4 ? "早退" : (d.state == 8 ? "迟到且早退":"")))}}</div>'
            }
        ]],
        url: 'check/queryListPage',
        method: 'post',
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        },
        elem: '#checkTable',
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

    //点击查询按钮事件监听
    $("#search").click(function () {
        var userId = $("#userId").val();
        var checkDate = $("#checkDate").val();
        var state = $("#state").val();
        tableIns.reload({
            where: { //设定异步数据接口的额外考勤，任意设
                condition: {
                    user_id: userId,
                    check_date: checkDate,
                    state_: state
                }
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    //根据日期获得星期几
    function getWeekDay(date) {
        var day = new Date(Date.parse(date));
        var weekArr = new Array('星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六');
        return weekArr[day.getDay()];
    }

});