layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;
    insideInfoId = "";
    typeCode = "";
    // 渲染表格
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers'},
            {
                field: 'title',
                title: '标题',
                align: 'center',
                event: 'detail',
                style: 'color: #3366FF;cursor: pointer;'
            },
            {field: 'typeName', title: '类别', width: 140, align: 'center'},
            {field: 'content', title: '内容', align: 'center'},
            {field: 'createTime', title: '时间', width: 175, align: 'center'},
            {field: 'attachment', title: '附件', width: 200, align: 'center'},
            {
                field: 'opt',
                title: '操作',
                align: 'center',
                fixed: 'right',
                width: 120,
                align: 'center',
                toolbar: '#toolBar'
            }
        ]],
        url: 'insideInfo/queryListPage',
        method: 'post',
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        },
        elem: '#insideInfoTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });

    // 初始化信息类别下拉列表数据，字典类型值：INSIDEINFO_TYPE
    $.ajax({
        type: 'POST',
        url: 'dic/listByType',
        data: JSON.stringify("INSIDEINFO_TYPE"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#type").append("<option value='" + data.data[i].code + "'>" + data.data[i].codeText + "</option>");
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
        insideInfoId = data.id;
        typeCode = data.type;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { // 查看
            pageOperation = 0;
            // 查看内部信息
            var index = layui.layer.open({
                title: "查看内部信息",
                type: 2,
                area: ['100%', '100%'],
                offset: ['0px', '0px'],
                content: "insideInfo.html",
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回内部列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            });
        } else if (layEvent === 'del') { // 删除
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'DELETE',
                    url: 'insideInfo/delBatchByIds',
                    data: JSON.stringify([data.id]),
                    success: function (data) {
                        if (data.code === 200) {
                            if (data.data === true) {
                                obj.del();
                                base.successMsg(layer, "删除成功");
                            }
                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
            });
        } else if (layEvent === 'edit') { // 修改
            pageOperation = 2;
            // 修改内部信息
            var index = layui.layer.open({
                title: "修改内部信息",
                type: 2,
                area: ['100%', '100%'],
                offset: ['0px', '0px'],
                content: "insideInfo.html",
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回内部信息列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            });
        }
    });

    // 查询
    $("#search").click(function () {
        if (base.fastClickCheck("#search")) {
            var type = $("#type").val();
            tableIns.reload({
                where: { //设定异步数据接口的额外参数，任意设
                    condition: {
                        type: type
                    }
                },
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });
        }
    });
    // 新增
    $(".addBtn").click(function () {
        pageOperation = 1;
        insideInfoId = "";
        typeCode = "";
        var index = layui.layer.open({
            title: "新增内部信息",
            type: 2,
            resize: false,
            area: ['100%', '100%'],
            offset: ['0px', '0px'],
            content: "insideInfo.html",
            success: function (layero, index) {
                setTimeout(function () {
                    layui.layer.tips('点击此处返回人才列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        });
    });
});