package im.inclusion.calendarreader;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.util.ServiceException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class ContactsReader {
    private static ContactsService contactService;

    /**
     * @param myService
     * @throws com.google.gdata.client.authn.oauth.OAuthException
     * @throws IOException
     * @throws ServiceException
     */


    private static final String CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/default/full";
    private static final int MAX_NB_CONTACTS = 1000;

    public static void printAllContacts(ContactsService smyService) throws ServiceException, IOException, GeneralSecurityException {

        File p12 = new File("./key.p12");

        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        String[] SCOPESArray = {"https://www.google.com/m8/feeds/contacts/default/full"};

        final List SCOPES = Arrays.asList(SCOPESArray);
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId("833214973151-7njrenhiete04g7ort28cjaq0lea4edi.apps.googleusercontent.com")
                .setServiceAccountScopes(SCOPES)
                .setServiceAccountPrivateKeyFromP12File(p12)
                .build();

//        Credential credential = CalendarConnection.authorize();
        ContactsService myService = new ContactsService("account-1@abstract-key-106311.iam.gserviceaccount.com");
//        System.out.println("[" + credential.getAccessToken() + "]");
        myService.setOAuth2Credentials(credential);
//        myService.setUserCredentials("account-1@abstract-key-106311.iam.gserviceaccount.com", "notasecret");
        myService.setUserToken(credential.getAccessToken());
        myService.setHeader("Authorization", "Bearer " + credential.getAccessToken());
        myService.getRequestFactory().setHeader("PerioDontist", CalendarConnection.APPLICATION_NAME);

        // Create query and submit a request
        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
        Query myQuery = new Query(feedUrl);
//        myQuery.setUpdatedMin(startTime);
        ContactFeed resultFeed = myService.query(myQuery, ContactFeed.class);
        // Print the results
        for (ContactEntry entry : resultFeed.getEntries()) {
            System.out.println(entry.getName());
            System.out.println("Updated on: " + entry.getUpdated().toStringRfc822());
        }

    }
}
