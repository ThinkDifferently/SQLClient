package com.morozovm.resume;

import java.util.Scanner;

public class SQLClient {

    private static Scanner scanner;
    private static ProductDao productDao;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        productDao = new ProductDao();
        System.out.println("Welcome to MorozSQL client. If you want to change database url or user, please, modify properties file");
        System.out.println("You are connected to the " + productDao.getURL() + " as " + productDao.getUser());
        System.out.println("If you want to terminate program, just enter \"enough\"");
        request();
    }

    public static void request() {
        System.out.println("Please, enter your request: ");
        String request = scanner.nextLine();
        if (validate(request)) {
            if (request.toLowerCase().startsWith("select")) {
                System.out.println(productDao.selectQuery(request));
                request();
            } else if (request.toLowerCase().startsWith("update")) {
                System.out.println(productDao.updateQuery(request));
                System.out.println("Enter \"commit\" if you want to commit changes");
                request();
            } else if (request.toLowerCase().startsWith("insert")) {
                System.out.println(productDao.insertQuery(request));
                System.out.println("Enter \"commit\" if you want to commit changes");
                request();
            } else if (request.toLowerCase().startsWith("delete")) {
                System.out.println(productDao.deleteQuery(request));
                System.out.println("Enter \"commit\" if you want to commit changes");
                request();
            }
        } else {
            System.out.println("Wrong SQL syntax. Please, try again. Enter \"help\" for more information");
            request();
        }
    }

    public static boolean validate(String request) {
        if (request.equalsIgnoreCase("enough")) {
            System.out.println("Thank you for choosing our program! Good bye!");
            System.exit(0);
        }
        if (request.equalsIgnoreCase("commit")) {
            productDao.commit();
            request();
        }
        if (request.equalsIgnoreCase("help")) {
            printHelp();
            request();
        }
        if (!request.endsWith(";")) {
            System.out.println("Please add ; at the end of your query");
            return false;
        }
        String requestValidate = request.toLowerCase();
        if ((requestValidate.startsWith("select") || requestValidate.startsWith("delete"))
                && requestValidate.contains("from")) {
            return true;
        } else if (requestValidate.startsWith("update") && requestValidate.contains("set")) {
            return true;
        } else return requestValidate.startsWith("insert") && requestValidate.contains("into");
    }

    private static void printHelp() {
        System.out.println("******************************");
        System.out.println("This sql client is able to perform the next queries: ");
        System.out.println("SELECT");
        System.out.println("UPDATE");
        System.out.println("INSERT");
        System.out.println("DELETE");
        System.out.println("The case of characters query is not important");
        System.out.println("******************************");
    }

}
