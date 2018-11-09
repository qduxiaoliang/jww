layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;

    pageOperation = 0;
    leaveId = '';

    // 渲染表格
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {
                field: 'userName',
                title: '请假人',
                width: 220,
                align: 'center',
                event: "detail",
                style: 'color: #3366FF;cursor: pointer;'
            },
            {
                field: 'leaveDate',
                title: '请假时间',
                width: 250,
                align: 'center',
                templet: '<div>{{d.startTime + "至" + d.endTime}}</div>'
            },
            {field: 'typeName', title: '请假类型', width: 220, align: 'center'},
            {field: 'remark', title: '请假说明', align: 'center'},
            {field: 'opt', title: '操作', fixed: 'right', width: 120, align: 'center', toolbar: '#toolBar'}
        ]],
        url: 'leave/queryListPage',
        method: 'post',
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        },
        elem: '#leaveTable',
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
    table.on('tool(tableFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 请假对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            leaveId = data.id;
            var index = layui.layer.open({
                title: "查看请假",
                type: 2,
                area: ['100%', '100%'],
                offset: ['0px', '0px'],
                content: "leave.html",
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回请假列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            });
        } else if (layEvent === 'del') { //删除
            leaveId = data.id;
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'DELETE',
                    url: 'leave/delBatchByIds',
                    data: JSON.stringify([data.id]),
                    success: function (data) {
                        if (data.code === 200) {
                            if (data.data === true) {
                                obj.del();
                                layer.msg("删除成功", {icon: 1, time: 2000});
                            }
                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
            });
        } else if (layEvent === 'edit') { //修改
            pageOperation = 2;
            leaveId = data.id;
            var index = layui.layer.open({
                title: "修改请假",
                type: 2,
                area: ['100%', '100%'],
                offset: ['0px', '0px'],
                content: "leave.html",
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回请假列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            });
        }
    });

    //监听表格复选框选择
    table.on('checkbox(tableFilter)', function (obj) {
    });

    //点击查询按钮事件监听
    $("#search").click(function () {
        var searchKey = $("#userId").val();
        tableIns.reload({
            where: { //设定异步数据接口的额外请假，任意设
                condition: {
                    user_id: searchKey
                }
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    // 新增请假按钮监听
    $(".addBtn").click(function () {
        pageOperation = 1;
        leaveId = "";
        var index = layui.layer.open({
            title: "员工请假",
            type: 2,
            area: ['100%', '100%'],
            offset: ['0px', '0px'],
            content: "leave.html",
            success: function (layero, index) {
                setTimeout(function () {
                    layui.layer.tips('点击此处返回请假列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        });
    });

    //批量删除按钮监听
    $(".batchDel").click(function () {
        var checkStatus = table.checkStatus('leaveTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请选择要删除的请假", {icon: 0, time: 2000});
            return;
        }
        layer.confirm('确定删除选中的信息？', {icon: 3, title: '确认'}, function (index) {
            var indexMsg = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
            var leaveIds = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                leaveIds[i] = checkStatus.data[i].id;
            }
            $.ajax({
                type: 'DELETE',
                url: 'leave/delBatchByIds',
                data: JSON.stringify(leaveIds),
                success: function (data) {
                    if (data.code == 200) {
                        if (data.data === true) {
                            layer.close(indexMsg);
                            layer.msg("删除成功", {icon: 1, time: 2000});
                            tableIns.reload({
                                page: {
                                    curr: 1 //重新从第 1 页开始
                                }
                            });
                        }
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        });
    })

});