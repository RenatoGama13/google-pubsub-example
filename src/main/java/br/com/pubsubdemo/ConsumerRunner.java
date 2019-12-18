package br.com.pubsubdemo;

import br.com.pubsubdemo.domain.Email;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
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
        this.template.subscribe("email-subscription", pubsubMessage -> {

            String pubsubResponse = pubsubMessage.getPubsubMessage().getData().toStringUtf8();
            Email email = null;
            try {
                email = (Email) this.stringToObject(pubsubResponse, Email.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            System.out.println(email.getName());
            System.out.println(email.getEmail());
            pubsubMessage.ack();
        });
    }

    private Object stringToObject(String response, Class clazz) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(response, clazz);
    }

}
