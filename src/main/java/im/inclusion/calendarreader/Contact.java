package im.inclusion.calendarreader;


import java.util.Set;
import java.util.TreeSet;

class Contact {
    String name;
    Set<String> emails;
    Set<String> phones;

    public Contact(String name) {
        this.name = name;
        this.emails = new TreeSet<String>();
        this.phones = new TreeSet<String>();
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void addEmail(String email) {
        this.emails.add(email);
    }

    public Set<String> getPhones() {
        return phones;
    }

    public void addPhone(String phone) {
        this.phones.add(phone);
    }

    public boolean isBerkeley() {
        for (String email : emails) {
            if (email.endsWith(".berkeley.edu")) return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name + " " + emails + " " + phones;
    }
}
