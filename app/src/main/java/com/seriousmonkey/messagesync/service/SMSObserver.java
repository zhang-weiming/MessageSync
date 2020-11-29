package com.seriousmonkey.messagesync.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import com.seriousmonkey.greendao_db.entity.ShortMessage;
import com.seriousmonkey.messagesync.BaseApplication;
import com.seriousmonkey.messagesync.dao.SMSDaoUtilImpl;
import com.seriousmonkey.messagesync.utils.email.Mail163Sender;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class SMSObserver extends Service {

    private SMSContentObserver SMSContentObserver;

    private Handler handler;

    public SMSObserver() {
        super();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1000:
                        // 收到新短信
                        ShortMessage message = (ShortMessage) msg.obj;
                        SMSDaoUtilImpl.getInstance().save(message);
                        Log.d(getClass().getName(), "new msg: " + message.toString());
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SMSContentObserver = new SMSContentObserver(handler);
        getContentResolver().registerContentObserver(SMSContentObserver.SMS_INBOX, true,
                SMSContentObserver);
        Log.d("Service", "监听开启");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(SMSContentObserver);
        Log.d("Service", "监听关闭");
    }


    static class SMSContentObserver extends ContentObserver {
        public static Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        private Context context;
        private Handler handler;

        long lastTimeofCall = 0L;
        long lastTimeofUpdate = 0L;
        long threshold_time = 500;
        static long lastMsgId = -1;


        public SMSContentObserver(Handler handler) {
            super(handler);
            this.context = BaseApplication.mContext;
            this.handler = handler;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);
            if(uri.toString().equals("content://sms/raw")){
                return;
            }

            //每当有新短信到来时，使用我们获取短消息的方法
//        lastTimeofCall = System.currentTimeMillis();
//        if (lastTimeofCall - lastTimeofUpdate < threshold_time) return;
//        lastTimeofUpdate = lastTimeofCall;

            Log.d(getClass().getName(), "new message");
            getSmsFromPhone();

        }

        public void getSmsFromPhone() {
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[] { "_id", "body", "address", "date", "type" };//"_id", "address", "person",, "date", "type
//        String where = " date > " + (System.currentTimeMillis() - 10 * 60 * 1000);
//        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
            Cursor cur = cr.query(SMS_INBOX, projection, "", null, "date desc");
            if (null == cur) {
                Log.d(getClass().getName(), "cannot get cursor");
                return;
            }
            if (cur.moveToFirst()) {
                long id = cur.getLong(cur.getColumnIndex("_id"));//id
                String number = cur.getString(cur.getColumnIndex("address"));//手机号
                String body = cur.getString(cur.getColumnIndex("body"));
                long longDate = cur.getLong(cur.getColumnIndex("date"));
                int intType = cur.getInt(cur.getColumnIndex("type"));
                cur.close();

                if (intType != 1) {
                    return;
                }

                Log.d(getClass().getName(), "new msg: cur id is " + id +
                        ", with last id is " + lastMsgId);
                if (id != lastMsgId) {
                    ShortMessage shortMessage = new ShortMessage();
                    shortMessage.setId(id);
                    shortMessage.setAddress(number);
                    shortMessage.setDesc(body);
                    shortMessage.setReceiveTime(longDate);

                    Log.d(getClass().getName(), "new msg: " + shortMessage.toString());
                    SMSDaoUtilImpl.getInstance().save(shortMessage);
                    sendMessageInEmail("新短信", shortMessage.toString());

//            Message msg = handler.obtainMessage();
//            msg.what = 1000;
//            msg.obj = shortMessage;
//
//            handler.sendMessage(msg);

                    lastMsgId = id;
                }

            }
        }

        private void sendMessageInEmail(final String title, final String content) {
            new Thread(() -> {
                try {
                    Mail163Sender sender = Mail163Sender.getInstance();
                    sender.setReceivers(new String[]{"收件人邮箱地址"})
                            .setTitle(title)
                            .setContent(content)
                            .send();

                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                Log.d(getClass().getName(), "send email");
            }).start();
        }
    }

}
