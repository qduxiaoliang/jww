layui.config({
    base: "../js/"
}).use(['base', 'element', 'layer', 'jquery', 'laydate', 'util'], function () {
    var base = layui.base,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        element = layui.element,
        util = layui.util,
        $ = layui.jquery,
        longitude, latitude;

    //获取当前地址
    if (navigator.geolocation) {
        var options = {
            // 指示浏览器获取高精度的位置，默认为false
            enableHighAccuracy: true,
            // 指定获取地理位置的超时时间，默认不限时，单位为毫秒
            timeout: 3000,
            // 最长有效期，在重复获取地理位置时，此参数指定多久再次获取位置
            maximumAge: 1000
        }
        navigator.geolocation.getCurrentPosition(onSuccess, onError, options);
    } else {
        // layer.msg("浏览器不支持获取当前地址!");
    }

    //成功时
    function onSuccess(position) {
        //返回用户位置:经度,纬度
        latitude = position.coords.latitude;
        longitude = position.coords.longitude;
    }

    //失败时
    function onError(error) {
    }

    $.ajax({
        type: 'POST',
        url: 'check/queryToday',
        success: function (data) {
            if (data.code === 200) {
                if(data.data != null){
                    var cinTime = data.data.cinTime;
                    var state = data.data.state;
                    if (cinTime && cinTime.length !== 0){
                        $(".startWork").css('display','none');
                        $("#cinTime").css('display','block');
                        var stateStr = state === 2 ? "（迟到）" : (state === 8 ? "（迟到）" : "");
                        $("#cinTime").html("<b>签到时间：</b>"+data.data.cinTime + stateStr);
                    }
                    var coutTime = data.data.coutTime;
                    if (coutTime && coutTime.length !== 0){
                        $(".startWork").css('display','none');
                        $(".offWork").css('display','none')
                        $("#coutTime").css('display','block')
                        var stateStr = state === 4 ? "（早退）" : (state === 8 ? "（早退）" : "");
                        $("#coutTime").html("<b>签退时间：</b>"+data.data.coutTime + stateStr);
                    }
                }
            }
        }
    });

    // 上班签到
    $(".startWork").click(function () {
        var option = {};
        option["latitude"] = latitude;
        option["longitude"] = longitude;

        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        $.ajax({
            type: 'POST',
            url: 'check/checkIn',
            data: JSON.stringify(option),
            success: function (data) {
                if (data.code === 200) {
                    //弹出loading
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg('操作成功！签到时间：' + util.toDateString(data.data.cinTime, 'yyyy-MM-dd HH:mm:ss'), {icon: 1});
                        location.reload();
                    }, 1000);
                } else {
                    layer.close(index);
                    layer.msg(data.message, {icon: 2});
                }
            }
        });

    });
    // 下班打卡
    $(".offWork").click(function () {
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        $.ajax({
            type: 'POST',
            url: 'check/checkOut',
            success: function (data) {
                if (data.code === 200) {
                    //弹出loading
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg('操作成功！签退时间：' + util.toDateString(data.data.coutTime, 'yyyy-MM-dd HH:mm:ss'), {icon: 1});
                        location.reload();
                    }, 1000);
                } else {
                    layer.close(index);
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    });
})
