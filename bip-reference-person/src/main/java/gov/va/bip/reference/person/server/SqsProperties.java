package gov.va.bip.reference.person.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@ConfigurationProperties(prefix = "aws.sqs", ignoreUnknownFields = false)
public class SqsProperties {
    private String queue;

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getQueueName() {
        return parseQueueName(queue);
    }

    private String parseQueueName(String endpoint) {
        URI endpointUri = URI.create(endpoint);
        String path = endpointUri.getPath();
        int pos = path.lastIndexOf('/');
        return path.substring(pos + 1);
    }
}
