package team.bham.service.dto;

public class EmailRequest {

    private String subject;
    private String body;

    // Default constructor
    public EmailRequest() {}

    // Getters and setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
