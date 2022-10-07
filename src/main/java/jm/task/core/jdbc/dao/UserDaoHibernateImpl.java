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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users")
                    .addEntity(User.class)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()){
            List<User> users = session.createQuery("FROM User", User.class).list();
            session.close();
            return users;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE from User")
                    //.addEntity(User.class)
                    .executeUpdate();
            transaction.commit();
            session.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
