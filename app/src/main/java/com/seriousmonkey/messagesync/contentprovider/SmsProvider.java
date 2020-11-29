package com.seriousmonkey.messagesync.contentprovider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.seriousmonkey.greendao_db.entity.ShortMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsProvider {
    final String SMS_URI_ALL = "content://sms/";

    private Context context;

    public SmsProvider() {}

    public SmsProvider(Context context) {
        this.context = context;
    }

    public List<ShortMessage> getAllData() {
        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type",};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, "date desc");
        Cursor cur = context.getContentResolver().query(uri, projection, "read = ?", new String[]{"0"}, "date desc");

        List<ShortMessage> allMessages = new ArrayList<ShortMessage>();
        if (null == cur) {
            Log.i("ooc", "************cur == null");
            return allMessages;
        }

        while (cur.moveToNext()) {
            long id = cur.getLong(cur.getColumnIndex("_id"));
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            long longDate = cur.getLong(cur.getColumnIndex("date"));
            int intType = cur.getInt(cur.getColumnIndex("type"));

            if (intType != 1) {
                continue;
            }

            //至此就获得了短信的相关的内容, 以下是把短信加入map中，构建listview,非必要。
            try {
                ShortMessage message = new ShortMessage();
                message.setId(id);
                message.setAddress(number);
                message.setName(name);
                message.setDesc(body);
                message.setReceiveTime(longDate);
                allMessages.add(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cur.close();

        return allMessages;
    }

    public String getDataByAddress(String address) {

        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type",};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, "date desc");
        Cursor cur = context.getContentResolver().query(uri, projection, "read = ?", new String[]{"0"}, "date desc");
        StringBuilder smsBuilder = new StringBuilder();


        if(cur.moveToFirst()){
            int index_Address = cur.getColumnIndex("address");
            int index_Person = cur.getColumnIndex("person");
            int index_Body = cur.getColumnIndex("body");
            int index_Date = cur.getColumnIndex("date");
            int index_Type = cur.getColumnIndex("type");

            do {
                String strAddress = cur.getString(index_Address);
                int intPerson = cur.getInt(index_Person);
                String strbody = cur.getString(index_Body);
                long longDate = cur.getLong(index_Date);
                int intType = cur.getInt(index_Type);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d = new Date(longDate);
                String strDate = dateFormat.format(d);

                String strType = "";
                if (intType == 1) {
                    strType = "接收";
                } else if (intType == 2) {
                    strType = "发送";
                } else if (intType == 3) {
                    strType = "草稿";
                } else if (intType == 4) {
                    strType = "发件箱";
                } else if (intType == 5) {
                    strType = "发送失败";
                } else if (intType == 6) {
                    strType = "待发送列表";
                } else if (intType == 0) {
                    strType = "所以短信";
                } else {
                    strType = "null";
                }

                if(strAddress.equals(address)){
                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(strDate + ", ");
                    smsBuilder.append(strType);
                    smsBuilder.append(" ]\n\n");
                }

            } while (cur.moveToNext());

            if (!cur.isClosed()) {
                cur.close();
                cur = null;
            }
        }else{
            smsBuilder.append("no result!");
        }


        smsBuilder.append("getSmsInPhone has executed!");

        return smsBuilder.toString();
    }
}
