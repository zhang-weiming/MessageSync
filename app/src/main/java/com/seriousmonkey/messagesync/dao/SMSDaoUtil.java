package com.seriousmonkey.messagesync.dao;


import com.seriousmonkey.greendao_db.entity.ShortMessage;

import java.util.Date;
import java.util.List;

public interface SMSDaoUtil {

    public void save(ShortMessage message);
    public void save(List<ShortMessage> messages);

    public void delete(ShortMessage message);
//    public void deleteById(long id);
//    public void deleteById(List<Long> ids);

    public List<ShortMessage> queryAllData();
    public List<ShortMessage> query(int start, int pagesize);
    public ShortMessage queryById(long id);
//    public List<ShortMessage> queryByDesc(String desc);
//    public List<ShortMessage> queryByDateTime(Date date);

    public void update(ShortMessage message);

    public void initData();

}
