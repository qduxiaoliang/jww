layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;
    //页面操作：0：查看，1：新增，2：修改
    pageOperation = 0;
    positionId = "";

    //列表加载
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {
                field: 'positionName',
                title: '职位名称',
                event: "detail",
                align: 'center',
                style: 'color: #3366FF;cursor: pointer;'
            },
            {field: 'companyName', title: '企业名称', align: 'center'},
            {field: 'opt', title: '操作', fixed: 'right', width: 120, align: 'center', toolbar: '#toolBar'}
        ]],
        url: 'companyPosition/queryListPage',
        method: 'POST',
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message', //状态信息的字段名称，默认：msg
        },
        elem: '#positionTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });

    //监听工具条
    table.on('tool(positionTable)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        positionId = data.id;
        //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var layEvent = obj.event;
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            var index = layui.layer.open({
                title: "查看职位",
                type: 2,
                area: ['100%', '100%'],
                offset: ['0px', '0px'],
                content: "companyPosition.html",
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回会员列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            });
        } else if (layEvent === 'del') { //删除
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'DELETE',
                    url: 'companyPosition/delBatchByIds',
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
            var index = layui.layer.open({
                title: "修改职位",
                type: 2,
                area: ['100%', '100%'],
                offset: ['0px', '0px'],
                content: "companyPosition.html",
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回职位列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            });
        }
    });

    //监听表格复选框选择
    table.on('checkbox(positionTable)', function (obj) {
    });

    //查询
    $("#search").click(function () {
        var searchKey = $(".search_input").val();
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                condition: {
                    position_name: searchKey
                }
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    //新增岗位
    $(".positionAdd_btn").click(function () {
        pageOperation = 1;
        positionId = "";
        var index = layui.layer.open({
            title: "新增职位",
            type: 2,
            area: ['100%', '100%'],
            offset: ['0px', '0px'],
            content: "companyPosition.html",
            success: function (layero, index) {
                setTimeout(function () {
                    layui.layer.tips('点击此处返回职位列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        });
    });

    //批量删除
    $(".batchDel").click(function () {
        var checkStatus = table.checkStatus('positionTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请选择要删除的用户", {icon: 0, time: 2000});
            return;
        }
        layer.confirm('确定删除选中的信息？', {icon: 3, title: '确认'}, function (index) {
            var indexMsg = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
            var positionIds = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                positionIds[i] = checkStatus.data[i].id;
            }
            $.ajax({
                type: 'DELETE',
                url: 'companyPosition/delBatchByIds',
                data: JSON.stringify(positionIds),
                success: function (data) {
                    if (data.code === 200) {
                        layer.close(indexMsg);
                        layer.msg("删除成功", {icon: 1, time: 2000});
                        tableIns.reload({
                            page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });
                    } else {
                        // alert(JSON.stringify(data));
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        });
    });

});