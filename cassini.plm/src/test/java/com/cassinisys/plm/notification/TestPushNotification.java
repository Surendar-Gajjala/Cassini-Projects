package com.cassinisys.plm.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

public class TestPushNotification {

    private static String SERVER_KEY = "AAAA-onN8Ys:APA91bEHhM9k2GpgrYbaGcrbJUhEWJSFEB9IESpD4zlCBsF5DTRkJOi02LbcEmRoUMasbpc_GAU3NoXo-q6jk7P5Lu_uWW6SfTLrNcKTw7Lh1uJuSAMw6nxzc4S3ayzvagrHN-LxY34L";
    private static String DEVICE_TOKEN = "flDODQ8kKXU:APA91bF-FyoordqxJPz9uABwJpTXj-cplkGN5KCs6qjaUBYwWID4sLHBIu3fcARk8VPRkXkt9moElJzky6WDRJscWYhMO61F5h2xDLpEOS7WUMRCX8CMFuM6H20tMjpqwub2jo4OoGvY";

    @Test
    public void testPushNotification() throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
        post.setHeader("Content-type", "application/json");
        post.setHeader("Authorization", "key=" + SERVER_KEY);

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode message = mapper.createObjectNode();
        message.put("to", DEVICE_TOKEN);
        message.put("priority", "high");

        ObjectNode notification = mapper.createObjectNode();
        notification.put("title", "Cassini Team");
        notification.put("body", "Cassini Systems");
        notification.put("sound", "default");
        notification.put("click_action", "FCM_PLUGIN_ACTIVITY");

        message.putPOJO("notification", notification);

        ObjectNode data = mapper.createObjectNode();
        data.put("content-available", "1");
        data.put("key1", "value 1");
        data.put("key2", "value 2");

        message.putPOJO("data", data);

        post.setEntity(new StringEntity(message.toString(), "UTF-8"));
        HttpResponse response = client.execute(post);
        if(response.getStatusLine().getStatusCode() == 200) {
            System.out.println("Message sent successfully");
        }
    }
}
