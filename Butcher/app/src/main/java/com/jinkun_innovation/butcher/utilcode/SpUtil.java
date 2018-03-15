package com.jinkun_innovation.butcher.utilcode;

import com.jinkun_innovation.butcher.utilcode.util.SPUtils;

/**
 * Created by yangxing on 2018/1/23.
 */

public class SpUtil {
    public static void saveBleName(String bleName) {
        SPUtils.getInstance().put("bleName", bleName);
    }

    public static String getBleName() {
        return SPUtils.getInstance().getString("bleName");
    }

    public static void saveBleAddress(String bleAddress) {
        SPUtils.getInstance().put("bleAddress", bleAddress);
    }

    public static String getBleAddress() {

        return SPUtils.getInstance().getString("bleAddress");
    }

    public static void saveIP(String ip) {
        SPUtils.getInstance().put("ip", ip);
    }

    public static String getIP() {
        return SPUtils.getInstance().getString("ip","222.249.165.94");
    }

    public static void savePort(String port) {
        SPUtils.getInstance().put("port", port);
    }

    public static String getPort() {
        return SPUtils.getInstance().getString("port","10100");
    }
}
