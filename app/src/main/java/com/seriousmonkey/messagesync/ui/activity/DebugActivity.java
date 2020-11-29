package com.seriousmonkey.messagesync.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.seriousmonkey.greendao_db.entity.ShortMessage;
import com.seriousmonkey.messagesync.R;
import com.seriousmonkey.messagesync.contentprovider.SmsProvider;
import com.seriousmonkey.messagesync.utils.email.EmailSender;
import com.seriousmonkey.messagesync.utils.email.Mail163Sender;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.util.List;

public class DebugActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_short_message).setOnClickListener(this);
        findViewById(R.id.btn_send_email).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_short_message:
                // TODO get latest short messages
                Toast.makeText(DebugActivity.this, "short messages", Toast.LENGTH_SHORT).show();
                List<ShortMessage> messages = new SmsProvider(this).getAllData();
                Log.d(getClass().getName(), "获得新短信数：" + messages.size());
                for (int i = 0; i < Math.min(messages.size(), 3); i++) {
                    Log.d(getClass().getName(), messages.toString());
                }
                break;
            case R.id.btn_send_email:
                // TODO send email
                Toast.makeText(DebugActivity.this, "send email", Toast.LENGTH_SHORT).show();
                sendTestEmail();
                break;
        }
    }

    private void sendTestEmail() {
        //耗时操作要起子线程
        new Thread(() -> {
            try {
                Mail163Sender sender = Mail163Sender.getInstance();
                sender.setReceivers(new String[]{"收件人邮箱地址"})
                        .setTitle("diy title")
                        .setContent("diy content")
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