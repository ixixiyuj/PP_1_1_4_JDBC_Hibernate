package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final static String USERS_TABLE = Util.getDbName() + ".`users`";
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE if not exists " + USERS_TABLE + " (\n" +
                    "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(45) NOT NULL,\n" +
                    "  `lastName` VARCHAR(45) NOT NULL,\n" +
                    "  `age` INT(3) NULL,\n" +
                    "  PRIMARY KEY (`id`));";
            statement.executeUpdate(sql);

        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void dropUsersTable() {
        try (Connection connection = Util.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "drop table if exists " + USERS_TABLE;
            statement.executeUpdate(sql);
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        Connection connection = null;
        try {
            connection = Util.getInstance().getConnection();
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            String sql = "INSERT INTO " + USERS_TABLE +
                    "VALUES (DEFAULT,"
                    + " \"" + name + "\", "
                    + " \"" + lastName + "\", "
                    + age + ")";
            statement.executeUpdate(sql);
            ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            Long id = rs.getLong("LAST_INSERT_ID()");
            new User(name, lastName, age).setId(id);
            connection.commit();
            connection.close();

        } catch (SQLException|ClassNotFoundException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }

            throw new RuntimeException(e);
        }


    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM " + USERS_TABLE +
                    "WHERE id = " + id;
            statement.executeUpdate(sql);
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (Connection connection = Util.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "select * from `pp113`.`users`";
            ResultSet rs = statement.executeQuery(sql);
            User user;
            while (rs.next()) {
                Long id = rs.getLong(1);
                String name = rs.getString(2);
                String lastName = rs.getString(3);
                Byte age = rs.getByte(4);

                user = new User(name, lastName, age);
                user.setId(id);

                result.add(user);
            }
            rs.close();
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "TRUNCATE " + USERS_TABLE;
            statement.executeUpdate(sql);
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
