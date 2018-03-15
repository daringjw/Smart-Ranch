package com.jinkun_innovation.claim;

import android.app.Application;
import android.content.Context;

import com.jinkun_innovation.claim.utilcode.util.LogUtils;
import com.jinkun_innovation.claim.utilcode.util.Utils;
import com.jinkun_innovation.claim.weight.callback.CustomCallback;
import com.jinkun_innovation.claim.weight.callback.EmptyCallback;
import com.jinkun_innovation.claim.weight.callback.ErrorCallback;
import com.jinkun_innovation.claim.weight.callback.LoadingCallback;
import com.jinkun_innovation.claim.weight.callback.TimeoutCallback;
import com.kingja.loadsir.core.LoadSir;


/**
 * Created by yangxing on 2017/10/26.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        initLog();
        initLoad();
    }

    private void initLoad() {
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }

    private void initLog() {
        LogUtils.Config config = LogUtils.getConfig()
                .setLogSwitch(BuildConfig.DEBUG)// 设置 log 总开关，包括输出到控制台和文件，默认开
                .setConsoleSwitch(BuildConfig.DEBUG)// 设置是否输出到控制台开关，默认开
                .setGlobalTag(null)// 设置 log 全局标签，默认为空
                // 当全局标签不为空时，我们输出的 log 全部为该 tag，
                // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                .setLogHeadSwitch(true)// 设置 log 头信息开关，默认为开
                .setLog2FileSwitch(false)// 打印 log 时是否存到文件的开关，默认关
                .setDir("")// 当自定义路径为空时，写入应用的/cache/log/目录中
                .setFilePrefix("")// 当文件前缀为空时，默认为"util"，即写入文件为"util-MM-dd.txt"
                .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
                .setConsoleFilter(LogUtils.V)// log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(LogUtils.V)// log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setStackDeep(1);// log 栈深度，默认为 1
        LogUtils.d(config.toString());
    }

    public  Context mContext;

    public Context getContext() {
        return getApplicationContext();
    }
}
