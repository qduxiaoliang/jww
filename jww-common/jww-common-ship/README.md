# jww-common-core
快递服务模块

### 使用步骤
1. 引入模块，pom.xml增加：

```
        <dependency>
            <groupId>com.jww</groupId>
            <artifactId>jww-common-jwt</artifactId>
            <version>1.0.0</version>
        </dependency>
```
2. 增加物流信息配置 

```
# 物流接口配置
ship:
  id: 2ffXXXXXX
  customer: 601461*************53
  key: XXXXXXXXX
  isTest: true
```

3. 注入服务实现类
```
@Autowired
private ShipService shipServiceQD100; 
```

4. 执行查询 
```
ShipDetail shipDetail = shipService.queryFreeFirst("jd", "VJ4103XXXXXXX");
```
