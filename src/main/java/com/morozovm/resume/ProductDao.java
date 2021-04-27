package com.morozovm.resume;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ProductDao {

    private Properties properties;
    private Connection connection;

    public ProductDao() {
        try {
            properties = new Properties();
            properties.load(this.getClass().getClassLoader().getResourceAsStream("prop.properties"));
            String url = properties.getProperty("database.url");
            String user = properties.getProperty("database.user");
            String password = properties.getProperty("database.password");
            this.connection = DriverManager.getConnection(url, user, password);
            this.connection.setAutoCommit(false);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ProductDao(Connection connection) {
        this.connection = connection;
    }

    public ProductDao(String propertiesPath) {
        try {
            properties = new Properties();
            properties.load(this.getClass().getClassLoader().getResourceAsStream(propertiesPath));
            String url = properties.getProperty("database.url");
            String user = properties.getProperty("database.user");
            String password = properties.getProperty("database.password");
            this.connection = DriverManager.getConnection(url, user, password);
            this.connection.setAutoCommit(false);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    public String getURL() {
        return properties.getProperty("database.url");
    }

    public String getUser() {
        return properties.getProperty("database.user");
    }

    public String selectQuery(String request) {
        StringBuilder result = new StringBuilder();
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(request)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                while (resultSet.next()) {
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        result.append(metaData.getColumnName(i) + "=" + resultSet.getString(i) + " ");
                    }
                    result.append(System.lineSeparator());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String updateQuery(String request) {
        StringBuilder result = new StringBuilder();
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(request)) {
            result.append(preparedStatement.executeUpdate()).append(" rows updated");
            result.append(System.lineSeparator());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String insertQuery(String request) {
        StringBuilder result = new StringBuilder();
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(request)) {
            result.append(preparedStatement.executeUpdate()).append(" rows inserted");
            result.append(System.lineSeparator());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String deleteQuery(String request) {
        StringBuilder result = new StringBuilder();
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(request)) {
            result.append(preparedStatement.executeUpdate()).append(" rows deleted");
            result.append(System.lineSeparator());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Changes are committed");
        System.out.println();
    }

}
