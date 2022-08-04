package task.test.working.groups.clients.api;


import task.test.working.groups.data.Message;

public interface KafkaClient {
    void send(Message message);
}
