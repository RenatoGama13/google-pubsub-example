package br.com.pubsubdemo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConsumerRunner implements ApplicationRunner {

    private final PubSubTemplate template;

    public ConsumerRunner(PubSubTemplate template) {
        this.template = template;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.template.subscribe("email-subscription", (pubsubMessage, ackReplyConsumer) -> {
            System.out.println("receveid = " + pubsubMessage.getData().toStringUtf8());
            System.out.println(String.format("Send an email to %s on %s",
                    pubsubMessage.getAttributesMap().get("name"),
                    pubsubMessage.getAttributesMap().get("email")));
            ackReplyConsumer.ack();
        });
    }
}
