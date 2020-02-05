package gov.va.bip.reference.person.sqs.service;

public class MessageAttributes {
    private String processID;
    private String userID;
    private String message;
    private int numberOfRetries;
    private long createTimestamp = System.currentTimeMillis();

    public MessageAttributes() {}

    public MessageAttributes(String processID, String userID, String message, int numberOfRetries) {
        this.processID = processID;
        this.userID = userID;
        this.message = message;
        this.numberOfRetries = numberOfRetries;
    }

    public String getProcessID() {
        return processID;
    }
    public void setProcessID(String processID) {
        this.processID = processID;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
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
}
