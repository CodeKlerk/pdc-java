package im.inclusion.calendarreader;

import com.google.api.client.util.Base64;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendSMS {


    private static final String USER_AGENT = "MobiBiashara Client Browser 1.0";


    // HTTP POST request

    public static void forwardSMS(String from, String to, String text, String sendat) {
        String url = "http://107.20.199.106/restapi/sms/1/text/single";

        try {
            URL obj = new URL(url);
            HttpURLConnection httpcon = (HttpURLConnection) obj
                    .openConnection();
            httpcon.setRequestMethod("POST");

            // BASE64Encoder enc = new sun.misc.BASE64Encoder();
//            String username = "muleso01";
//            String password = "y3BH6qeA";

            String username = "ThePeriodontistDentalCentre";
            String password = "pAXsKDof";

            SMS sms = new SMS(from, to, text, sendat);
            Gson gsonsms = new Gson();

            String body = gsonsms.toJson(sms);
            System.out.print(body);


            String authString = username + ":" + password;
//            System.out.println("auth string: " + authString);
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            String encodedAuthorization = new String(authEncBytes);
            System.out.print(encodedAuthorization);      //   System.exit(0);

//            String encodedAuthorization = "bXVsZXNvMDE6eTNCSDZxZUE=";

            httpcon.setRequestProperty("Authorization", "Basic "
                    + encodedAuthorization);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setDoOutput(true);
            httpcon.connect();

            DataOutputStream wr = new DataOutputStream(
                    httpcon.getOutputStream());
            wr.writeBytes(body);
            wr.flush();
            wr.close();

            int responsecode = httpcon.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    httpcon.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result

            System.out.println(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
