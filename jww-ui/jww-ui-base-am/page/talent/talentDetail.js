layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table', 'element'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table,
        element = layui.element;
    talentId = parent.checkedTalentId;
    talentName = '';
    phone = '';
    workRecordId = '';

    $(".layui-form input").prop("disabled", true);
    $(".layui-form textarea").prop("disabled", true);
    if (parent.checkedTalentFollowState !== undefined && parent.checkedTalentFollowState === 3 && !base.isAdmin()) {
        $("#addWorkRecordBtn").hide();
        $("#addInterviewRecordBtn").hide();
    }
    $("#id").val(parent.checkedTalentId);

    var loadingIndex = base.loading(layer);
    // 查询人才
    $.ajax({
        type: 'POST',
        url: 'talent/query4Translate',
        data: JSON.stringify(parent.checkedTalentId),
        success: function (data) {
            if (data.code === 200) {
                layer.close(loadingIndex);
                if (data.data !== null) {
                    for (var item in data.data) {
                        if (item === 'id' || item === 'provinceName' || item === 'cityName' || item === 'districtName'
                            || item === 'provinceId' || item === 'cityId' || item === 'districtId' || item === 'sex'
                            || item === 'talentState' || item === 'infoSrc' || item === 'professionId' || item === 'deptId'
                            || item === 'positionId' || item === 'professionName' || item === 'deptName' || item === 'positionName') {
                            continue;
                        }
                        $("#" + item).val(data.data[item]);
                    }
                    $("#sex").val(data.data['sex'] === 1 ? '男' : '女');
                    $("#city").val(data.data['provinceName'] + "-" + data.data['cityName']);
                    $("#position").val(data.data['professionName'] + "-" + data.data['deptName'] + "-" + data.data['positionName']);
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 渲染工作记录表格
    table.render({
        elem: '#workRecordTable',
        cellMinWidth: 150,
        //设置表头
        cols: [[
            {type: 'numbers'},
            {field: 'createTime', title: '通话时间', align: 'center'},
            {
                field: 'callResult',
                title: '通话结果',
                align: 'center',
                templet: '<div>{{d.callResult == 0 ? "无效" : "有效"}}</div>'
            },
            {field: 'callContent', title: '通话内容', align: 'center'},
            {field: 'nconnTime', title: '下次联系时间', align: 'center'},
            {field: 'userName', title: '业务员', align: 'center'}
        ]],
        url: 'workRecord/listByTalentId',
        method: 'post',
        where: {
            talentId: parent.checkedTalentId
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        }
    });

    // 渲染面试记录表格
    table.render({
        elem: '#interviewRecordTable',
        cellMinWidth: 150,
        //设置表头
        cols: [[
            {type: 'numbers'},
            {field: 'createTime', title: '面试时间', align: 'center'},
            {field: 'companyName', title: '面试企业', align: 'center'},
            {field: 'positionName', title: '面试职位', align: 'center'},
            {field: 'interviewFlowName', title: '面试流程', align: 'center'},
            {field: 'remark', title: '备注', align: 'center'},
            {field: 'userName', title: '业务员', align: 'center'}
        ]],
        url: 'interviewRecord/listByTalentId',
        method: 'post',
        where: {
            talentId: parent.checkedTalentId
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        }
    });

    // 新增工作记录按钮监听
    $("#addWorkRecordBtn").click(function () {
        talentName = $("#talentName").val();
        phone = $("#phone").val();
        workRecordId = parent.workRecordId;
        var index = layui.layer.open({
            title: "新增工作记录",
            type: 2,
            offset: ['0px', '0px'],
            area: ['100%', '100%'],
            content: "workRecord/workRecord.html",
            btnAlign: 'l'
        });
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function () {
            layui.layer.full(index);
        });
    });

    // 新增面试记录按钮监听
    $("#addInterviewRecordBtn").click(function () {
        talentName = $("#talentName").val();
        var index = layui.layer.open({
            title: "新增面试记录",
            type: 2,
            offset: ['0px', '0px'],
            area: ['100%', '100%'],
            content: "interviewRecord/interviewRecord.html",
            btnAlign: 'l'
        });
    });
});

