package com.thachlam.webhook;

/**
 *
 * @author Lam Du Thach
 */
public class Example {

    public static void main(String[] args) {
        SlackWebhookMessage slackMessage = SlackWebhookMessage.builder()
                .channel("webhook-java")
                .username("lamduthach")
                .text("This is test slack webhook java")
                .icon_emoji(":hugging_face:")
                .build();
        SlackWebhookUtils.sendMessage(slackMessage);

        SlackWebhookMessage slackMessage403 = SlackWebhookMessage.builder()
                .channel("webhook-java")
                .username("api-name1")
                .text("[403] Forbidden Baby")
                .icon_emoji(":no_entry:")
                .build();
        SlackWebhookUtils.sendMessage(slackMessage403);

        SlackWebhookMessage slackMessage500 = SlackWebhookMessage.builder()
                .channel("webhook-java")
                .username("api-name2")
                .text("[500] Server say : Help I'm not feel well")
                .icon_emoji(":warning:")
                .build();
        SlackWebhookUtils.sendMessage(slackMessage500);
    }
}
