package im.inclusion.calendarreader;

/**
 * Created by Inclusion on 10/26/2015.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventDescriptionParser {
    public String eventDescriptionText;

    public int patientNumber;
    public String patientProcedure;
    public String patientName;
    public String title;
    public String patientPhoneNo;
    public boolean patientevent;

    // public void Main(String args[]) {
    public String edtest(String eventDescription, String eventid, String eventDate) {
//        System.out.println(eventDescription);
//        this.setEventDescriptionText(eventDescription);

        // System.out.print(this.getEventDescriptionText());

        // String str = "a d, m, i.n";
//        System.out.println("index of Pt --> " + eventDescription.indexOf("Pt"));


//        System.out.println("Patient Event " + eventDescription);

        Matcher matcher = Pattern.compile("\\d+").matcher(eventDescription);
        if (matcher.find()) {
            int i = Integer.valueOf(matcher.group()); // index of integer
            int patientnumber = Integer.parseInt(matcher.group());
            String patientname = eventDescription.substring(0, eventDescription.indexOf(matcher.group())).replace("Pt ", "");
            String patientProcedure = eventDescription.substring(eventDescription.indexOf(matcher.group())).replace(String.valueOf(i), "");

//            System.out.println("Patient name " + patientname);
//            System.out.println("Patient number " + patientnumber);
//            System.out.println("Patient procedure " + patientProcedure);
//            System.out.println("Event ID" + eventid);
//            System.out.println("Event Date" + eventDate);

            ParsedPatientEvent parsedPatientEvent = new ParsedPatientEvent(patientname, patientnumber, patientProcedure, eventid, eventDate);
            return "('" + patientname + "'," + patientnumber + ",'" + patientProcedure + "','" + eventid + "','" + eventDate + "')";
        }

        return "";
    }

    public void splitEventDescriptionText(String eventDescription) {

    }

    public String getEventDescriptionText() {
        return eventDescriptionText;
    }

    public void setEventDescriptionText(String eventDescriptionText) {
        eventDescriptionText = this.eventDescriptionText;
    }

    public int getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(int patientNumber) {
        patientNumber = this.patientNumber;
    }

    public String getPatientProcedure() {
        return patientProcedure;
    }

    public void setPatientProcedure(String patientProcedure) {
        patientProcedure = this.patientProcedure;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        patientName = this.patientName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = this.title;
    }

    public String getPatientPhoneNo() {
        return patientPhoneNo;
    }

    public void setPatientPhoneNo(String patientPhoneNo) {
        patientPhoneNo = this.patientPhoneNo;
    }

}

