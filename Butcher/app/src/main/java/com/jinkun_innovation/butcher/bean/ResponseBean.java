package com.jinkun_innovation.butcher.bean;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by yangxing on 2018/1/24.
 */

public class ResponseBean {
    private String msg;//   保存成功! /参数接受异常!/ 参数有误！
    private String code;//   200 / 402/403
    private String url;//   二维码访问网址

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}
