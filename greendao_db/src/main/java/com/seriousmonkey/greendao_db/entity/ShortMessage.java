package com.seriousmonkey.greendao_db.entity;

import android.text.format.DateUtils;
import com.seriousmonkey.greendao_db.utils.DateUtil;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ShortMessage {

    @Id(autoincrement = true)
    private Long _id;

    // 手机系统赋予短信的id
    private Long id;

    // 手机号
    private String address;

    // 姓名
    private String name;

    // 内容
    private String desc;

    // 收到时间
    private long receiveTime;

    // 是否上传
    private boolean isUploaded;

    // 最后操作时间
    private long lastUpdateTime;

    @Generated(hash = 882678025)
    public ShortMessage(Long _id, Long id, String address, String name, String desc,
            long receiveTime, boolean isUploaded, long lastUpdateTime) {
        this._id = _id;
        this.id = id;
        this.address = address;
        this.name = name;
        this.desc = desc;
        this.receiveTime = receiveTime;
        this.isUploaded = isUploaded;
        this.lastUpdateTime = lastUpdateTime;
    }

    @Generated(hash = 1222660184)
    public ShortMessage() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public boolean getIsUploaded() {
        return this.isUploaded;
    }

    public void setIsUploaded(boolean isUploaded) {
        this.isUploaded = isUploaded;
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String makeEmailContent() {
        return "address='" + address + '\'' +
                ", <br/>name='" + name + '\'' +
                ", <br/>desc='" + desc + '\'' +
                ", <br/>receiveTime=" + DateUtil.parseLongToLongStr(receiveTime);
    }

    @Override
    public String toString() {
        return "ShortMessage {" +
                "\nid='" + id + '\'' +
                ", \naddress='" + address + '\'' +
                ", \nname='" + name + '\'' +
                ", \ndesc='" + desc + '\'' +
                ", \nreceiveTime=" + receiveTime +
                "\n}";
    }
}
