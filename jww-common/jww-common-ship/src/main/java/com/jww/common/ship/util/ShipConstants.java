package com.jww.common.ship.util;

/**
 * 物流枚举
 *
 * @author shadj
 * @date 2018/6/23 11:14
 */
public class ShipConstants {

    /**
     * 查询结果状态：
     * 0：物流单暂无结果，
     * 1：查询成功，
     * 2：接口出现异常
     */
    public enum Status {
        /**
         * 物流单暂无结果
         */
        NONE("0", "物流单暂无结果"),
        /**
         * 查询成功
         */
        SUCCESS("1", "查询成功"),
        /**
         * 接口出现异常
         */
        FAIL("2", "接口出现异常");

        private final String value;
        private final String desc;

        Status(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String value() {
            return this.value;
        }

        public String getDesc() {
            return this.desc;
        }

    }

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
    public enum State {
        /**
         * 在途
         */
        ON_WAY("0", "在途"),
        /**
         * 揽件
         */
        COLLECTION("1", "揽件"),
        /**
         * 疑难
         */
        DIFFICULT("2", "疑难"),
        /**
         * 签收
         */
        SIGN("3", "签收"),
        /**
         * 退签
         */
        BACK_SIGN("4", "退签"),
        /**
         * 派件
         */
        SEND("5", "派件"),
        /**
         * 退回
         */
        RETURN("6", "退回");

        private final String value;
        private final String desc;

        State(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String value() {
            return this.value;
        }

        public String getDesc() {
            return this.desc;
        }

    }
}
