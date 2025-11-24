package model;

public class Choice {
    private Message message;
    private int index;
    private String finish_reason;

    // getters and setters
    public Message getMessage() { return message; }
    public void setMessage(Message message) { this.message = message; }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    public String getFinish_reason() { return finish_reason; }
    public void setFinish_reason(String finish_reason) { this.finish_reason = finish_reason; }
}
