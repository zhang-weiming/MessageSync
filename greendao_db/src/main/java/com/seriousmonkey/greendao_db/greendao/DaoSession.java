package com.seriousmonkey.greendao_db.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.seriousmonkey.greendao_db.entity.ShortMessage;

import com.seriousmonkey.greendao_db.greendao.ShortMessageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig shortMessageDaoConfig;

    private final ShortMessageDao shortMessageDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        shortMessageDaoConfig = daoConfigMap.get(ShortMessageDao.class).clone();
        shortMessageDaoConfig.initIdentityScope(type);

        shortMessageDao = new ShortMessageDao(shortMessageDaoConfig, this);

        registerDao(ShortMessage.class, shortMessageDao);
    }
    
    public void clear() {
        shortMessageDaoConfig.clearIdentityScope();
    }

    public ShortMessageDao getShortMessageDao() {
        return shortMessageDao;
    }

}
