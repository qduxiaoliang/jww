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
        elem: '#date',
        range: '~'
    });

    // 渲染表格
    var tableIns = table.render({
        //设置表头
        cols: [[
            {field: 'account', title: '员工工号', align: 'center'},
            {field: 'userName', title: '员工姓名', align: 'center'},
            {field: 'totalNum', title: '所有人才', align: 'center'},
            {field: 'addNum', title: '新增人才', align: 'center'},
            {field: 'connNum', title: '联系人才', align: 'center'},
            {field: 'cjNum', title: '成交人才', align: 'center'},
            {field: 'yxNum', title: '意向人才', align: 'center'},
            {field: 'hmdNum', title: '黑名单人才', align: 'center'},
            {field: 'wlxNum', title: '从未联系', align: 'center'},
            {field: 'zrNum', title: '转让人才', align: 'center'}
        ]],
        url: 'workStastics/queryListPage',
        method: 'post',
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        },
        elem: '#workStasticsTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });

    // 查询
    $(".search_btn").click(function () {
        if (base.fastClickCheck(".search_btn")) {
            var searchKey = $(".date").val();
            tableIns.reload({
                where: { //设定异步数据接口的额外参数，任意设
                    condition: {
                        date: searchKey
                    }
                },
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });
        }
    });


});