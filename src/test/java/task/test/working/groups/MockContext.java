package task.test.working.groups;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import task.test.working.groups.clients.api.KafkaClient;

import static org.mockito.Mockito.mock;

@Configuration
public class MockContext {
    @Bean
    @Primary
    public KafkaClient kafkaClient() {
        return mock(KafkaClient.class);
    }
}
