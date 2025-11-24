package model;

import java.util.List;

// ApiRequest.java
public class ApiRequest {
    private String model;
    private List<Message> messages;
    private double temperature;
    private int max_tokens;

    // 构造函数
    public ApiRequest(String model, List<Message> messages, double temperature, int max_tokens) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
    }

    // getters and setters
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public int getMax_tokens() { return max_tokens; }
    public void setMax_tokens(int max_tokens) { this.max_tokens = max_tokens; }
}
