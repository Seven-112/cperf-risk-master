package com.mshz.webflux.dto;

import java.util.List;

public class NotifDTO {
    private List<NotifEntity> entities;
    private Long senderId;
    private String senderLogin;
    private NotifAction action;
    private List<Long> targetUserIds;

    public NotifDTO(){}

    public NotifDTO(List<NotifEntity> entities, Long senderId, String senderLogin, NotifAction action,List<Long> targetUserIds) {
        this.entities = entities;
        this.senderId = senderId;
        this.senderLogin = senderLogin;
        this.action = action;
        this.targetUserIds = targetUserIds;
    }

    
    public List<NotifEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<NotifEntity> entities) {
        this.entities = entities;
    }

    public Long getSenderId() {
        return senderId;
    }
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    public String getSenderLogin() {
        return senderLogin;
    }
    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }
    public NotifAction getAction() {
        return action;
    }
    public void setAction(NotifAction action) {
        this.action = action;
    }

    public List<Long> getTargetUserIds() {
        return targetUserIds;
    }

    public void setTargetUserIds(List<Long> targetUserIds) {
        this.targetUserIds = targetUserIds;
    }
    
}
