package com.thachlam.webhook;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder(builderClassName = "Builder")
@Getter
@Setter
public class SlackWebhookMessage implements Serializable {

    private String channel;
    private String username;
    private String text;
    private String icon_emoji;
}
