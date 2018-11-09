layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'upload', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = "talent/add",
        attachmentName = '';

    // 初始化出生日期
    laydate.render({
        elem: '#birthday',
        type: 'date',
        max: 0
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

    var loadingIndex;
    // 选完文件后不自动上传
    layui.upload.render({
        elem: '#chooseFile',
        url: 'talent/uploadResume',
        auto: false,
        accept: 'file',
        exts: 'doc|docx',
        bindAction: '#upload',
        choose: function (obj) {
            obj.preview(function (index, file, result) {
                attachmentName = file.name;
                $("#attachmentName").val(file.name);
            });
            $("#upload").show();
            $("#uploadTip").show();
        },
        before: function (obj) {
            loadingIndex = base.loading(layer);
            this.data = {
                attachmentId: $("#attachmentId").val(),
                talentId: $("#id").val()
            };
        },
        done: function (res) {
            layer.close(loadingIndex);
            if (res.code === 200) {
                $("#upload").hide();
                $("#uploadTip").hide();
                base.successMsg(layer, "原始简历上传成功");
                $("#attachmentName").val(attachmentName);
                if (res.data !== null) {
                    $("#attachmentId").val(res.data.attachmentId);
                    var talentNameDom = $("#talentName");
                    if (talentNameDom.val() === '') {
                        talentNameDom.val(res.data.talentName);
                    }
                    if (res.data.sex !== null && res.data.sex !== '') {
                        $("#sex").val(res.data.sex);
                        form.render('select');
                    }
                    var addressDom = $("#address");
                    if (addressDom.val() === '') {
                        addressDom.val(res.data.address);
                    }
                    var emailDom = $("#email");
                    if (emailDom.val() === '') {
                        emailDom.val(res.data.email);
                    }
                    var phoneDom = $("#phone");
                    if (phoneDom.val() === '') {
                        phoneDom.val(res.data.phone);
                    }
                    var resumeDom = $("#resume");
                    if (resumeDom.val() === '') {
                        resumeDom.val(res.data.resume);
                    }
                }
            } else {
                base.errorMsg(layer, res.message);
            }

        },
        error: function (index, upload) {
            layer.close(loadingIndex);
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

    // 初始化人才状态下拉列表数据，字典类型值：TALENT_STATE
    $.ajax({
        type: 'POST',
        url: 'dic/listByType',
        data: JSON.stringify("TALENT_STATE"),
        success: function (data) {
            if (data.code === 200) {
                if (data.data !== null) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#talentState").append("<option value='" + data.data[i].code + "'>" + data.data[i].codeText + "</option>");
                    }
                    form.render('select');
                }
            } else {
                layer.msg(data.message, {icon: 2});
            }
        }
    });

    // 初始化所属省份下拉列表数据，字典类型值：TALENT_STATE
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

    // 初始化所属行业
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

    // 获取父页面的pageOperation，判断是查看、新增、修改
    if (parent.pageOperation === 1) { // 新增
    } else if (parent.pageOperation === 2) { // 修改
        submitUrl = "talent/modify";
        // 判断用户是否具有sys:admin权限，如果没有则不允许修改姓名、手机号码、信息来源
        if (!base.isAdmin()) {
            $("#talentName").attr('disabled', true);
            $("#talentName").addClass("layui-disabled");
            $("#phone").attr('disabled', true);
            $("#phone").addClass("layui-disabled");
            $("#infoSrc").attr('disabled', true);
            $("#infoSrc").addClass("layui-select-disabled");
        }

    } else { // 查看
        $(".layui-form input").prop("disabled", true);
        $(".layui-form textarea").prop("disabled", true);
        $('.layui-form button').hide();
    }

    if (parent.pageOperation === 2 || parent.pageOperation === 0) {
        $("#id").val(parent.checkedTalentId);
        if (parent.checkedProvinceId !== undefined && parent.checkedProvinceId !== null && parent.checkedProvinceId !== '') {
            // 初始化城市下拉框
            $.ajax({
                type: 'POST',
                url: 'area/list',
                data: JSON.stringify(parent.checkedProvinceId),
                success: function (data) {
                    if (data.code === 200) {
                        if (data.data !== null) {
                            for (var i = 0; i < data.data.length; i++) {
                                $("#cityId").append("<option value='" + data.data[i].id + "'>" + data.data[i].city + "</option>");
                            }
                            form.render('select');
                        }
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        }

        if (parent.checkedProfessionId !== undefined && parent.checkedProfessionId !== null && parent.checkedProfessionId !== '') {
            // 初始化行业部门下拉框
            $.ajax({
                type: 'POST',
                url: 'profession/queryDeptList',
                data: JSON.stringify(parent.checkedProfessionId),
                success: function (data) {
                    if (data.code === 200) {
                        if (data.data !== null) {
                            for (var i = 0; i < data.data.length; i++) {
                                $("#deptId").append("<option value='" + data.data[i].id + "'>" + data.data[i].name + "</option>");
                            }
                            form.render('select');
                        }
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        }

        if (parent.checkedDeptId !== undefined && parent.checkedDeptId !== null && parent.checkedDeptId !== '') {
            // 初始化行业部门职位下拉框
            $.ajax({
                type: 'POST',
                url: 'profession/queryPositionList',
                data: JSON.stringify(parent.checkedDeptId),
                success: function (data) {
                    if (data.code === 200) {
                        if (data.data !== null) {
                            for (var i = 0; i < data.data.length; i++) {
                                $("#positionId").append("<option value='" + data.data[i].id + "'>" + data.data[i].name + "</option>");
                            }
                            form.render('select');
                        }
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        }

        loadingIndex = base.loading(layer);
        // 查询人才
        $.ajax({
            type: 'POST',
            url: 'talent/query',
            data: JSON.stringify(parent.checkedTalentId),
            success: function (data) {
                layer.close(loadingIndex);
                if (data.code === 200) {
                    if (data.data !== null) {
                        for (var item in data.data) {
                            $("#" + item).val(data.data[item]);
                        }
                        form.render('select');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    }

    // 监听submit
    form.on('submit(addFilter)', function (data) {
        var loadingIndex = base.loading(layer);
        $.ajax({
            type: 'POST',
            url: submitUrl,
            data: JSON.stringify(data.field),
            success: function (data) {
                layer.close(loadingIndex);
                if (data.code === 200) {
                    if (parent.pageOperation === 1) {
                        base.successMsg(layer, "人才新增成功");
                    } else {
                        base.successMsg(layer, "人才修改成功");
                    }
                    layer.closeAll("iframe");
                    parent.document.getElementById("search").click();
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });

        // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    // 打开人才重复检测结果窗口
    var openDuplicateTalentValidator = function () {
        $(window).one("resize", function () {
            checkDuplicateTalentName = $("#talentName").val();
            checkDuplicateTalentPhone = $("#phone").val();
            layui.layer.open({
                title: "人才重复检测",
                type: 2,
                area: ['80%', '60%'],
                offset: ['0px', '0px'],
                btn: ['我知道了'],
                content: "talentDuplicate.html"
            });
        }).resize();
    };

    // 查询人才重复
    var queryDuplicateTalentValidator = function (talentName, phone) {
        $.ajax({
            type: 'POST',
            url: 'talent/count',
            data: JSON.stringify({
                talentName: talentName,
                phone: phone
            }),
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== 0) {
                        openDuplicateTalentValidator();
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    };

    // 人才姓名失去焦点事件
    $("#talentName").change(function () {
        queryDuplicateTalentValidator($("#talentName").val(), $("#phone").val());
    });

    // 手机号码失去焦点事件
    $("#phone").change(function () {
        queryDuplicateTalentValidator($("#talentName").val(), $("#phone").val());
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

    // 抓取简历关键数据单击事件
    $("#extractKeyData").click(function () {
        if (base.fastClickCheck("#extractKeyData")) {
            var resume = $("#resume").val();
            if (resume !== '') {
                loadingIndex = base.loading(layer);
                $.ajax({
                    type: 'POST',
                    url: 'talent/extractResumeKeyData',
                    data: JSON.stringify(resume),
                    success: function (data) {
                        layer.close(loadingIndex);
                        if (data.code === 200) {
                            if (data.data !== null) {
                                var talentNameDom = $("#talentName");
                                if (talentNameDom.val() === '') {
                                    talentNameDom.val(data.data.talentName);
                                }
                                var phoneDom = $("#phone");
                                if (phoneDom.val() === '') {
                                    phoneDom.val(data.data.phone);
                                }
                                var emailDom = $("#email");
                                if (emailDom.val() === '') {
                                    emailDom.val(data.data.email);
                                }
                                if (data.data.sex !== null && data.data.sex !== '') {
                                    $("#sex").val(data.data.sex);
                                    form.render('select');
                                }
                                var addressDom = $("#address");
                                if (addressDom.val() === '') {
                                    addressDom.val(data.data.address);
                                }
                            }
                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
                queryDuplicateTalentValidator($("#talentName").val(), $("#phone").val());
            }
        }
    })

});

