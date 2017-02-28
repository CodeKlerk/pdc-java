package im.inclusion.calendarreader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CalendarConnection {
    /**
     * Application name.
     */
    public static final String APPLICATION_NAME = "Inclusion  - calendar project";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"),
            ".credentials/calendar-api-quickstart");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory
            .getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     */
    private static final List<String> SCOPES = Arrays
            .asList(CalendarScopes.CALENDAR, "https://www.google.com/m8/feeds");

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static ContactsService contactsService;

    static {
        try {
            contactsService = getContactService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = CalendarConnection.class
                .getResourceAsStream("/client_secret.json");
        System.out.println(CalendarConnection.class);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow,
                new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to "
                + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     *
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static com.google.api.services.calendar.Calendar getCalendarService()
            throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
                APPLICATION_NAME).build();
    }

    public static ContactsService getContactService() throws IOException {
        Credential credential = CalendarConnection.authorize();
        ContactsService contactsService = new ContactsService("Periodintists Contact Service");

        contactsService.setOAuth2Credentials(credential);

        contactsService.setHeader("Authorization", "Bearer " + credential.getAccessToken());

        return contactsService;
    }


    //    public static void main(String[] args) throws IOException, ServiceException, GeneralSecurityException {
    public static void fetchCalendarEvents(String datesince) throws IOException, ServiceException, GeneralSecurityException, SQLException, ClassNotFoundException {
//        EventDescriptionParser edp = new EventDescriptionParser();
/*
        String msg = "Pt Stephen Mwaura  00262 - ROS 18 done";
        edp.setEventDescriptionText(msg);
        edp.edtest(msg);
        System.exit(0);

        */


  /*
        CalendarEvent ce = new CalendarEvent();
        JsonParser jp = new JsonParser();
        try {
            jp.test();
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/

//        cr.printAllContacts(contactsService);
//        System.exit(0);

        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        // com.google.api.services.calendar.model.Calendar class.
        com.google.api.services.calendar.Calendar service = getCalendarService();

// Initialize Calendar service with valid OAuth credentials
//        Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials).setApplicationName("applicationName").build();
// Iterate through entries in calendar list
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `tbl_events` (`g_eventid`, `g_eventdescription`, `g_eventlocation`, `g_eventstatus`, `g_eventstart`, `g_eventend`, `g_calendar`) VALUES ");

        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
            System.out.println(calendarListEntry.getSummary());

            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
//            System.out.println(now + " time now");

            DateTime then = new DateTime(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
//            System.out.println(then + " time then");

//            System.exit(0);
//            System.out.println(calendarListEntry.getId() + "calendar id");

//                DateTime sixMonthsAgo = new DateTime("2013-01-01T23:22:18.726+03:00");
//            DateTime sixMonthsAgo = new DateTime("2012-07-11T03:30:00-06:00");


//            DateTime then = new DateTime("2015-04-22T00:00:00-03:00");
            DateTime minTime = new DateTime(datesince);


            Events events = service.events().list(calendarListEntry.getId()).setMaxResults(5000)
                    .setTimeMin(minTime).setOrderBy("startTime").setSingleEvents(true)
                    .execute();


            List<Event> eventitems = events.getItems();

            if (eventitems.size() == 0) {
                System.out.println("No events found.");
            } else {
                DatabaseHelper db = new DatabaseHelper();

//                System.out.println("Upcoming events");
                for (Event event : eventitems) {
                    DateTime start = event.getStart().getDateTime();

                    if (start == null) {
                        start = event.getStart().getDate();
                    }

                    String eventid = event.getId();
                    String eventdescription = event.getSummary();
                    String eventlocation = event.getLocation();
                    String eventstatus = event.getStatus();
                    EventDateTime eventstart = event.getStart();
                    EventDateTime eventend = event.getEnd();
                    // Event.Source eventsource = event.getSource();

                    if (eventdescription == null) {
                    } else {
                        if (eventdescription.indexOf("'") > 0) {
                            eventdescription = eventdescription.replaceAll("'", "''");
                        }
                    }
//                        System.out.println("event description text " + eventdescription);

//                        System.exit(0);

//                    if (calendarListEntry.getId() == "theperiodontistdc@gmail.com") {
                    if (eventstart.getDateTime() != null && eventlocation != null) {
                        sb.append("('" + eventid + "', '" + eventdescription.replace("'", "/'") + "', '" + eventlocation + "', '"
                                + eventstatus + "', '" + eventstart.getDateTime().toString().replace("T", " ").substring(0, eventstart.getDateTime().toString().length() - 10) + "', '"
                                + eventend.getDateTime().toString().replace("T", " ").substring(0, eventend.getDateTime().toString().length() - 10) + "', 'theperiodontistdc@gmail.com')" + ",");
                    }
                        /*System.out.printf("%s (%s)\n", event.getSummary(), start);
                        System.out.println("eventid -->" + eventid
                                        + "eventdescription -->" + eventdescription
                                        + "eventlocation --> " + eventlocation
                                        + "eventstatus --> " + eventstatus
                                        + "eventstarttime -->" + eventstart.getDateTime()
                                        + "eventend --> " + eventend.getDateTime()
                                        + calendarListEntry.getId()
                                // + "eventsource title --> " + eventsource.getTitle()

                        );
                        */
                }
            }
        }        pageToken = calendarList.getNextPageToken();

        }

        while (pageToken != null);
        String str = sb.toString().substring(0, sb.toString().length() - 1);

//        System.out.print(str);
        DatabaseHelper db = new DatabaseHelper();
//        db.runQueryStr(sb.toString() + ";");
//        System.out.println(str.length());

        if (str.length() > 147) {
            PeriodontistService.runQueryStr(str + ";");
        }

//        CalendarEvent ce = new CalendarEvent();
//    ce.CreateEvent(service);
    }


}