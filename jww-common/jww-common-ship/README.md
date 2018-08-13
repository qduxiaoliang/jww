# jww-common-core
快递服务模块

### 使用步骤
1. 增加物流信息配置 

```
# 物流接口配置
ship:
  id: 2ffXXXXXX
  customer: 601461*************53
  key: XXXXXXXXX
  isTest: true
```

2. 注入服务实现类
```
private ShipService shipServiceQD100; 
```

3. 执行查询 
```
ShipDetail shipDetail = shipService.queryFreeFirst("jd", "VJ4103XXXXXXX");
```
