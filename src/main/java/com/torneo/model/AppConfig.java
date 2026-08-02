package com.torneo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AppConfig {

    @Id
    private String id = "config";
    private int activeDay = 1;

    public AppConfig() {
    }

    public AppConfig(String id, int activeDay) {
        this.id = id;
        this.activeDay = activeDay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getActiveDay() {
        return activeDay;
    }

    public void setActiveDay(int activeDay) {
        this.activeDay = activeDay;
    }
}
