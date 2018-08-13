package com.jww.common.ship.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 物流跟踪信息
 *
 * @author shadj
 * @date 2018/6/23 11:05
 */
@Data
public class ShipContent implements Serializable {

    /**
     * 跟踪信息的时间
     */
    private String time;

    /**
     * 跟综信息的描述
     */
    private String context;
}
