package com.jww.common.ship.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.jww.common.core.exception.BusinessException;
import com.jww.common.ship.bean.ShipDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 快递工具类
 *
 * @author shadj
 * @date 2018/6/21 17:14
 */
@Slf4j
public class ShipUtil {

    private static final String QUERY_URL = "https://poll.kuaidi100.com/poll/query.do";
    private static final String QUERY_URL_TEST = "https://poll.kuaidi100.com/test/poll/query.do";
    private static final String QUERY_URL_FREE = "http://api.kuaidi100.com/api?id={}&com={}&nu={}&valicode=&show=0&muti=1&order=desc";
    /**
     * 免费版快递公司黑名单
     */
    private static List<String> freeComBlackList = Arrays.asList("shunfeng", "yuantong", "shentong", "yunda", "ems", "zhongtong");

    /**
     * 快递100实时快递查询接口实现
     *
     * @param key        身份授权key
     * @param customer   快递100分配的公司编号
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @return JSONObject 对象
     * @author shadj
     * @date 2018/6/21 17:21
     */
    public static ShipDetail query(String key, String customer, String com, String shipNumber) {
        return query(key, customer, com, shipNumber, true);
    }

    /**
     * 快递100实时快递查询接口实现
     *
     * @param key        身份授权key
     * @param customer   快递100分配的公司编号
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @param isTest     是否是测试
     * @return ShipDetail 物流跟踪详情信息
     * @author shadj
     * @date 2018/6/21 17:21
     */
    public static ShipDetail query(String key, String customer, String com, String shipNumber, boolean isTest) {
        Map map = new LinkedHashMap();
        map.put("com", com);
        map.put("num", shipNumber);
        map.put("from", "");
        map.put("to", "");
        map.put("resultv2", 0);
        String paramStr = JSON.toJSONString(map);
        String sign = SecureUtil.md5(paramStr + key + customer).toUpperCase();
        HashMap params = new HashMap();
        params.put("param", paramStr);
        params.put("sign", sign);
        params.put("customer", customer);
        String urlString = QUERY_URL;
        if (isTest) {
            urlString = QUERY_URL_TEST;
        }
        log.info("urlString: {}, params: {}", urlString, params);
        String resp = HttpUtil.post(urlString, params);
        if (StrUtil.isBlank(resp)) {
            throw new BusinessException("查询物流跟踪信息完成，无响应数据");
        }
        ShipDetail shipDetail = JSON.parseObject(resp, ShipDetail.class);
        return shipDetail;
    }

    /**
     * 查询物流单号的跟踪信息免费接口实现
     *
     * @param id         身份授权key
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @return ShipDetail 物流跟踪详情信息
     * @author shadj
     * @date 2018/6/21 17:21
     */
    public static ShipDetail queryFree(String id, String com, String shipNumber) {
        String urlString = StrUtil.format(QUERY_URL_FREE, id, com, shipNumber, 1);
        log.info("urlString: {}", urlString);
        String resp = HttpUtil.post(urlString, "");
        log.info("查询物流单号的跟踪信息免费接口响应：{}", resp);
        ShipDetail shipDetail = JSON.parseObject(resp, ShipDetail.class);
        return shipDetail;
    }

    /**
     * 查询物流单号的跟踪信息，优先调用免费接口
     *
     * @param id         身份授权key——免费版
     * @param key        身份授权key——企业版
     * @param customer   快递100分配的公司编号——企业版
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @return ShipDetail 物流跟踪详情信息
     * @author shadj
     * @date 2018/6/23 15:30
     */
    public static ShipDetail queryFirtFree(String id, String key, String customer, String com, String shipNumber) {
        return queryFirtFree(id, key, customer, com, shipNumber, true);
    }

    /**
     * 查询物流单号的跟踪信息，优先调用免费接口
     *
     * @param id         身份授权key——免费版
     * @param key        身份授权key——企业版
     * @param customer   快递100分配的公司编号——企业版
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @param isTest     是否是测试
     * @return ShipDetail 物流跟踪详情信息
     * @author shadj
     * @date 2018/6/23 15:30
     */
    public static ShipDetail queryFirtFree(String id, String key, String customer, String com, String shipNumber, boolean isTest) {
        if (freeComBlackList.contains(com)) {
            return query(key, customer, com, shipNumber, isTest);
        }
        ShipDetail shipDetail = queryFree(id, com, shipNumber);
        if (ObjectUtil.isNull(shipDetail) || CollectionUtil.isEmpty(shipDetail.getData())) {
            //如果免费版没查到数据，则调用企业版
            shipDetail = query(key, customer, com, shipNumber, isTest);
        }
        return shipDetail;
    }
}
