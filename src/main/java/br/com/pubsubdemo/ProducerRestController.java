package br.com.pubsubdemo;

import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProducerRestController {

    private final PubSubTemplate template;

    public ProducerRestController(PubSubTemplate template) {
        this.template = template;
    }

    @PostMapping("/publish/{name}")
    ListenableFuture<String> publish(@PathVariable String name, @RequestBody EmailRequest emailRequest){
        Map<String, String> map = new HashMap<>();
        map.put("email", emailRequest.getEmail());
        map.put("name", emailRequest.getName());
        return this.template.publish("email-topic", "message " + name + "!", map);
    }
}
