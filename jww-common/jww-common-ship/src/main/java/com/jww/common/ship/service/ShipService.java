package com.jww.common.ship.service;

import com.jww.common.ship.bean.ShipDetail;

/**
 * 快递服务类
 *
 * @author shadj
 * @date 2018/6/25 18:15
 */
public interface ShipService {

    /**
     * 查询物流单号的跟踪信息免费接口实现
     *
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @return ShipDetail 物流跟踪详情信息
     * @author shadj
     * @date 2018/6/21 17:21
     */
     ShipDetail queryFree(String com, String shipNumber);

    /**
     * 快递100实时快递查询接口实现
     *
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @return ShipDetail 物流跟踪详情信息
     * @author shadj
     * @date 2018/6/21 17:21
     */
    ShipDetail query(String com, String shipNumber);

    /**
     * 查询物流单号的跟踪信息，优先调用免费接口
     *
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @return ShipDetail 物流跟踪详情信息
     * @author shadj
     * @date 2018/6/23 15:30
     */
    ShipDetail queryFreeFirst(String com, String shipNumber);
}
