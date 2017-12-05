layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'account', title: '账号'},
            {field: 'userName', title: '姓名'},
            {field: 'sex', title: '性别', templet: '<div>{{d.sex === 1 ? "男" : "女"}}</div>'},
            {field: 'phone', title: '手机号'},
            {field: 'idCard', title: '身份证'},
            {field: 'dept', title: '部门'},
            {field: 'position', title: '职位'},
            {field: 'email', title: '邮箱'},
            {field: 'opt', fixed: 'right', width: 160, align: 'center', toolbar: '#toolBar'}
        ]],
        url: 'user/listPage',
        method: 'post',
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
        },
        elem: '#userTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });

    //监听工具条
    table.on('tool(tableFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            layer.msg("功能正在开发中，敬请期待...", {icon: 0});
        } else if (layEvent === 'del') { //删除
            var userIds = [data.id];
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'user/delBatchByIds',
                    data: JSON.stringify(userIds),
                    success: function (data) {
                        if (data.code == 200) {
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
        } else if (layEvent === 'edit') { //编辑
            layer.msg("功能正在开发中，敬请期待...", {icon: 0});
        }
    });

    //监听表格复选框选择
    table.on('checkbox(tableFilter)', function (obj) {
    });

    //查询
    $(".search_btn").click(function () {
        var searchKey = $(".search_input").val();
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                condition: {
                    account_: searchKey,
                    user_name: searchKey,
                    phone_: searchKey
                }
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    //添加会员
    $(".usersAdd_btn").click(function () {
        var index = layui.layer.open({
            title: "添加会员",
            type: 2,
            content: "addUser.html",
            success: function (layero, index) {
                setTimeout(function () {
                    layui.layer.tips('点击此处返回会员列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        });
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function () {
            layui.layer.full(index);
        });
        layui.layer.full(index);
    });

    //批量删除
    $(".batchDel").click(function () {
        var checkStatus = table.checkStatus('userTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请选择要删除的用户", {icon: 0, time: 2000});
            return;
        }
        layer.confirm('确定删除选中的信息？', {icon: 3, title: '确认'}, function (index) {
            var indexMsg = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
            var userIds = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                userIds[i] = checkStatus.data[i].id;
            }
            $.ajax({
                type: 'POST',
                url: 'user/delBatchByIds',
                data: JSON.stringify(userIds),
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