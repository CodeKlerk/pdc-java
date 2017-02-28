package im.inclusion.calendarreader;

import com.google.api.services.calendar.Calendar;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.*;

/**
 * Created by Inclusion on 10/16/2015.
 */
public class PeriodontistService {

    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException, InterruptedException, GeneralSecurityException, ServiceException {
        // get events daily at least once {yesterday backwards}

        // store the events into a mysql db
//        check 6 months due patients & create event

//        check daily sms alerts for today & tomorrow...Schedule texts on api
//        booking algorithm


//        check google calendar
//        load all events to tbl_events
//        parse events for patients data.

//        update patient 's last seen data

//        schedule next 6 days birthdays
//        check tbm_events for last seen over 6 months ago


//        SendSMS sm = new SendSMS();

//        SendSMS.forwardSMS("SoftwareDyn", "254731300909", "2016 messageThis is a drill sms", "2016-12-19T09:57:00.000+03:00");
        do {
            Calendar cs = CalendarConnection.getCalendarService();

            CalendarConnection cc = new CalendarConnection();
            try {
                parsePatientEvents();

                fetchnSendTodays();
                fetchnSendTomorrows();
//                fetchnSend6months();
                System.exit(0);


                runQueryStr("DELETE FROM  `tbl_events` WHERE g_eventstart > CURDATE( )--  + INTERVAL 1 DAY --  and g_eventstart < curdate() + interval 2 day; ");

                CalendarConnection.fetchCalendarEvents(getLastEventDate());
                parsePatientEvents();


            } catch (ServiceException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }


//            fetchnSend6months();
// should send after like 2 weeks

            System.out.println("very true");
//            System.exit(0);

            Thread.sleep(230000);
        } while (true);

//        System.exit(0);

    }

    private static void fetchnSend6months() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");
        // setup the connection with the DB.
        Connection connect = DriverManager
                .getConnection("jdbc:mysql://localhost/periodontal?"
                        + "user=root&password=");
        // statements allow to issue SQL queries to the database
        Statement statement = connect.createStatement();
        StringBuilder datestring = new StringBuilder();
//        String sql = "SELECT * FROM tbl_patients where last_seen < DATE_SUB(now(), INTERVAL 6 MONTH) ORDER BY `tbl_patients`.`last_seen`  DESC";
//        String sql = "SELECT * FROM tbl_patients where last_seen < DATE_SUB(now(), INTERVAL 6 MONTH) AND last_seen > '2016-03-01' ORDER BY last_seen  DESC";
//        String sql = "SELECT * FROM tbl_patients where last_seen < DATE_SUB(now(), INTERVAL 6 MONTH) AND last_seen > '2016-04-03' ORDER BY last_seen  DESC";
//        String sql = "SELECT * FROM tbl_patients where last_seen < DATE_SUB(now(), INTERVAL 6 MONTH) AND last_seen > '2016-05-01' ORDER BY last_seen  DESC";
        String sql = "SELECT * FROM tbl_patients where last_seen < DATE_SUB(now(), INTERVAL 6 MONTH) AND last_seen > '2016-05-18' ORDER BY last_seen  DESC";

//2016-05-18
//        2016-07-26
//        6 months begin 2016-04-03
//        String sql = "SELECT * FROM `tbl_patients` WHERE `last_seen` > '2016-01-01' AND `last_seen` < '2016-03-01'";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
//            String phone = cleanMSISDN(rs.getString("phone"));
            String phone = "254" + rs.getString("phone");
            String msg = "Hi, It's been a while since you had your bi-annual checkup. A 30 minutes checkup is recommended at least once in every 6 months. Please call us on 0716521043 to book your checkup appointment. Regards, The Periodontist Dental Care.";
            System.out.println("number - " + phone + " msg - " + msg);
            SendSMS.forwardSMS("PDC Dental", phone, msg, "");

        }
//        SendSMS.forwardSMS("PDC Dental", "254716521043", "Hi, It's been a while since you had your bi-annual checkup. A 30 minutes checkup is recommended at least once in every 6 months. Please call Faith on 0716521043 to book your checkup appointment. Regards, The Periodontist Dental Care.", "");
        System.exit(0);

        rs.close();
//        return datestring.toString().replace(" ", "T");

    }

    private static void parsePatientEvents() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        // setup the connection with the DB.
        Connection connect = DriverManager
                .getConnection("jdbc:mysql://localhost/periodontal?"
                        + "user=root&password=");
        // statements allow to issue SQL queries to the database
        Statement statement = connect.createStatement();
        // resultSet gets the result of the SQL query
//        String queryStr = "SELECT * FROM tbl_events where	g_eventend > '0000-00-00 00:00:00'";
        String queryStr = "SELECT * FROM tbl_events " +
                "WHERE g_eventend >  '0000-00-00 00:00:00' " +
                "AND g_eventend < NOW( ) + interval 2 day " +
                "AND g_eventend >curdate() - interval 7 day " +
                "AND eventid NOT IN (SELECT eventid FROM tbl_parsed_patient_events )";
//        int updresult = statement.executeUpdate(queryStr);
        statement = connect.createStatement();
        // resultSet gets the result of the SQL query
        ResultSet resultSet = statement.executeQuery(queryStr);
        // PrintStream recepients = null;

        StringBuilder sb = new StringBuilder();
