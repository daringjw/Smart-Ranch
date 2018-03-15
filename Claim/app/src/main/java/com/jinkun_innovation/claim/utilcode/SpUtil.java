package com.jinkun_innovation.claim.utilcode;

import com.jinkun_innovation.claim.utilcode.util.SPUtils;

/**
 * Created by yangxing on 2018/3/9.
 */

public class SpUtil {
    public static void saveAccount(String username) {
        SPUtils.getInstance().put("username", username);
    }

    public static void getAccount() {
        SPUtils.getInstance().getString("username");
    }
}
