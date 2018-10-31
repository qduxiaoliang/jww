layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;

    // 页面操作：0：查看，1：新增，2：修改
    pageOperation = 0;
    menuId = "";


    //列表加载
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {
                field: 'menuName',
                title: '菜单名称',
                align: 'center',
                event: "detail",
                style: 'color: #3366FF;cursor: pointer;'
            },
            {field: 'parentName', title: '上级菜单', align: 'center', sort: true},
            {
                field: 'menuType',
                title: '类型',
                sort: true,
                align: 'center',
                templet: '<div>{{d.menuType === 0 ? "目录" : d.menuType === 1 ? "菜单" : "按钮"}}</div>'
            },
            {field: 'iconcls', title: '菜单图标样式', align: 'center', sort: false},
            {field: 'sortNo', title: '排序', align: 'center', sort: true},
            {field: 'request', title: '请求地址', align: 'center', sort: false},
            {field: 'permission', title: '权限标识', align: 'center', sort: false},
            {field: 'opt', title: '操作', fixed: 'right', width: 120, align: 'center', toolbar: '#toolBar'}
        ]],
        url: 'menu/queryListPage',
        method: 'post',
        response: {
            statusCode: 200 //成功的状态码，默认：0
        },
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        },
        elem: '#menuTable'
    });

    // 初始化父级菜单下拉框
    $.ajax({
        type: 'POST',
        url: 'menu/queryParentMenu',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#parentId").append("<option value='" + data.data[i].id + "'>" + data.data[i].menuName + "</option>");
                    }
                    form.render('select');
                }
            }
        }
    });

    //查询
    $("#search").click(function () {
        var menuName = $("#menuName").val();
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                condition: {
                    menu_name: menuName,
                    menu_type: $("#menuType").val(),
                    parent_id: $("#parentId").val()
                }
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    //监听状态操作
    form.on('checkbox(enableCbx)', function (obj) {
        // layer.tips(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);
    });

    //监听单元格修改
    table.on('edit(menuTable)', function (obj) {
        var value = obj.value //得到修改后的值
            , data = obj.data //得到所在行所有键值
            , field = obj.field; //得到字段
        // layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
        var modData = {};
        modData["id"] = data.id;
        modData[field] = value;
        modData["parentId"] = data.parentId;
        modMenuData(modData);
    });

    //监听工具条
    table.on('tool(menuTable)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        menuId = data.id;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            var index = layui.layer.open({
                title: "查看菜单",
                type: 2,
                area: ['100%', '100%'],
                offset: ['0px', '0px'],
                content: "menu.html",
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            });
        } else if (layEvent === 'del') { //删除
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'DELETE',
                    url: 'menu/delete',
                    data: menuId,
                    success: function (data) {
                        if (data.code === 200) {
                            if (data.data === true) {
                                obj.del();
                                layer.msg("删除成功", {icon: 1, time: 2000});
                            } else {
                                layer.msg(data.message, {icon: 2});
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
                title: "修改菜单",
                type: 2,
                area: ['100%', '100%'],
                offset: ['0px', '0px'],
                content: "menu.html",
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            });
        }
    });

    //监听表格复选框选择
    table.on('checkbox(menuTable)', function (obj) {
    });

    //新增菜单
    $(".menuAdd_btn").click(function () {
        pageOperation = 1;
        menuId = "";
        var index = layui.layer.open({
            title: "新增菜单",
            type: 2,
            area: ['100%', '100%'],
            offset: ['0px', '0px'],
            content: "menu.html",
            success: function (layero, index) {
                setTimeout(function () {
                    layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        });
    });

    //批量删除
    $(".batchDel").click(function () {
        var checkStatus = table.checkStatus('menuTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请选择要删除的菜单", {icon: 0, time: 2000});
            return;
        }
        layer.confirm('确定删除选中的信息？', {icon: 3, title: '确认'}, function (index) {
            var indexMsg = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
            var menuIds = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                menuIds[i] = checkStatus.data[i].id;
            }
            $.ajax({
                type: 'POST',
                url: 'menu/deleteBatchIds',
                data: JSON.stringify(menuIds),
                success: function (data) {
                    if (data.code === 200) {
                        //layer.close(indexMsg);
                        layer.msg("操作成功，成功删除记录数" + data.data, {icon: 1, time: 2000});
                        tableIns.reload();
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        });
    })

    function modMenuData(modData) {
        var index = layer.msg('修改中，请稍候', {icon: 16, time: false, shade: 0.8});
        $.ajax({
            type: "POST",
            url: "menu/modify",
            data: JSON.stringify(modData),
            success: function (data) {
                if (data.code === 200) {
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg("修改成功！", {icon: 1});
                    }, 500);
                } else {
                    top.layer.close(index);
                    layer.msg(data.message, {icon: 2});
                }
            },
            contentType: "application/json"
        });
    }

});