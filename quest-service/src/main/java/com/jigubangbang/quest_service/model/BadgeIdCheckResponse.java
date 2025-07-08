package com.jigubangbang.quest_service.model;

public class BadgeIdCheckResponse {
    
    private boolean available;     
    private Integer suggestedId;   
    private String message;         

    public BadgeIdCheckResponse() {}

    public BadgeIdCheckResponse(boolean available, Integer suggestedId, String message) {
        this.available = available;
        this.suggestedId = suggestedId;
        this.message = message;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Integer getSuggestedId() {
        return suggestedId;
    }

    public void setSuggestedId(Integer suggestedId) {
        this.suggestedId = suggestedId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BadgeIdCheckResponse{" +
                "available=" + available +
                ", suggestedId=" + suggestedId +
                ", message='" + message + '\'' +
                '}';
    }
}
