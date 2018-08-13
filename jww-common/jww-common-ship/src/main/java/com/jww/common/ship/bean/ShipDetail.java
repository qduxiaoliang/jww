package com.jww.common.ship.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 物流跟踪详情信息
 *
 * @author shadj
 * @date 2018/6/23 11:08
 */
@Data
public class ShipDetail implements Serializable {
    /**
     *
     */
    private String message;
    /**
     * 查询结果状态：
     * 0：物流单暂无结果，
     * 1：查询成功，
     * 2：接口出现异常
     */
    private String status;
    /**
     * 快递单当前的状态
     * 0：在途，即货物处于运输过程中；
     * 1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息；
     * 2：疑难，货物寄送过程出了问题；
     * 3：签收，收件人已签收；
     * 4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收；
     * 5：派件，即快递正在进行同城派件；
     * 6：退回，货物正处于退回发件人的途中；
     */
    private String state;
    /**
     * 物流公司编号
     */
    private String com;
    /**
     * 物流单号
     */
    private String nu;
    /**
     * 是否签收标记
     */
    private String ischeck;
    /**
     * 物流跟踪信息列表
     */
    List<ShipContent> data = new ArrayList();

}
