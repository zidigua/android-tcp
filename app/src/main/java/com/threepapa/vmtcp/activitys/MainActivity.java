package com.threepapa.vmtcp.activitys;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.threepapa.vmtcp.R;
import com.threepapa.vmtcp.domain.sys_read;
import com.threepapa.vmtcp.domain.sys_surplus;
import com.threepapa.vmtcp.utils.Log;
import com.threepapa.vmtcp.utils.PreferenceUtils;
import com.threepapa.vmtcp.utils.SharedPreferencesKeys;
import com.threepapa.vmtcp.utils.ToastUtils;
import com.zcw.togglebutton.ToggleButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class MainActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    private Button btn_send_msg;
    private Button btn_setting;
    private Button btn_send_sensor;
    private Button btn_send_sys_status;
    private Button btn_set_mac;
    private Button btn_dev_pz;


    private EditText et_pm2_5;
    private EditText et_co2;
    private EditText et_hum;
    private EditText et_temp;
    private EditText et_temp_out;
    private EditText et_mac;


    private RadioGroup rg_wind;
    private RadioGroup rg_mode;

    private RadioButton rb_mode_freedom;
    private RadioButton rb_mode_manual;

    private ToggleButton tb_child_lock;
    private ToggleButton tb_led;
    private ToggleButton tb_ele_heat;
    private ToggleButton tb_power;

    private TextView tv_child_lock;
    private TextView tv_led;
    private TextView tv_ele_heat;
    private TextView tv_power;

    private TreeMap<Integer, Integer> mWindDataMap = new TreeMap<>();
    private TreeMap<Integer, Integer> mModeDataMap = new TreeMap<>();


    @Subscribe
    public int setContentByXML() {
        return R.layout.activity_main;
    }


    @Override
    public void initView() {
        btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_send_sensor = (Button) findViewById(R.id.btn_send_sensor);
        btn_send_sys_status = (Button) findViewById(R.id.btn_send_sys_status);
        btn_set_mac = (Button) findViewById(R.id.btn_set_mac);
        btn_dev_pz = (Button) findViewById(R.id.btn_dev_pz);


        et_pm2_5 = (EditText) findViewById(R.id.et_pm2_5);
        et_co2 = (EditText) findViewById(R.id.et_co2);
        et_temp = (EditText) findViewById(R.id.et_temp);
        et_mac = (EditText) findViewById(R.id.et_mac);


        rg_wind = (RadioGroup) findViewById(R.id.rg_wind);
        rg_mode = (RadioGroup) findViewById(R.id.rg_mode);


        rb_mode_freedom = ((RadioButton) findViewById(R.id.rb_mode_freedom));
        rb_mode_manual = ((RadioButton) findViewById(R.id.rb_mode_manual));


        tb_child_lock = (ToggleButton) findViewById(R.id.tb_child_lock);
        tb_led = (ToggleButton) findViewById(R.id.tb_led);
        tb_ele_heat = (ToggleButton) findViewById(R.id.tb_ele_heat);
        tb_power = (ToggleButton) findViewById(R.id.tb_power);

        tv_child_lock = (TextView) findViewById(R.id.tv_child_lock);
        tv_led = (TextView) findViewById(R.id.tv_led);
        tv_ele_heat = (TextView) findViewById(R.id.tv_ele_heat);
        tv_power = (TextView) findViewById(R.id.tv_power);


        String mac = PreferenceUtils.get(SharedPreferencesKeys.MAC, "60C5A86385B3").toUpperCase();
        et_mac.setText(mac);

    }

    /**
     * @author bruce.z
     */
    public class AllCapTransformationMethod extends ReplacementTransformationMethod {

        @Override
        protected char[] getOriginal() {
            return new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        }

        @Override
        protected char[] getReplacement() {
            return new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        }

    }

    @Override
    public void initListener() {

        et_mac.setTransformationMethod(new AllCapTransformationMethod());

        btn_send_msg.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        btn_send_sensor.setOnClickListener(this);
        btn_send_sys_status.setOnClickListener(this);
        btn_set_mac.setOnClickListener(this);
        btn_dev_pz.setOnClickListener(this);

//        tb_child_lock.setOnToggleChanged(onToggleChanged);
//        tb_led.setOnToggleChanged(onToggleChanged);
//        tb_ele_heat.setOnToggleChanged(onToggleChanged);
//        tb_power.setOnToggleChanged(onToggleChanged);

        rg_wind.setOnCheckedChangeListener(this);
        rg_mode.setOnCheckedChangeListener(this);

        //  初始化链接
        String mac = PreferenceUtils.get(SharedPreferencesKeys.MAC, "60C5A86385B3").toUpperCase();
        boolean isDebuggingEnv = PreferenceUtils.get(SharedPreferencesKeys.ISDEBUG, false);
        String format = String.format("&1|%s|3.0.1|110\r\n", mac);

        mApp.tcpClientConn(isDebuggingEnv, format);
        mApp.setOnRecvDataListener(new MyOnRecvDataListener(this));

    }

    @Override
    protected void initData() {
        mWindDataMap.put(R.id.rb_wind_dormant, 0);
        mWindDataMap.put(R.id.rb_wind_sound_off, 1);
        mWindDataMap.put(R.id.rb_wind_low, 2);
        mWindDataMap.put(R.id.rb_wind_middle, 3);
        mWindDataMap.put(R.id.rb_wind_high, 4);
        mWindDataMap.put(R.id.rb_wind_higher, 5);


        mModeDataMap.put(R.id.rb_mode_freedom, 1);
        mModeDataMap.put(R.id.rb_mode_manual, 0);

        boolean isDebuggingEnv = PreferenceUtils.get(SharedPreferencesKeys.ISDEBUG, false);
        if (isDebuggingEnv) {
            btn_dev_pz.setText("测试环境");
        } else {
            btn_dev_pz.setText("正式环境");
        }

    }


