package com.seriousmonkey.messagesync.dao;

import android.content.Context;
import android.util.Log;
import com.seriousmonkey.greendao_db.entity.ShortMessage;
import com.seriousmonkey.greendao_db.greendao.DaoSession;
import com.seriousmonkey.greendao_db.greendao.DaoSessionManager;
import com.seriousmonkey.greendao_db.greendao.ShortMessageDao;
import com.seriousmonkey.messagesync.BaseApplication;
import com.seriousmonkey.messagesync.contentprovider.SmsProvider;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 代理模式
 * 一个外部 DbUtils 类，可以持有不同类型的 bean
 * 增删改查动作屏蔽 bean 类型
 */

public class SMSDaoUtilImpl implements SMSDaoUtil {

    private static SMSDaoUtilImpl shortMessageDaoUtil;

    private Context context;

    private SMSDaoUtilImpl() {
        context = BaseApplication.mContext;
    }

    /**
     * Singleton MODE
     * @return
     */
    public static SMSDaoUtilImpl getInstance() {
        if (shortMessageDaoUtil == null) {
            synchronized (SMSDaoUtilImpl.class) {
                if (shortMessageDaoUtil == null) {
                    shortMessageDaoUtil = new SMSDaoUtilImpl();
                }
            }
        }
        return shortMessageDaoUtil;
    }

    private ShortMessageDao getShortMessageDao(DaoSessionManager.IoState ioState) {
        DaoSession daoSession = null;
        switch (ioState) {
            case Readable:
                daoSession = DaoSessionManager.getInstace().getDaoSessionReadable(context);
                break;
            case Writable:
                daoSession = DaoSessionManager.getInstace().getDaoSessionWritable(context);
        }
        return daoSession.getShortMessageDao();
    }

    @Override
    public void save(ShortMessage message) {
//        //增加
//        messageDao.insert(song);
        message.setLastUpdateTime(new Date().getTime());
        getShortMessageDao(DaoSessionManager.IoState.Writable).insert(message);
    }

    @Override
    public void save(List<ShortMessage> messages) {
        for (ShortMessage message : messages) {
            message.setLastUpdateTime(new Date().getTime());
            getShortMessageDao(DaoSessionManager.IoState.Writable).insert(message);
        }
    }

    @Override
    public void delete(ShortMessage message) {
        getShortMessageDao(DaoSessionManager.IoState.Writable).delete(message);
    }

    @Override
    public List<ShortMessage> queryAllData() {
        try {
            ShortMessageDao dao = getShortMessageDao(DaoSessionManager.IoState.Readable);
            QueryBuilder<ShortMessage> qb = dao.queryBuilder();
            //条件： id & （（notice & priid） | friend）
            qb.orderDesc(ShortMessageDao.Properties.Id);
            return qb.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<ShortMessage> query(int start, int pagesize) {
        try {
            initData();
            ShortMessageDao dao = getShortMessageDao(DaoSessionManager.IoState.Readable);
            QueryBuilder<ShortMessage> qb = dao.queryBuilder();
            //条件： id & （（notice & priid） | friend）
            qb.orderDesc(ShortMessageDao.Properties.ReceiveTime);
            return qb.list().subList(start, start + pagesize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ShortMessage queryById(long id) {
        //查
        //Song query = songDao.queryBuilder().where(SongDao.Properties.SingerCode.eq(111)).list().get(0);

        try {
            QueryBuilder<ShortMessage> qb = getShortMessageDao(DaoSessionManager.IoState.Readable).queryBuilder();
            //条件： id & （（notice & priid） | friend）
            ShortMessage query = qb.where(ShortMessageDao.Properties.Id.eq(id)).list().get(0);
            return query;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ShortMessage> queryWhere(WhereCondition condition) {
        try {
            QueryBuilder<ShortMessage> qb = getShortMessageDao(DaoSessionManager.IoState.Readable).queryBuilder();
            //条件： id & （（notice & priid） | friend）
            List<ShortMessage> query = qb.where(condition).list();
            return query;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void update(ShortMessage message) {
        //删
        //songDao.delete(song);
        message.setLastUpdateTime(new Date().getTime());
    }

    /**
     * 获取系统所有短信，保存到本app的数据库保存
     * - 获取已保存短信
     * - 获取系统所有短信
     * - 过滤，保存
     */
    @Override
    public void initData() {
        List<Long> messagesHeld_ids = new ArrayList<>();
        List<ShortMessage> messagesHeld = queryAllData();

        if (messagesHeld != null && messagesHeld.size() > 0) {
            try {
                Log.d("getAllData", messagesHeld.size() + messagesHeld.get(0).getDesc());
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (ShortMessage message : messagesHeld) {
                messagesHeld_ids.add(message.getId());
            }
        }

        List<ShortMessage> messages = new SmsProvider(context).getAllData();

        for (int i = messages.size() - 1; i >= 0; i--) {
            long id = messages.get(i).getId();
            if (messagesHeld_ids.contains(id)) {
                messages.remove(i);
            }
        }
        save(messages);
    }

}
