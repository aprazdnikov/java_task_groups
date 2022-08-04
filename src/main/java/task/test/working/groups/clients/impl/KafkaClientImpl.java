package task.test.working.groups.clients.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import task.test.working.groups.clients.api.KafkaClient;
import task.test.working.groups.data.Message;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class KafkaClientImpl implements KafkaClient {
    private static final String TOPIC = "messages";

    private final KafkaTemplate<Long, Message> template;

    @Override
    public void send(Message message) {
        template.send(TOPIC, message);
    }
}
