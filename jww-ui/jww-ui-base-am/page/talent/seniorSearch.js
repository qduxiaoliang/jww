layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'table', 'element'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laydate = layui.laydate,
        $ = layui.jquery,
        table = layui.table,
        element = layui.element;

    // 初始化出生日期
    laydate.render({
        elem: '#birthday',
        type: 'date',
        range: '~',
        max: 0
    });

    // 初始化下次联系时间
    laydate.render({
        elem: '#nconnTime',
        type: 'date'
    });

    // 初始化通话时间
    laydate.render({
        elem: '#callTime',
        type: 'datetime',
        range: '~'
    });

    // 初始化录入时间
    laydate.render({
        elem: '#createTime',
        type: 'datetime',
        range: '~'
    });

    // 初始化最后联系时间
    laydate.render({
        elem: '#lconnTime',
        type: 'datetime',
        range: '~'
    });

    // 初始化人才学历，字典类型值：TALENT_QUALIFICATIONS
    $.ajax({
        type: 'POST',
        url: 'dic/listByType',
        data: JSON.stringify("TALENT_QUALIFICATIONS"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#qualifications").append("<option value='" + data.data[i].code + "'>" + data.data[i].codeText + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 初始化人才跟进状态下拉列表数据
    $.ajax({
        type: 'POST',
        url: 'talent/followStateList',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#followState").append("<option value='" + data.data[i].followState + "'>" + data.data[i].followStateName + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 初始化人才状态下拉列表数据，字典类型值：TALENT_STATE
    $.ajax({
        type: 'POST',
        url: 'dic/listByType',
        data: JSON.stringify("TALENT_STATE"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#talentState").append("<option value='" + data.data[i].id + "'>" + data.data[i].codeText + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 初始化所属行业下拉列表数据
    $.ajax({
        type: 'POST',
        url: 'profession/queryProfessionList',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#professionId").append("<option value='" + data.data[i].id + "'>" + data.data[i].name + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 初始化所属省份下拉列表数据，字典类型值：INTERVIEW_FLOW
    $.ajax({
        type: 'POST',
        url: 'area/list',
        data: JSON.stringify("1"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#provinceId").append("<option value='" + data.data[i].id + "'>" + data.data[i].province + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 初始化面试企业下拉列表数据
    $.ajax({
        type: 'POST',
        url: 'company/queryList',
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#interviewCompany").append("<option value='" + data.data[i].id + "'>" + data.data[i].companyName + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 初始化面试流程下拉列表数据，字典类型值：INTERVIEW_FLOW
    $.ajax({
        type: 'POST',
        url: 'dic/listByType',
        data: JSON.stringify("INTERVIEW_FLOW"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#interviewFlow").append("<option value='" + data.data[i].code + "'>" + data.data[i].codeText + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 初始化信息来源，字典类型值：INFO_SRC
    $.ajax({
        type: 'POST',
        url: 'dic/listByType',
        data: JSON.stringify("INFO_SRC"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#infoSrc").append("<option value='" + data.data[i].code + "'>" + data.data[i].codeText + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    var tableLoadingIndex = base.loading(layui.layer);
    // 渲染表格
    var tableIns = table.render({
        cellMinWidth: 110,
        //设置表头
        cols: [[
            {
                field: 'talentName',
                title: '姓名',
                width: 140,
                align: 'center',
                event: 'detail',
                style: 'color: #3366FF;cursor: pointer;'
            },
            {field: 'phone', title: '手机号码', width: 140, align: 'center'},
            {field: 'professionName', title: '行业', align: 'center'},
            {field: 'positionName', title: '职位', align: 'center'},
            {field: 'followStateName', title: '跟进状态', align: 'center'},
            {field: 'userName', title: '业务员', align: 'center'},
            {field: 'createTime', title: '录入时间', width: 175, align: 'center'},
            {field: 'lconnTime', title: '最后联系时间', width: 175, align: 'center'},
            {field: 'nconnTime', title: '下次联系时间', width: 175, align: 'center'},
            {field: 'protectionDate', title: '保护截止日期', width: 130, align: 'center'},
            {field: 'opt', title: '操作', width: 120, align: 'center', fixed: 'right', toolbar: '#toolBar'}
        ]],
        data: [],
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
            checkedTalentFollowState = data.followState;
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
        } else if (layEvent === 'del') { // 删除
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'talent/delBatchByIds',
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
        } else if (layEvent === 'edit') { //修改
            pageOperation = 2;
            checkedTalentId = data.id;
            checkedProvinceId = data.provinceId;
            checkedProfessionId = data.professionId;
            checkedDeptId = data.deptId;
            var index = layui.layer.open({
                title: "修改人才",
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
        }
    });

    // 查询
    form.on('submit(searchFilter)', function (data) {
        if (base.fastClickCheck("#search")) {
            tableLoadingIndex = base.loading(layui.layer);
            tableIns.reload({
                url: 'talent/listPage4Senior',
                method: 'post',
                request: {
                    pageName: 'current', // 页码的参数名称，默认：page
                    limitName: 'size' // 每页数据量的参数名，默认：limit
                },
                response: {
                    statusCode: 200, // 成功的状态码，默认：0
                    msgName: 'message' // 状态信息的字段名称，默认：msg
                },
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

    // 所属省份下拉框监听
    form.on('select(provinceIdFilter)', function (data) {
        var cityIdDom = $("#cityId");
        if (data.value === "") {
            cityIdDom.empty();
            form.render('select');
            return false;
        }
        $.ajax({
            type: 'POST',
            url: 'area/list',
            data: data.value,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        cityIdDom.empty();
                        if (data.data.length > 0) {
                            cityIdDom.append("<option value=''>请选择</option>");
                            for (var i = 0; i < data.data.length; i++) {
                                cityIdDom.append("<option value='" + data.data[i].id + "'>" + data.data[i].city + "</option>");
                            }
                        }
                        form.render('select');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    });

    // 所属行业下拉框监听
    form.on('select(professionIdFilter)', function (data) {
        var deptIdDom = $("#deptId"), positionIdDom = $("#positionId");
        if (data.value === "") {
            deptIdDom.empty();
            positionIdDom.empty();
            form.render('select');
            return false;
        }
        $.ajax({
            type: 'POST',
            url: 'profession/queryDeptList',
            data: data.value,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        deptIdDom.empty();
                        positionIdDom.empty();
                        if (data.data.length > 0) {
                            deptIdDom.append("<option value=''>请选择</option>");
                            for (var i = 0; i < data.data.length; i++) {
                                deptIdDom.append("<option value='" + data.data[i].id + "'>" + data.data[i].name + "</option>");
                            }
                        }
                        form.render('select');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    });

    // 所属行业部门下拉框监听
    form.on('select(deptIdFilter)', function (data) {
        var positionIdDom = $("#positionId");
        if (data.value === "") {
            positionIdDom.empty();
            form.render('select');
            return false;
        }
        $.ajax({
            type: 'POST',
            url: 'profession/queryPositionList',
            data: data.value,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        positionIdDom.empty();
                        if (data.data.length > 0) {
                            positionIdDom.append("<option value=''>请选择</option>");
                            for (var i = 0; i < data.data.length; i++) {
                                positionIdDom.append("<option value='" + data.data[i].id + "'>" + data.data[i].name + "</option>");
                            }
                        }
                        form.render('select');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    });

    // 面试企业下拉框监听
    form.on('select(interviewCompanyFilter)', function (data) {
        var positionIdDom = $("#interviewPosition");
        if (data.value === "") {
            positionIdDom.empty();
            form.render('select');
            return false;
        }
        $.ajax({
            type: 'POST',
            url: 'companyPosition/queryListByCompanyId',
            data: data.value,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        positionIdDom.empty();
                        if (data.data.length > 0) {
                            positionIdDom.append("<option value=''>请选择</option>");
                            for (var i = 0; i < data.data.length; i++) {
                                positionIdDom.append("<option value='" + data.data[i].id + "'>" + data.data[i].positionName + "</option>");
                            }
                        }
                        form.render('select');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    });
});