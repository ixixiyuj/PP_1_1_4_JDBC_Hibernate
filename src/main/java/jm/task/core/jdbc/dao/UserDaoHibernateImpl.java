package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {


    }



    @Override
    public void createUsersTable() {
    try (Session session = Util.getSessionFactory().openSession()) {
        String sql = new StringBuilder().append("CREATE TABLE IF NOT EXISTS users ")
                .append("(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, ")
                .append("name VARCHAR(50) NOT NULL, lastName VARCHAR(50) NOT NULL, ")
                .append("age TINYINT NOT NULL)").toString();;
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.createSQLQuery(sql)
                .addEntity(User.class)
                .executeUpdate();
        transaction.commit();

    }

    }

    @Override
    public void dropUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery("DROP TABLE IF EXISTS users")
                .addEntity(User.class)
                .executeUpdate();
        transaction.commit();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        SessionFactory sf = Util.getSessionFactory();
        Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();

        User user = new User(name, lastName, age);
        session.save(user);
        transaction.commit();
        session.close();

    }

    @Override
    public void removeUserById(long id) {
        SessionFactory sf = Util.getSessionFactory();
        Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(session.get(User.class, id));
        transaction.commit();
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        SessionFactory sf = Util.getSessionFactory();
        Session session = sf.openSession();
        List<User> users = session.createQuery("FROM User", User.class).list();
        session.close();
        return users;
    }


    @Override
    public void cleanUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery("TRUNCATE users")
                .addEntity(User.class)
                .executeUpdate();
        transaction.commit();
        session.close();

    }
}
