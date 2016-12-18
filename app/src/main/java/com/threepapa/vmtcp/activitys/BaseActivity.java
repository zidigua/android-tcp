package com.threepapa.vmtcp.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.threepapa.vmtcp.app.MyApp;

/**
 * Created by wu on 2016/9/29.
 * 基类
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BaseActivity";

    protected Context mContext;
    protected MyApp mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉标题栏

        mContext = getApplicationContext();

        mApp = ((MyApp) getApplication());

        setContentView(setContentByXML());

        initView();
        initListener();
        initData();
    }

    protected abstract int setContentByXML();

    protected abstract void initListener();

    protected abstract void initView();

    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                onLocalViewClick(v);
                break;
        }
    }

    protected void onLocalViewClick(View v) {

    }




}
