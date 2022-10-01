package jm.task.core.jdbc;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;

public class Main {
//    Создание таблицы User(ов)
//    Добавление 4 User(ов) в таблицу с данными на свой выбор. После каждого добавления должен быть
//    вывод в консоль ( User с именем – name добавлен в базу данных )
//    Получение всех User из базы и вывод в консоль ( должен быть переопределен toString в классе User)
//    Очистка таблицы User(ов)
//    Удаление таблицы

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Util.getInstance().getConnection();
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        for (int i = 0; i < 4; i ++) {
            userService.saveUser("name" + i,
                    "lastName" + i,
                    (byte)((i + 1) * 5));
            System.out.println("User с именем name" + i + " добавлен в базу данных");
        }
        System.out.println();
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();

    }
}
