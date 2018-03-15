package com.jinkun_innovation.claim.login_model;

import com.jinkun_innovation.claim.base.BaseView;
import com.jinkun_innovation.claim.bean.LoginBean;

/**
 * Created by yangxing on 2018/3/5.
 */

public interface LoginView extends BaseView {
    void toLogin(LoginBean bean);
}
