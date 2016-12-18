package com.threepapa.vmtcp.activitys;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.threepapa.vmtcp.R;
import com.threepapa.vmtcp.domain.sys_read;
import com.threepapa.vmtcp.domain.sys_surplus;
import com.threepapa.vmtcp.utils.PreferenceUtils;
import com.threepapa.vmtcp.utils.SharedPreferencesKeys;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by wu on 2016/9/26.
 * 设置滤芯
 */
public class SettingActivity extends BaseActivity {

    private EditText seekBar_high;
    private EditText seekBar_middle;
    private EditText seekBar_low;

    private Button btn_reset;

    @Override
    protected int setContentByXML() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        seekBar_high = (EditText) findViewById(R.id.seekBar_high);
        seekBar_middle = (EditText) findViewById(R.id.seekBar_middle);
        seekBar_low = (EditText) findViewById(R.id.seekBar_low);

        btn_reset = (Button) findViewById(R.id.btn_reset);

    }

    @Override
    protected void initListener() {
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.put(SharedPreferencesKeys.TOP, Integer.valueOf(seekBar_high.getText().toString()));
                PreferenceUtils.put(SharedPreferencesKeys.MIDDLE, Integer.valueOf(seekBar_middle.getText().toString()));
                PreferenceUtils.put(SharedPreferencesKeys.BOTTOM, Integer.valueOf(seekBar_low.getText().toString()));

                String format = String.format("&3|1^%s|2^%s|3^%s|%s\r\n", seekBar_low.getText().toString(),
                        seekBar_middle.getText().toString(),
                        seekBar_high.getText().toString(),
                        MainActivity.MyOnRecvDataListener.returnStu);
                mApp.sendData(format);
            }

        });
    }

    @Override
    protected void initData() {
        Integer bottom = PreferenceUtils.get(SharedPreferencesKeys.BOTTOM, 0);
        Integer middle = PreferenceUtils.get(SharedPreferencesKeys.MIDDLE, 0);
        Integer top = PreferenceUtils.get(SharedPreferencesKeys.TOP, 0);
        seekBar_high.setText(String.valueOf(top));
        seekBar_middle.setText(String.valueOf(middle));
        seekBar_low.setText(String.valueOf(bottom));
    }


    @Subscribe // 接收到从服务器发送过来的set_surplus的命令
    public void onReportEvent(sys_surplus setSurplusEvent) {
        if (setSurplusEvent.top != null) {
            seekBar_high.setText(String.valueOf(setSurplusEvent.top));
        }
        if (setSurplusEvent.middle != null) {
            seekBar_middle.setText(String.valueOf(setSurplusEvent.middle));
        }
        if (setSurplusEvent.bottom != null) {
            seekBar_low.setText(String.valueOf(setSurplusEvent.bottom));
        }
    }

    @Subscribe // 发送滤芯寿命
    public void onReportEvent(sys_read setSurplusEvent) {
        btn_reset.callOnClick();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
































