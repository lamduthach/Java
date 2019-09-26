package com.thachlam.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author Lam Du Thach
 */
public class SlackWebhookUtils {

    private static final String SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/xxxxxxxxxxxxxxxxxxxxxxxx";

    public static void sendMessage(SlackWebhookMessage message) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(SLACK_WEBHOOK_URL);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(message);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            client.execute(httpPost);
            client.close();
        } catch (IOException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }
}
