package managesystem.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AccountDB {

    static String user = "root";
    static String password = "1234";
    static String url = "jdbc:mysql://localhost/account";
    static String driver = "com.mysql.cj.jdbc.Driver";

    public static Connection getCon() {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }

}
