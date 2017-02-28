package im.inclusion.calendarreader;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
	public static void test() throws JSONException {
        new PeriodontistService();
		String Res = "{\"messages\":[{\"to\":\"254731300909\",\"status\":{\"groupId\":1,\"groupName\":\"PENDING\",\"id\":7,\"name\":\"PENDING_ENROUTE\",\"description\":\"Message sent to next instance\"},\"smsCount\":1,\"messageId\":\"1443131766639058424\"}]}";
		parseResponse(Res);
	}

	public static void parseResponse(String resp) throws JSONException {
		JSONObject response = new JSONObject(resp);
		System.out.println(response.getJSONArray("messages"));

	}
}
