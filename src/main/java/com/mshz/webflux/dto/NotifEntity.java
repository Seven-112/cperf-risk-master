package com.mshz.webflux.dto;

public class NotifEntity {
    private Long id;
    private String name;

    public NotifEntity(){}

    public NotifEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "{id:"+id+", name:"+name+"}";
    }
}
