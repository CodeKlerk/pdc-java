package im.inclusion.calendarreader;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Inclusion on 10/8/2015.
 */
public class CalendarEvent {
    public void createCheckup() {
//        patient
//                patientNumber
//        date
//                checkupDuration

    }


    public void CreateEvent(Calendar service, String summary, String location, String starttime, String endtime) {
        // Refer to the Java quickstart on how to setup the environment:
// https://developers.google.com/google-apps/calendar/quickstart/java
// Change the scope to CalendarScopes.CALENDAR and delete any stored
// credentials.

        Event event = new Event()
                .setSummary(summary)
                .setLocation(location)
                .setDescription(summary);

        DateTime startDateTime = new DateTime(starttime);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Africa/Nairobi");
        event.setStart(start);


        DateTime endDateTime = new DateTime(endtime);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Africa/Nairobi");
        event.setEnd(end);


        String[] recurrence = new String[]{"RRULE:FREQ=YEARLY;COUNT=100"};
        event.setRecurrence(Arrays.asList(recurrence));
/*
        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail("lpage@example.com"),
                new EventAttendee().setEmail("sbrin@example.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

*/
        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";

        try {
            event = service.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Event created: %s\n", event.getHtmlLink());


    }
}
