package com.seriousmonkey.messagesync.utils.email;

import android.util.Log;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class Mail163Sender {
    private static volatile Mail163Sender emailSender;

    public static Mail163Sender getInstance() {
        if (emailSender == null) {
            synchronized (Mail163Sender.class) {
                if (emailSender == null) {
                    emailSender = new Mail163Sender();

                }
            }
        }
        return emailSender;
    }

    private EmailSender sender;
    private String[] receivers;
    private String title;
    private String content;

    private Mail163Sender() {
        sender = new EmailSender();
    }

    public String[] getReceivers() {
        return receivers;
    }

    public Mail163Sender setReceivers(String[] receivers) {
        this.receivers = receivers;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Mail163Sender setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Mail163Sender setContent(String content) {
        this.content = content;
        return this;
    }

    public void send() throws AddressException, MessagingException {
//        try {
            //设置服务器地址和端口，以163为例
            sender.setProperties("smtp.163.com", "25");
            //分别设置发件人，邮件标题和文本内容
            sender.setMessage("发件人邮箱", title, content);
            //设置收件人
            sender.setReceiver(receivers);
            //添加附件换成你手机里正确的路径
            // sender.addAttachment("/sdcard/emil/emil.txt");
            //发送邮件
            sender.sendEmail("smtp.163.com", "发件人邮箱", "邮箱密码");
        Log.d(getClass().getName(), "send email");
    }
}
