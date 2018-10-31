layui.config({
    base: "../../js/"
}).use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;

    var getCaptcha = function () {
        $.ajax({
            type: 'GET',
            url: '/captcha/' + $("#captchaId").val(),
            success: function (data) {
                if (data.code === 200) {
                    $("#captchaImg").attr('src', 'data:image/jpeg;base64,' + data.data.captcha);
                    $("#captchaId").val(data.data.captchaId);
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    };
    getCaptcha();

    // 单击验证码事件
    $("#captchaImg").click(function () {
        getCaptcha();
    });

    // 表单验证
    form.verify({
        code: function (value, item) {
            if (value.length !== 4) {
                return "验证码输入有误";
            }
        }
    });

    //登录按钮事件
    form.on("submit(login)", function (data) {
        //var loadingIndex = base.loading(layer);
        $.ajax({
            type: 'POST',
            url: "/login",
            dataType: 'json',
            contentType: "application/json",
            data: JSON.stringify(data.field),
            success: function (data) {
                //layer.close(loadingIndex);
                if (data.code === 200) {
                    layui.data('JWW_UMP', {
                        key: 'CUURENT_USER', value: data.data
                    });
                    window.location.href = "../../index.html";
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
        return false;
    })
});
