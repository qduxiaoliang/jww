layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;

    pageOperation = 0; // 页面操作：0：查看，1：新增，2：修改
    checkedTalentId = ""; // 人才ID
    checkedProvinceId = ""; // 人才所属省份ID
    checkedProfessionId = ""; // 人才所属行业ID
    checkedDeptId = ""; // 人才所属行业部门ID
    workRecordId = "";//工作记录编号(预约联系记录编号)

    var tableLoadingIndex = base.loading(layui.layer);
    // 渲染表格
    var tableIns = table.render({
        cellMinWidth: 110,
        //设置表头
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'talentName', title: '姓名', width: 140, align: 'center'},
            {field: 'phone', title: '手机号码', width: 140, align: 'center'},
            {field: 'professionName', title: '行业', align: 'center'},
            {field: 'positionName', title: '职位', align: 'center'},
            {field: 'followStateName', title: '跟进状态', align: 'center'},
            {field: 'userName', title: '业务员', align: 'center'},
            {field: 'createTime', title: '录入时间', width: 175, align: 'center'},
            {field: 'lconnTime', title: '最后联系时间', width: 175, align: 'center'},
            {field: 'nconnTime', title: '下次联系时间', width: 175, align: 'center', sort: true},
            {field: 'protectionDate', title: '保护截止日期', width: 130, align: 'center'},
            {field: 'opt', title: '操作', width: 100, align: 'center', fixed: 'right', toolbar: '#toolBar'}
        ]],
        url: 'talent/queryOrderListPage',
        method: 'post',
        where: { // 设定异步数据接口的额外参数，任意设
            condition: {
                nconn_time: 1
            }
        },
        request: {
            pageName: 'current', // 页码的参数名称，默认：page
            limitName: 'size' // 每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, // 成功的状态码，默认：0
            msgName: 'message' // 状态信息的字段名称，默认：msg
        },
        elem: '#talentTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        },
        done: function () {
            layui.layer.close(tableLoadingIndex);
        }
    });

    //监听工具条
    table.on('tool(tableFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            checkedTalentId = data.id;
            workRecordId = data.workRecordId;
            var index = layui.layer.open({
                title: "查看人才",
                type: 2,
                area: ['100%', '100%'],
                offset: ['0px', '0px'],
                content: "talentDetail.html",
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回人才列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            });
        }
    });

    // 查询
    form.on('submit(searchFilter)', function (data) {
        if (base.fastClickCheck("#search")) {
            tableLoadingIndex = base.loading(layui.layer);
            tableIns.reload({
                where: { // 设定异步数据接口的额外参数，任意设
                    condition: data.field
                },
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });
        }
        // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    // 新增人才
    $(".addBtn").click(function () {
        pageOperation = 1;
        var index = layui.layer.open({
            title: "新增人才",
            type: 2,
            area: ['100%', '100%'],
            offset: ['0px', '0px'],
            content: "talent.html",
            success: function (layero, index) {
                setTimeout(function () {
                    layui.layer.tips('点击此处返回人才列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        });
    });

    // 转让
    $(".transferBtn").click(function () {
        var checkStatus = table.checkStatus('talentTable');
        if (checkStatus.data.length === 0) {
            base.normalMsg(layer, "请至少选择一条记录");
            return;
        }
        layui.layer.open({
            title: "确认转让",
            type: 2,
            area: ['320px', '430px'],
            fixed: false, // 不固定
            content: '/page/talent/transferConfirm.html'
        });

    });

    // 批量删除
    $(".batchDel").click(function () {
        var checkStatus = table.checkStatus('talentTable');
        if (checkStatus.data.length === 0) {
            base.normalMsg(layer, "请至少选择一条记录");
            return;
        }
        layer.confirm('确定删除选中的人才吗？', {icon: 3, title: '确认'}, function (index) {
            var loadingIndex = base.loading(layer);
            var ids = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                ids[i] = checkStatus.data[i].id;
            }
            $.ajax({
                type: 'POST',
                url: 'talent/delBatchByIds',
                data: JSON.stringify(ids),
                success: function (data) {
                    if (data.code === 200) {
                        if (data.data === true) {
                            layer.close(loadingIndex);
                            base.successMsg(layer, "删除成功");
                            tableIns.reload({
                                page: {
                                    curr: 1 //重新从第 1 页开始
                                }
                            });
                        }
                    } else {
                        base.errorMsg(layer, data.message);
                    }
                }
            });
        });
    });
});