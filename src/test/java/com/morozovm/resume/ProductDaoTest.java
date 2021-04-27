package com.morozovm.resume;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ProductDaoTest {

    static ProductDao productDao;

    @BeforeClass
    public static void init() {
        try {
            String url = "jdbc:h2:mem:test";
            String user = "user";
            String password = "";
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE  table if not exists testTable " +
                    "(id int, name VARCHAR(255), price int)");
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement("insert into testTable values (1, 'one', 100), " +
                    "(2, 'two', 200)");
            preparedStatement.execute();
            productDao = new ProductDao(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelect() {
        String result = productDao.selectQuery("Select name from testTable where id = 1");
        assertEquals(result, "NAME=one " + System.lineSeparator());
        String result2 = productDao.selectQuery("Select price from testTable where name = 'two'");
        assertEquals(result2, "PRICE=200 " + System.lineSeparator());
        String result3 = productDao.selectQuery("Select * from testTable where id = 1");
        String expected = "ID=1 NAME=one PRICE=100 " + System.lineSeparator();
        assertEquals(result3, expected);
    }

    @Test
    public void testInsert() {
        String result = productDao.insertQuery("insert into testTable values (10, 'first', 200)");
        assertEquals(result, "1 rows inserted" + System.lineSeparator());
        String result2 = productDao.insertQuery("insert into testTable values (11, 'second', 220), (12, 'third', 100)");
        assertEquals(result2, "2 rows inserted" + System.lineSeparator());
    }

    @Test
    public void testDelete() {
        productDao.insertQuery("insert into testTable values (100, 'first', 2000)");
        String result = productDao.deleteQuery("delete from testTable where id = 100");
        assertEquals(result, "1 rows deleted" + System.lineSeparator());
        productDao.insertQuery("insert into testTable values (101, 'second', 2200), (102, 'third', 3100)");
        String result2 = productDao.deleteQuery("delete from testTable where price > 2000");
        assertEquals(result2, "2 rows deleted" + System.lineSeparator());
        String result3 = productDao.deleteQuery("delete from testTable where price > 999999999");
        assertEquals(result3, "0 rows deleted" + System.lineSeparator());
    }

    @Test
    public void testUpdate() {
        productDao.insertQuery("insert into testTable values (30001, 'update', 700)");
        productDao.insertQuery("insert into testTable values (30002, 'update2', 800)");
        String result = productDao.updateQuery("update testTable SET price = price + 1 where id > 30000");
        assertEquals("2 rows updated" + System.lineSeparator(), result);
        String result2 = productDao.selectQuery("select price from testTable where id = 30001");
        assertEquals("PRICE=701 " + System.lineSeparator(), result2);
    }

}