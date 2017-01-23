package com.coder.dream.jpa;

import com.coder.dream.guice.persist.jpa.BaseDao;
import com.google.inject.Singleton;

/**
 * Created by konghang on 2017/1/23.
 */
@Singleton
public class UserDaoImpl extends BaseDao<Long, User> implements UserDao {

}
