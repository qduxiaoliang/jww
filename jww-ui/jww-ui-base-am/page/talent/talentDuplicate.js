layui.config({
    base: "../../js/"
}).use(['base', 'jquery', 'table'], function () {
    var base = layui.base,
        $ = layui.jquery,
        table = layui.table;

    // 渲染表格
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers'},
            {field: 'talentName', title: '姓名', align: 'center'},
            {field: 'keyWord', title: '关键字', align: 'center'},
            {field: 'phone', title: '手机号码', align: 'center'},
            {field: 'professionName', title: '行业', align: 'center'},
            {field: 'positionName', title: '职位', align: 'center'},
            {field: 'followStateName', title: '跟进状态', align: 'center'},
            {field: 'userName', title: '业务员', align: 'center'},
            {field: 'createTime', title: '录入时间', align: 'center'},
            {field: 'protectionDate', title: '保护截止日期', align: 'center'}
        ]],
        url: 'talent/listPage4Senior',
        method: 'post',
        where: { // 设定异步数据接口的额外参数，任意设
            condition: {
                talent_name: parent.checkDuplicateTalentName,
                phone_: parent.checkDuplicateTalentPhone
            }
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        },
        elem: '#talentDuplicateTable'
    });
});