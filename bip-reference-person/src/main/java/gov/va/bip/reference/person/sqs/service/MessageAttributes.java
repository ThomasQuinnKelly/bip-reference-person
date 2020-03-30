package gov.va.bip.reference.person.sqs.service;

public class MessageAttributes {
    private String message;
    private int numberOfRetries;
    private long createTimestamp = System.currentTimeMillis();

    public MessageAttributes() {}

    public MessageAttributes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getNumberOfRetries() {
        return numberOfRetries;
    }
    public void setNumberOfRetries(int numberOfAttempts) {
        this.numberOfRetries = numberOfAttempts;
    }
    public long getCreateTimestamp() {
        return createTimestamp;
    }
    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String toJson() {
        return "{" +
                "\"message\":\"" + message + '\"' +
                ", \"numberOfRetries\":" + numberOfRetries +
                ", \"createTimestamp\":" + createTimestamp +
                "}";
    }
}
