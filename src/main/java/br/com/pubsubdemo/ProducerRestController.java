package br.com.pubsubdemo;

import br.com.pubsubdemo.domain.Email;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    ListenableFuture<String> publish(@PathVariable String name, @RequestBody Email email) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        return this.template.publish("email-topic", new ObjectMapper().writeValueAsBytes(email));
    }
}
