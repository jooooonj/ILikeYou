package com.ll.gramgram.boundedContext.notification.entity;

public enum NotificationType {
    LIKE("1", "LIKE"),
    MODIFY("2","어드민")
    ;
    private final String typeNum;
    private final String type;

    NotificationType(String typeNum, String type) {
        this.typeNum = typeNum;
        this.type = type;
    }

    public String getTypeNum() {
        return typeNum;
    }

    public String getType() {
        return type;
    }
}
