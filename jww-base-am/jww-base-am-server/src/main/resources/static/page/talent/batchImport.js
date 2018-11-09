layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'upload', 'table', 'element'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table,
        tableData = [];

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

    // 渲染表格
    var tableIns = table.render({
        // 设置表头
        cols: [[
            {
                field: 'fileName',
                title: '文件名',
                align: 'center'
            },
            {field: 'fileSize', title: '大小（KB）', align: 'center', width: 150},
            {
                field: 'result', title: '状态', align: 'center', sort: true, width: 150,
                templet: function (d) {
                    if (d.result === 1) {
                        return "<div style='background-color: #009688'><span style='color: white'>上传成功</span></div>";
                    } else if (d.result === 2) {
                        return "<div style='background-color: #FF5722'><span style='color: white'>失败</span></div>";
                    } else {
                        return "待上传";
                    }
                }
            },
            {field: 'errorMsg', title: '错误信息', align: 'center'},
            {field: 'opt', title: '操作', width: 100, align: 'center', fixed: 'right', toolbar: '#toolBar'}
        ]],
        data: tableData,
        elem: '#talentTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 50, 100]
        }
    });

    var loadingIndex;
    var errorFileCount = 0;
    var files;
    // 拖拽上传
    layui.upload.render({
        elem: '#selectResume',
        url: 'talent/batchImportResumeItem',
        auto: false,
        accept: 'file',
        exts: 'doc|docx',
        size: 5120,
        multiple: true,
        number: 200,
        bindAction: '#upload',
        choose: function (obj) {
            files = obj.pushFile();
            tableData = [];
            layui.each(files, function (index) {
                var file = files[index];
                var item = {
                    index: index,
                    fileName: file.name,
                    fileSize: parseInt(file.size / 1000),
                    errorMsg: '',
                    result: 0
                };
                tableData.push(item);
            });
            tableIns.reload({
                data: tableData
            });
            $("#delAll").removeClass("layui-disabled").attr("disabled", false);
            $("#successFileCount").text(0);
            $("#failFileCount").text(0);
        },
        before: function (obj) {
            if ($.isEmptyObject(files)) {
                base.normalMsg(layer, "没有待上传的文件");
                return;
            }
            loadingIndex = base.loading(layer);
            tableData = [];
            errorFileCount = 0;
            this.data = {
                infoSrc: $("#infoSrc").val(),
                keyWord: $("#keyWord").val(),
                company: $("#company").val(),
                professionId: $("#professionId").val(),
                deptId: $("#deptId").val(),
                positionId: $("#positionId").val(),
                provinceId: $("#provinceId").val(),
                cityId: $("#cityId").val()
            };
        },
        done: function (res, index, upload) {
            var item = {
                fileName: files[index].name,
                fileSize: parseInt(files[index].size / 1000),
                result: 1,
                errorMsg: ''
            };
            if (res.code !== 200) {
                errorFileCount++;
                item["result"] = 2;
                item["errorMsg"] = res.message
            }
            tableData.push(item);
            delete files[index];
        },
        error: function (index, upload) {
            errorFileCount++;
            var item = {
                fileName: files[index].name,
                fileSize: parseInt(files[index].size / 1000),
                result: 2,
                errorMsg: '网络异常'
            };
            delete files[index];
        },
        allDone: function (obj) { //当文件全部被提交后，才触发
            tableIns.reload({
                data: tableData
            });
            $("#successFileCount").text(obj.total - errorFileCount);
            $("#failFileCount").text(errorFileCount);
            $("#delAll").addClass("layui-disabled").attr("disabled", true);
            layer.close(loadingIndex);
        }
    });

    //监听工具条
    table.on('tool(tableFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'del') { // 删除
            var confirmIndex = layer.confirm('您确定要移除吗？', {icon: 3, title: '确认'}, function () {
                obj.del();
                delete files[data.index];
                layer.close(confirmIndex);
            });
        }
    });

    // 清空列表
    $("#delAll").click(function () {
        var confirmIndex = layer.confirm('您确定要移除所有文件吗？', {icon: 3, title: '确认'}, function () {
            layui.each(files, function (index) {
                delete files[index];
            });
            tableData = [];
            tableIns.reload({
                data: tableData
            });
            layer.close(confirmIndex);
            $("#delAll").addClass("layui-disabled").attr("disabled", true);
        });
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