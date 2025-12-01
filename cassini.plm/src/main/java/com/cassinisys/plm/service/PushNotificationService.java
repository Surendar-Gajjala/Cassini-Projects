package com.cassinisys.plm.service;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Nageshreddy on 28-10-2020.
 */
@Component
public class PushNotificationService {

    private static final Logger LOGGER = Logger.getLogger("confLogger");
    private static String SERVER_KEY = "AAAA-onN8Ys:APA91bEHhM9k2GpgrYbaGcrbJUhEWJSFEB9IESpD4zlCBsF5DTRkJOi02LbcEmRoUMasbpc_GAU3NoXo-q6jk7P5Lu_uWW6SfTLrNcKTw7Lh1uJuSAMw6nxzc4S3ayzvagrHN-LxY34L";
    //    private static String DEVICE_TOKEN = "dUrT33EdwDo:APA91bFnoNh290gzYOzPxQaTpgD8v-hBMsRxf3IYEg8ALgeS8TcCEqMy4TWqeinqWuG5yudzNeJgMEgJ43i5uqUFfMIu4ywaYKzBXKakRL3IPtqJ5N8Ux96pmJ_9JlASf3blzd72kK1s";
    private static String DEVICE_TOKEN = "cMzPujTBuZI:APA91bEdfgH5Cmfik6Ge-IAXrxke83gOdK6_Ny8UUVCg82xUydKEjKBKN27rPPfttBTA0TnJf8yBysuttWvqsc9zTe2ZzDP66Nc75P8OI2YgwORyrjXwnoC9ydSFCaOToIDxHjj5RLBB";
    @Autowired
    private PersonRepository personRepository;

    public void sendPushNotification(String body, String[] tokens, String landing_page) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
        post.setHeader("Content-type", "application/json");
        post.setHeader("Authorization", "key=" + SERVER_KEY);

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode message = mapper.createObjectNode();
        if (tokens.length > 0) {
            message.putPOJO("registration_ids", tokens);
        } else {
            message.putPOJO("registration_ids", DEVICE_TOKEN);
        }

        message.put("priority", "high");

        ObjectNode notification = mapper.createObjectNode();
        notification.put("title", "CassiniPLM");
        notification.put("body", body);
        notification.put("sound", "default");
        notification.put("click_action", "FCM_PLUGIN_ACTIVITY");
        notification.put("icon", "fcm_push_icon");

        message.putPOJO("notification", notification);

        ObjectNode data = mapper.createObjectNode();
        data.put("landing_page", "itemDetails");
        data.put("key1", "value 1");
        data.put("key2", "value 2");

        message.putPOJO("data", data);

        post.setEntity(new StringEntity(message.toString(), "UTF-8"));
        HttpResponse response = null;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            new CassiniException(e.getMessage());
        }

        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            LOGGER.info("Message sent successfully");
        }
    }
}
