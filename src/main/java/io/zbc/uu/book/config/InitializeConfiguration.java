package io.zbc.uu.book.config;

import io.zbc.uu.book.dao.IGoodsDao;
import io.zbc.uu.book.dao.IUserDao;
import io.zbc.uu.book.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class InitializeConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private IGoodsDao goodsDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private BookProperties bookProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        createTables();
        initDefaultAuth();
    }

    private void createTables() {
        goodsDao.createGoodsTable();
        userDao.createUserTable();
    }

    @Transactional
    public void initDefaultAuth() {
        User user = buildDefaultUser();
        User existUser = userDao.selectUserByName(bookProperties.getAuth().getUserName());
        if (existUser != null) {
            user.setUserId(existUser.getUserId());
        } else {
            userDao.insertUser(user);
        }
    }

    private User buildDefaultUser() {
        User user = new User();
        user.setUserName(bookProperties.getAuth().getUserName());
        user.setPassword(bookProperties.getAuth().getPassword());
        return user;
    }

}