//        System.out.println()
        boolean patienteventinResult = false;
        if (resultSet.next()) {
            sb.append("INSERT INTO `tbl_parsed_patient_events` ( `patientname`, `patientnumber`, `patientprocedure`, `eventid`, `eventdate`) VALUES ");

            while (resultSet.next()) {

                System.out.println(resultSet.getString("g_eventdescription"));

                String eventdescription = resultSet.getString("g_eventdescription");
                String location = resultSet.getString("g_eventlocation");
                String starttime = resultSet.getString("g_eventstart");
                String endtime = resultSet.getString("g_eventend");
                String eventid = resultSet.getString("eventid");

                if (eventdescription.indexOf("Pt ") < 3 && eventdescription.indexOf("Pt ") >= 0) {
                    System.out.println("eventdescription " + eventdescription);

                    EventDescriptionParser ed = new EventDescriptionParser();
                    String resp = ed.edtest(eventdescription.replace("'", "''"), eventid, starttime);
                    if (resp.length() > 1) {
//                System.out.print(resp);
                        sb.append(resp);
                        if (!resultSet.isLast()) {
                            sb.append(", \n ");
                        }
                    }
//                    patienteventinResult = true;
                }
            }

            String str = sb.toString();
//        ;
//        sb.append(";");
            String eventsStr = str.substring(0, sb.toString().length() - 0) + ";";
            System.out.println("eventsStr" + eventsStr);
            if (patienteventinResult) {
                runQueryStr(eventsStr);
            }
        } else {
            return;
        }
        connect.close();
    }

    private static String getLastEventDate() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        // setup the connection with the DB.
        Connection connect = DriverManager
                .getConnection("jdbc:mysql://localhost/periodontal?"
                        + "user=root&password=");
        // statements allow to issue SQL queries to the database
        Statement statement = connect.createStatement();
        StringBuilder datestring = new StringBuilder();
        String sql = "SELECT g_eventstart FROM tbl_events ORDER BY g_eventstart DESC limit 1";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            datestring.append(rs.getString("g_eventstart"));
        }
        rs.close();
        return datestring.toString().replace(" ", "T");
    }


    public static void fetchnSendTomorrows() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        // setup the connection with the DB.
        Connection connect = DriverManager
                .getConnection("jdbc:mysql://localhost/periodontal?"
                        + "user=root&password=");
        // statements allow to issue SQL queries to the database
        Statement statement = connect.createStatement();
        StringBuilder datestring = new StringBuilder();
        String sql = "SELECT * from tbl_parsed_patient_events,tbl_events\n" +
                "where tbl_parsed_patient_events.eventdate > curdate() + interval 1 day\n" +
                " and tbl_parsed_patient_events.eventdate < curdate() + interval 2 day\n" +
                "and tbl_events.eventid = tbl_parsed_patient_events.eventid\n" +
                "ORDER BY tbl_parsed_patient_events.eventdate DESC;";
//                "SELECT * FROM `tbl_events` where g_eventstart >  curdate() + interval 1 day    and g_eventstart < curdate() + interval 2 day ORDER BY `g_eventend` DESC";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            String eventstart = rs.getString("g_eventstart");
            String eventlocation = cleanMSISDN(rs.getString("g_eventlocation"));
            String starttime = eventstart.substring(11, eventstart.length() - 5);
            String msg = "Hope you are fine. Just to remind you of your appointment tomorrow at the periodontist at " + starttime + ". For queries or confirmations kindly call/text 0716521043. Regards, The periodontist dental centre.";
            System.out.println("number - " + eventlocation + "msg - " + msg);
            SendSMS.forwardSMS("PDC Dental", eventlocation, msg, "");
            // SendSMS.forwardSMS("PDCDental", "254731300909", "2016 messageThis is a drill sms", "");

//            System.exit(0);
        }

        rs.close();
//        return datestring.toString().replace(" ", "T");
    }


    public static void fetchnSendTodays() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        // setup the connection with the DB.
        Connection connect = DriverManager
                .getConnection("jdbc:mysql://localhost/periodontal?"
                        + "user=root&password=");
        // statements allow to issue SQL queries to the database
        Statement statement = connect.createStatement();
        StringBuilder datestring = new StringBuilder();
        String sql = " select * from tbl_parsed_patient_events,tbl_events\n" +
                "where tbl_parsed_patient_events.eventdate > curdate()\n" +
                " and tbl_parsed_patient_events.eventdate < curdate() + interval 1 day\n" +
                "and tbl_events.eventid = tbl_parsed_patient_events.eventid\n" +
                "ORDER BY tbl_parsed_patient_events.eventdate DESC;";
//                "SELECT * FROM `tbl_events` where g_eventstart > curdate() and g_eventstart < curdate() + interval 1 day ORDER BY `g_eventend` DESC";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            String eventstart = rs.getString("g_eventstart");
            String eventlocation = cleanMSISDN(rs.getString("g_eventlocation"));
            String starttime = eventstart.substring(11, eventstart.length() - 5);
            String msg = "Hope you are fine. Just to remind you of your appointment today at the periodontist at " + starttime + ". For queries or confirmations kindly call/text 0716521043. Regards, The periodontist dental centre.";
            System.out.println("number - " + eventlocation + "msg - " + msg);
            SendSMS.forwardSMS("PDC Dental", eventlocation, msg, "");
        }

        rs.close();
//        return datestring.toString().replace(" ", "T");
    }

    private static String cleanMSISDN(String MSISDN) {
        String msisdn = MSISDN.replace("+", "");
        msisdn = msisdn.replace(" ", "");
        if (msisdn.indexOf("0") == 0) {
            msisdn = "254" + msisdn.substring(1, msisdn.length());
        }


        return msisdn;
    }


    public static void runQueryStr(String queryStr) throws ClassNotFoundException, SQLException {
        System.out.println("queryStr -> " + queryStr);

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