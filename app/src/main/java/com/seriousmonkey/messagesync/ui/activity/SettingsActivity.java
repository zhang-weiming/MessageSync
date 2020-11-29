package com.seriousmonkey.messagesync.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.seriousmonkey.messagesync.R;
import com.seriousmonkey.messagesync.receiver.SmsReceiver;
import com.seriousmonkey.messagesync.service.SMSObserver;

import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    static class CheckField {
        public static final String MsgObserver = "MsgObserver";

    }

    private static final String SP_FILE = "Setting";

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch newMsgObserverSwitch;

    private SmsReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        newMsgObserverSwitch = (Switch) findViewById(R.id.new_msg_observer_switch);
        newMsgObserverSwitch.setOnClickListener(this);

        initUIState();

        smsReceiver = new SmsReceiver();
    }

    @Override
    public void onClick(View v) {
        Log.d(getClass().getName(), "点击");
        switch (v.getId()) {
            case R.id.new_msg_observer_switch:
//                Intent newMsgObsvrIntent = new Intent(this, SMSObserver.class);
//                if (newMsgObserverSwitch.isChecked()) {
//                    startService(newMsgObsvrIntent);
//                    Log.d(getClass().getName(), "开启监听");
//                    Toast.makeText(SettingsActivity.this, "开启监听短信接收", Toast.LENGTH_SHORT).show();
//                } else {
//                    stopService(newMsgObsvrIntent);
//                    Log.d(getClass().getName(), "关闭监听");
//                    Toast.makeText(SettingsActivity.this, "关闭监听短信接收", Toast.LENGTH_SHORT).show();
//                }
                if (newMsgObserverSwitch.isChecked()) {
                    IntentFilter filter = new IntentFilter(SmsReceiver.SMS_RECEIVED);
                    filter.setPriority(1000);
                    registerReceiver(smsReceiver, filter);
                    Log.d(getClass().getName(), "开启监听");
                    Toast.makeText(SettingsActivity.this, "开启监听短信接收", Toast.LENGTH_SHORT).show();
                } else {
                    unregisterReceiver(smsReceiver);
                    Log.d(getClass().getName(), "关闭监听");
                    Toast.makeText(SettingsActivity.this, "关闭监听短信接收", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void initUIState() {
        SharedPreferences sp = getSharedPreferences(SP_FILE, Context.MODE_APPEND);
        if (sp.contains(CheckField.MsgObserver)) {
            newMsgObserverSwitch.setChecked(
                sp.getBoolean(CheckField.MsgObserver, false)
                && isServiceWork(this, "com.seriousmonkey.messagesync.service.SMSObserver")
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = getSharedPreferences(SP_FILE, Context.MODE_APPEND);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(CheckField.MsgObserver, newMsgObserverSwitch.isChecked());
        editor.commit();
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    static class SMSListener {
        private static volatile SMSListener smsListener;

        public static SMSListener getInstance() {
            if (smsListener == null) {
                synchronized (SMSListener.class) {
                    if (smsListener == null) {
                        smsListener = new SMSListener();
                    }
                }
            }
            return smsListener;
        }

        public static final int SMS_DB_Observer = 1;
        public static final int SMS_Receiver = 2;

        private Context mContext;
        private Handler mHandler;
        private SMSObserver smsObserver;
        private SmsReceiver smsReceiver;

        private SMSListener() {}
        private SMSListener(Context context, Handler handler) {
            mContext = context;
            mHandler = handler;
        }

        public void setSmsListener(boolean turnOn) {

        }
    }
}
