package com.seriousmonkey.messagesync.entity;

import com.seriousmonkey.greendao_db.entity.ShortMessage;
import com.seriousmonkey.messagesync.utils.DateUtil;

public class ShortMessageItem implements Cloneable {

    private int icon;
    private String name;
    private String address;
    private String date;
    private String desc;

    private static ShortMessageItem itemCloneable;

    public ShortMessageItem() {}

    public ShortMessageItem(int icon, String name, String address, String date, String desc) {
        this.icon = icon;
        this.name = name;
        this.address = address;
        this.date = date;
        this.desc = desc;
    }

    public static ShortMessageItem fromShortMessage(ShortMessage message) {
        ShortMessageItem msgItem = null;
        try {
            if (itemCloneable != null) {
                msgItem = (ShortMessageItem) itemCloneable.clone();
            } else {
                msgItem = (itemCloneable = new ShortMessageItem());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } finally {
            if (itemCloneable == null) {
                msgItem = new ShortMessageItem();
            }
        }

        msgItem.setName(message.getName());
        msgItem.setAddress(message.getAddress());
        msgItem.setDate(message.getReceiveTime());
        msgItem.setDesc(message.getDesc());
        return msgItem;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setDate(long longDate) {
        if (DateUtil.isToday(longDate)) {
            this.date = DateUtil.parseLongToTimeStr(longDate);
        }
        else {
            this.date = DateUtil.parseLongToDayStr(longDate);
        }
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
