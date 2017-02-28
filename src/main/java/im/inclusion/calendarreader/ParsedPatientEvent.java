package im.inclusion.calendarreader;

/**
 * Created by Inclusion on 11/23/2015.
 */
public class ParsedPatientEvent {
    String patientname;
    int patientnumber;
    String patientProcedure;
    String eventid;

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public int getPatientnumber() {
        return patientnumber;
    }

    public void setPatientnumber(int patientnumber) {
        this.patientnumber = patientnumber;
    }

    public String getPatientProcedure() {
        return patientProcedure;
    }

    public void setPatientProcedure(String patientProcedure) {
        this.patientProcedure = patientProcedure;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    String eventDate;


    public ParsedPatientEvent(String patientname, int patientnumber, String patientProcedure, String eventid, String eventDate) {
    }
}
