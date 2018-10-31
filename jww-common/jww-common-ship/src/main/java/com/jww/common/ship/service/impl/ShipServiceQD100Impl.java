package com.jww.common.ship.service.impl;

import com.jww.common.ship.bean.ShipDetail;
import com.jww.common.ship.bean.ShipProperties;
import com.jww.common.ship.service.ShipService;
import com.jww.common.ship.util.ShipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 快递100物流接口实现
 *
 * @author shadj
 * @date 2018/6/26 9:09
 */
@Slf4j
@Service("shipServiceQD100")
public class ShipServiceQD100Impl implements ShipService {

    @Autowired
    private ShipProperties shipProperties;

    /**
     * 查询物流单号的跟踪信息免费接口实现
     *
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @return ShipDetail 物流跟踪详情信息
     * @author shadj
     * @date 2018/6/21 17:21
     */
    @Override

    public ShipDetail queryFree(String com, String shipNumber) {
        log.info("免费查询物流跟踪信息，快递公司：{}，物流单号：{}", com, shipNumber);
        return ShipUtil.queryFree(shipProperties.getId(), com, shipNumber);
    }

    /**
     * 快递100实时快递查询接口实现
     *
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @return ShipDetail 物流跟踪详情信息
     * @author shadj
     * @date 2018/6/21 17:21
     */
    @Override
    public ShipDetail query(String com, String shipNumber) {
        log.info("免费查询物流跟踪信息，快递公司：{}，物流单号：{}", com, shipNumber);
        return ShipUtil.query(shipProperties.getKey(), shipProperties.getCustomer(), com, shipNumber, shipProperties.isTest());
    }

    /**
     * 查询物流单号的跟踪信息，优先调用免费接口
     *
     * @param com        要查询的快递公司代码
     * @param shipNumber 要查询的快递单号
     * @return ShipDetail 物流跟踪详情信息
     * @author shadj
     * @date 2018/6/23 15:30
     */
    @Override
    public ShipDetail queryFreeFirst(String com, String shipNumber) {
        log.info("免费查询物流跟踪信息，快递公司：{}，物流单号：{}", com, shipNumber);
        return ShipUtil.queryFirtFree(shipProperties.getId(), shipProperties.getKey(), shipProperties.getCustomer(), com, shipNumber, shipProperties.isTest());
    }
}
