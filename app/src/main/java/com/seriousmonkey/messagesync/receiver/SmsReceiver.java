package com.seriousmonkey.messagesync.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.seriousmonkey.greendao_db.entity.ShortMessage;
import com.seriousmonkey.messagesync.dao.SMSDaoUtilImpl;
import com.seriousmonkey.messagesync.utils.email.Mail163Sender;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsReceiver";

    public SmsReceiver() {
        Log.d(TAG, "new SmsReceiver");
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "onReceive 收到了");
        Cursor cursor = null;
        try {
            if ("hhh_action".equals(intent.getAction())) {
                Log.d("hhh_action", "收到了");
            }

            if (SMS_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "sms received!");
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    if (messages.length > 0) {
                        String content = messages[0].getMessageBody();
                        String sender = messages[0].getOriginatingAddress();
                        long msgDate = messages[0].getTimestampMillis();
//                        String smsToast = "New SMS received from : "
//                                + sender + "\n'"
//                                + content + "'";
                        Log.d(TAG, "message from: " + sender + ", message body: " + content
                                + ", message date: " + msgDate);
                        //自己的逻辑
                        ShortMessage shortMessage = new ShortMessage();
                        shortMessage.setAddress(sender);
                        shortMessage.setDesc(content);
                        shortMessage.setReceiveTime(msgDate);
                        SMSDaoUtilImpl.getInstance().save(shortMessage);
                        sendMessageInEmail("184****7125 新短信", shortMessage.makeEmailContent());
                        Log.d(getClass().getName(), "new msg: " + shortMessage.toString());
                    }
                }
                cursor = context.getContentResolver().query(
                        Uri.parse("content://sms"),
                        new String[] { "_id", "address", "read", "body", "date" },
                        "read = ? ", new String[] { "0" },
                        "date desc");
                if (null == cursor){
                    return;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception : " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
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