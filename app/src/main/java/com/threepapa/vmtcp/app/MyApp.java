package com.threepapa.vmtcp.app;

import android.app.Application;
import android.content.Context;

import com.threepapa.vmtcp.activitys.MainActivity;
import com.threepapa.vmtcp.utils.PreferenceUtils;

/**
 * Created by wu on 2016/9/29.
 */
public class MyApp extends Application {

    private static final String TAG = "MyApp";

    public Context mContext;
    private String url_dev = "tcp://112.126.73.73:9001";
    private String url_pz = "tcp://ac.sangebaba.com:9417";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    /**
     *  发送数据 接口
     *  @param path 要发送的数据
     *
     */
    public native void sendData(String path);

    /**
     * 接收数据监听
     * @param l 回调对象
     */
    public native void setOnRecvDataListener(MainActivity.MyOnRecvDataListener l);

    /**
     *  @param isTest 是否连接到测试环境
     */
    public native void tcpClientConn(boolean isTest, String mac);

    /**
     * 断开连接
     */
    public native void tcpClientDisconn();


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        initSP();
        initTcp();

    }

    private void initSP() {
        PreferenceUtils.initialize(mContext);
    }

    private void initTcp() {

    }
}