//    private MyOnToggleChanged onToggleChanged = new MyOnToggleChanged(this);
//
//    private static class MyOnToggleChanged implements ToggleButton.OnToggleChanged {
//
//        private MyApp mApp;
//        private MainActivity mActivity;
//        private SoftReference<MainActivity> activity = null;
//
//        MyOnToggleChanged(MainActivity activity) {
//            this.activity = new SoftReference<>(activity);
//            mActivity = this.activity.get();
//            mApp = ((MyApp) mActivity.getApplication());
//        }
//
//        @Override
//        public void onToggle(ToggleButton view, boolean on) {
//            String toggle = on ? "开" : "关";
//            if (activity == null) return;
//
//            switch (view.getId()) {
//                case R.id.tb_child_lock:
//                    break;
//                case R.id.tb_led:
//                    break;
//                case R.id.tb_ele_heat:
//                    break;
//                case R.id.tb_power:
//                    break;
//            }
//        }
//    }


    /**
     * 接收到云端发送来的数据
     */
    public static class MyOnRecvDataListener {

        public static String returnStu;
        private static MainActivity mainActivity;
        SoftReference<MainActivity> activity = null;

        MyOnRecvDataListener(MainActivity mainActivity) {
            activity = new SoftReference<>(mainActivity);
            MyOnRecvDataListener.mainActivity = activity.get();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public static void onRecvData(String data) {
            Log.e(TAG, data);

            String[] split = data.split("\\|");
            for (String s : split) {
                Log.e(TAG, s);
            }
            Integer val;
            Log.e(TAG, Arrays.toString(split));
            switch (split[0]) {
                case "&7":  // 初始化 时间校验
                    Log.e(TAG, "&7 ---");
                    returnStu = split[3];
                    break;

                case "&1": { // 1.开关控制
                    Log.e(TAG, "&1 ---");
                    String s = split[1];
                    val = Integer.valueOf(s);
                    if (val == 0) {
                        mainActivity.tb_power.setToggleOff();
                    } else {
                        mainActivity.tb_power.setToggleOn();
                    }
                }
                break;
                case "&2": { // 2.童锁设置
                    Log.e(TAG, "&2 ---");
                    String s = split[1];
                    val = Integer.valueOf(s);
                    if (val == 0) {
                        mainActivity.tb_child_lock.setToggleOff();
                    } else {
                        mainActivity.tb_child_lock.setToggleOn();
                    }
                }
                break;
                case "&3": { // 3.自动/手动 控制
                    Log.e(TAG, "&3 ---");
                    String s = split[1];
                    val = Integer.valueOf(s);
                    if (val == 0) {

                        String mode = split[2];
                        val = Integer.valueOf(mode);
                        mainActivity.rb_mode_manual.setChecked(true);

                        for (Map.Entry<Integer, Integer> entry : mainActivity.mWindDataMap.entrySet()) {
                            if (Integer.compare(entry.getValue(), val) == 0) {
                                ((RadioButton) mainActivity.findViewById(entry.getKey())).setChecked(true);
                            }
                        }
                    } else {
                        mainActivity.rb_mode_freedom.setChecked(true);
                    }
                }
                break;
                case "&4": {// 4.档位变更
                    Log.e(TAG, "&4 ---");
                    String s = split[1];
                    val = Integer.valueOf(s);
                    for (Map.Entry<Integer, Integer> entry : mainActivity.mWindDataMap.entrySet()) {
                        if (Integer.compare(entry.getValue(), val) == 0) {
                            ((RadioButton) mainActivity.findViewById(entry.getKey())).setChecked(true);
                        }
                    }
                }
                break;
                case "&5": {// 5.自动开关机时间设置
                    Log.e(TAG, "&5 ---");
                    String s = split[1];
                    val = Integer.valueOf(s);
                    ToastUtils.showShort(mainActivity, "星期" + val + " " + split[2]);
                }
                break;
                case "&6": {// 6.滤芯寿命重置
                    Log.e(TAG, "&6 ---");
                    String s = split[1];
                    val = Integer.valueOf(s);
                    sys_surplus bean = new sys_surplus();
                    switch (val) {
                        case 1:
                            bean.top = null;
                            bean.middle = null;
                            bean.bottom = 0;
                            break;
                        case 2:
                            bean.top = null;
                            bean.middle = 0;
                            bean.bottom = null;
                            break;
                        case 3:
                            bean.top = 0;
                            bean.middle = null;
                            bean.bottom = null;
                            break;
                    }
                    EventBus.getDefault().post(bean);

                }
                break;
                case "&15": {// 7.滤芯寿命读取
                    Log.e(TAG, "&15 ---");
                    String s = split[1];
                    val = Integer.valueOf(s);
                    EventBus.getDefault().post(new sys_read());
                }
                break;
                case "&16": {// 8.滤芯寿命设置
                    Log.e(TAG, "&16 ---");
                    String s = split[1];
                    String[] split1 = s.split("\\^");
                    Integer integer = Integer.valueOf(split1[0]);
                    Integer value = Integer.valueOf(split1[1]);
                    sys_surplus bean = new sys_surplus();
                    switch (integer) {
                        case 1:
                            bean.top = null;
                            bean.middle = null;
                            bean.bottom = value;
                            break;
                        case 2:
                            bean.top = null;
                            bean.middle = value;
                            bean.bottom = null;
                            break;
                        case 3:
                            bean.top = value;
                            bean.middle = null;
                            bean.bottom = null;
                            break;
                    }
                    EventBus.getDefault().post(bean);
                }
                break;
                case "&17": {// 9.LED灯光开关功能
                    Log.e(TAG, "&17 ---");
                    String s = split[1];
                    val = Integer.valueOf(s);
                    if (val == 0) {
                        mainActivity.tb_led.setToggleOff();
                    } else {
                        mainActivity.tb_led.setToggleOn();
                    }
                }
                break;


                case "&21": // 升级通知
                    break;
                case "&22": // 回应升级请求
                    break;
                case "&23": // 升级数据包
                    break;
                case "&24": // 远程重启
                    break;
                case "&25": // 升级结果
                    break;
                default:
                    break;
            }

        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (group.getId()) {
            case R.id.rg_wind: {
            }
            break;
            case R.id.rg_mode: {
            }
            break;
            default:
        }
    }


    @Override
    public void onClick(View v) {
        String user = PreferenceUtils.get(SharedPreferencesKeys.MAC, "60C5A86385B3").toUpperCase();
        switch (v.getId()) {
            case R.id.btn_send_msg:
                mApp.sendData(String.format("&4|%s\r\n", user));
                break;
            case R.id.btn_send_sensor:
                Integer isAuto = mModeDataMap.get(rg_mode.getCheckedRadioButtonId());
                Integer wind = mWindDataMap.get(rg_wind.getCheckedRadioButtonId());
                boolean isChildOn = tb_child_lock.isToggleOn();
                boolean isLedOn = tb_led.isToggleOn();
                boolean isPowerOn = tb_power.isToggleOn();
                //操作码|净化器id|pm25采样|自动或者手动(自动1,手动0)|运行档位|儿童锁|机器开关状态|感光数据|Voc传感器数值|初始化返回的10字节字符串<CR><LF>
                String format = String.format("&2|%s|%s|%d|%d|%d|%d|%d|%d|%s\r\n", user,
                        et_pm2_5.getText().toString(),
                        isAuto,
                        wind,
                        isChildOn ? 1 : 0,
                        isPowerOn ? 1 : 0,
                        1111,
                        3,
                        MyOnRecvDataListener.returnStu);
                mApp.sendData(format);

                break;
            case R.id.btn_send_sys_status:  // 5.机器状态
                //操作码|净化器id|灯光开关状态|睡眠模式|预留2位|预留3位|预留4位|预留5位|初始化返回的10字节字符串<CR><LF>
                boolean toggleOn = tb_led.isToggleOn();
                String toggle = toggleOn ? "1" : "0";
                String format1 = String.format("&6|%s|%s|%s|00|000|0000|00000|%s\r\n", user, toggle, "00", MyOnRecvDataListener.returnStu);
                mApp.sendData(format1);

                break;
            case R.id.btn_setting:
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_set_mac:

                String patternMac = "^[A-F0-9]{2}([A-F0-9]{2}){5}$";
                String mac = et_mac.getText().toString().toUpperCase();
                if (!Pattern.compile(patternMac).matcher(mac).find()) {
                    ToastUtils.showShort(getApplicationContext(), "MAC地址格式错误");
                    return;
                }

                PreferenceUtils.put(SharedPreferencesKeys.MAC, mac);
                ToastUtils.showShort(getApplicationContext(), "重启后生效");
                break;

            case R.id.btn_dev_pz: {
                boolean isDebug = PreferenceUtils.get(SharedPreferencesKeys.ISDEBUG, false);
                if (isDebug) {
                    btn_dev_pz.setText("正式环境");
                    PreferenceUtils.put(SharedPreferencesKeys.ISDEBUG, false);
                } else {
                    btn_dev_pz.setText("测试环境");
                    PreferenceUtils.put(SharedPreferencesKeys.ISDEBUG, true);
                }

                ToastUtils.showShort(getApplicationContext(), "重启后生效");
            }

        }
    }

}
