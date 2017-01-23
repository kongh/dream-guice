package com.coder.dream.jpa;

import com.coder.dream.guice.persist.jpa.IBaseDao;
import com.google.inject.ImplementedBy;

/**
 * Created by konghang on 2017/1/23.
 */
@ImplementedBy(UserDaoImpl.class)
public interface UserDao extends IBaseDao<Long, User> {
}
