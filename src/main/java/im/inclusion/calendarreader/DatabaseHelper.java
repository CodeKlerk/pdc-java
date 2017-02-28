package im.inclusion.calendarreader;

import java.sql.*;

public class DatabaseHelper {

    public void FetchNSendSMS() throws ClassNotFoundException, SQLException {

        // this will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // setup the connection with the DB.
        Connection connect = DriverManager
                .getConnection("jdbc:mysql://localhost/periodontal?"
                        + "user=root&password=strongpass");
        // statements allow to issue SQL queries to the database
        Statement statement = connect.createStatement();
        // resultSet gets the result of the SQL query
        ResultSet resultSet = statement
                .executeQuery("SELECT s1.smsid,s1.body, s1.recepientscount, s1.sender,"
                        + "s1.recepients, s1.sendscheduletime "
                        + "FROM smslog s1  where "
                        + "s1.sendscheduletime < DATE_ADD(now(), INTERVAL 5 MINUTE) "
                        + "and s1.sentstatus = 'false'");
        // PrintStream recepients = null;
        System.out.println("1 --" + " sender " + "smsid " + "recepient ");
        while (resultSet.next()) {

            String recepient = "+"
                    + resultSet.getString("recepients").replaceAll("\\s+",
                    "");
            String message = resultSet.getString("body");

            System.out.println("message " + message);
            System.out.println("to recepient ->" + recepient);

            SendSMS sendsms = new SendSMS();
            sendsms.forwardSMS(recepient, message, "","");
        }
        connect.close();

    }

    public void runQueryStr(String queryStr) throws ClassNotFoundException, SQLException {
        // this will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // setup the connection with the DB.
        Connection connect = DriverManager
                .getConnection("jdbc:mysql://localhost/periodontal?"
                        + "user=root&password=");
        // statements allow to issue SQL queries to the database
        Statement statement = connect.createStatement();
        // resultSet gets the result of the SQL query
        int updresult = statement.executeUpdate(queryStr);
        // PrintStream recepients = null;

        connect.close();

    }
}