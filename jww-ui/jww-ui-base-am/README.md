layui源码修改：
1. table.js中修改.ajax请求data参数，外层包装JSON.stringify
2. 修改laydate.css文件，把.layui-laydate-range样式width调整为：550px（由于页面缩放默认宽度会造成换行）