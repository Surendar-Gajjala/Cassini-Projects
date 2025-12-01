package com.cassinisys.erp.notification;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.service.notification.push.GCMMessage;
import com.cassinisys.erp.service.notification.push.GCMPushNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by reddy on 18/02/16.
 */
public class GCMMessageTest extends BaseTest {

    @Autowired
    private GCMPushNotification pushNotification;

    @Test
    public void testSendMessage() throws Exception {
        String to = "evxOIOpXBRo:APA91bEBPzM-6Tmp8HBnb2IRxYNAHNc0xOO-O0UGwZnJBArmPOErN32qZF2tuEzFsSP4dhXSnN-1fV4Xd4YOal4NTEKMhohw4dtOGtI_M8zjnw4hccjDnAsfHsmWo_fX0L2NRRtDVqXq";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data = mapper.createObjectNode();

        data.put("id", 222);
        data.put("orderNumber", "P00001");
        data.put("customer", "ABCD Concept School");

        GCMMessage message = new GCMMessage(to, data);
        pushNotification.sendMessage(message);
    }
}
