package im.inclusion.calendarreader;

/**
 * Created by Inclusion on 12/9/2015.
 */


public class SMS {

    private String from;
    private String to;
    public String text;
    public String sendAt;


    public SMS(String from, String to, String text, String sendatt) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.sendAt = sendatt;
    }


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSendAt() {
        return sendAt;
    }

    public void setSendAt(String sendat) {
        this.sendAt = sendat;

    }

}
