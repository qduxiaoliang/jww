package com.jww.base.am.common.util;

import com.alibaba.fastjson.JSONObject;
import com.jww.common.core.Constants;
import com.jww.common.redis.util.CacheUtil;
import com.xiaoleilu.hutool.util.ObjectUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * IP工具类
 *
 * @author wanyong
 * @since 2018-01-07
 */
public class IpUtil {

    private static final int CODE_SUCCESS = 200;

    /**
     * 通过接口获得ip地址详情
     *
     * @param urlString
     * @return
     */
    public static String get(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == CODE_SUCCESS) {
                StringBuilder builder = new StringBuilder();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"));
                for (String s = br.readLine(); s != null; s = br
                        .readLine()) {
                    builder.append(s);
                }
                br.close();
                return builder.toString();
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    /**
     * 通过淘宝ip接口获得ip地址详情
     *
     * @param ip
     * @return
     */
    public static String queryIP(String ip) {
        String url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;
        return get(url);
    }

    /**
     * 返回ip地址详情字符串
     *
     * @param ip
     * @return
     */
    public static String getIpDetail(String ip) {
        String unknowIp = "未知ip";
        boolean isIP = checkIP(ip);
        if (!isIP) {
            return unknowIp;
        }
        if (isInnerIP(ip)) {
            return "内网ip";
        }
        String ipDetail = null;
        // 缓存中存在则直接返回缓存结果
        Object cacheIpObj = CacheUtil.getCache().get(Constants.CacheNamespaceEnum.IP.value() + ip);
        if (cacheIpObj != null) {
            return (String) cacheIpObj;
        }
        String res = queryIP(ip);
        if (res == null) {
            return null;
        }
        JSONObject resJ = JSONObject.parseObject(res);
        String codeKey = "code";
        if (ObjectUtil.isNull(resJ.get(codeKey))) {
            return unknowIp;
        }
        int code = Integer.parseInt(resJ.get(codeKey).toString());
        if (code == 0) {
            JSONObject data = resJ.getJSONObject("data");
            ipDetail = data.get("country").toString() + data.get("region").toString() + data.get("city").toString() + data.get("isp").toString();
            //将查询结果放入缓存
            if (!CacheUtil.getCache().exists(Constants.CacheNamespaceEnum.IP.value() + ip)) {
                CacheUtil.getCache().set(Constants.CacheNamespaceEnum.IP.value() + ip, ipDetail, 86400 * 7);
            }

        } else {
            return unknowIp;
        }
        return ipDetail;
    }

    /**
     * 判断是否为内网ip地址
     *
     * @param ipAddress
     * @return
     */
    public static boolean isInnerIP(String ipAddress) {
        boolean isInnerIp = false;
        long ipNum = getIpNum(ipAddress);
        /**
         私有IP：A类  10.0.0.0-10.255.255.255
         B类  172.16.0.0-172.31.255.255
         C类  192.168.0.0-192.168.255.255
         当然，还有127这个网段是环回地址
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || ("127.0.0.1").equals(ipAddress);
        return isInnerIp;
    }

    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
        return ipNum;
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }

    /**
     * 检验ip地址是否合法
     *
     * @param str
     * @return
     */
    private static boolean checkIP(String str) {
        Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }


    public static void main(String[] args) {
        System.out.println(getIpDetail("113.246.78.58"));
    }

}
