package ecs189.querying.github;

import ecs189.querying.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vincent on 10/1/2017.
 */
public class GithubQuerier {

    private static final String BASE_URL = "https://api.github.com/users/";

    public static String eventsAsHTML(String user) throws IOException, ParseException {
        List<JSONObject> response = getEvents(user);
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        for (int i = 0; i < response.size(); i++) {
            JSONObject event = response.get(i);
            String type = event.getString("type");
            // Get created_at date, and format it in a more pleasant style
            String creationDate = event.getString("created_at");
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
            Date date = inFormat.parse(creationDate);
            String formatted = outFormat.format(date);
            JSONObject payloadarr = event.getJSONObject("payload");
            JSONArray commits = payloadarr.getJSONArray("commits");

            // Add type of event as header
            sb.append("<h3 class=\"type\">");
            sb.append(type);
            sb.append("</h3>");
            //Add Sha

            // Add formatted date
            sb.append(" on ");
            sb.append(formatted);
            sb.append("<br />");
            //sb.append("<br />");


            for(int j = 0; j < commits.length(); j++) {
                JSONObject comobj = commits.getJSONObject(j);
                JSONObject author = comobj.getJSONObject("author");
                String name = author.getString("name"); // getting the name which is at index 1
                String sha = comobj.getString("sha");
                sha = sha.substring(0, 8);
                String message = comobj.getString("message");
                //System.out.println(sha);
                // String sha = commits(j).get("sha");
                sb.append("<h4 class=\"commitnum\">");
                sb.append("Commit #");
                sb.append(j+1);
                sb.append("</h4>");

                //sb.append("<br />");
                sb.append("    >" );
                sb.append("<b> Author: </b>" );
                sb.append(name); sb.append("<br />");
               // sb.append("<br />")
                sb.append("    > ");
                sb.append("<b> SHA : </b>");
                sb.append(sha);
                sb.append("<br />");
                sb.append("    > ");
                sb.append("<b> Message : </b>");
                sb.append(message);
                sb.append("<br />");
                sb.append("<br />");

               // System.out.println(sha);
                //System.out.println(message);
            }
            // Add collapsible JSON textbox (don't worry about this for the homework; it's just a nice CSS thing I like)
            sb.append("<a data-toggle=\"collapse\" href=\"#event-" + i + "\">JSON</a>");
            sb.append("<div id=event-" + i + " class=\"collapse\" style=\"height: auto;\"> <pre>");
            sb.append(event.toString());
            sb.append("</pre> </div>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private static List<JSONObject> getEvents(String user) throws IOException {
        List<JSONObject> eventList = new ArrayList<JSONObject>();
        String url = BASE_URL + user + "/events";
        System.out.println(url);
        JSONObject json = Util.queryAPI(new URL(url));
        System.out.println(json);
        JSONArray events = json.getJSONArray("root");

        for (int i = 0; i < events.length() && i < 10; i++) {
               if ((events.getJSONObject(i).get("type")).equals("PushEvent")) {
               // JSONObject payloadarr = events.getJSONObject(i);
                //System.out.println(payloadarr);
               /* JSONObject payloadfinal = (JSONObject) events.getJSONObject(i).get("payload");
                JSONArray commitsarr = (JSONArray) payloadfinal.get("commits");
                System.out.println("payload array final");
                System.out.println(payloadfinal);
                System.out.println("commits array final");
                System.out.println(commitsarr);*/
                 eventList.add(events.getJSONObject(i));
            }
                //}


               //eventList.add(events.getJSONObject(i));
        }
        return eventList;
    }
}