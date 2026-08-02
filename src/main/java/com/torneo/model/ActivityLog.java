package com.torneo.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String userName;
    private String team;
    private int subgroup;
    private String type; // Correr, Ciclismo, Futbol, Aguadillas, Cartas, Piscina, Cancion, Reto
    private double amount;
    private String gameOrDetail; // Aquí guardaremos el ID del reto cuando sea type="Reto"
    private double pointsMvp;
    private int dayNumber;

    @Column(name = "activity_day")
    private Integer day = 1; // Día del torneo (1 al 7)

    private String timestamp;

    public ActivityLog() {
        this.timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public ActivityLog(String userId, String userName, String team, int subgroup, String type, double amount, String gameOrDetail, double pointsMvp, int dayNumber) {
        this.userId = userId;
        this.userName = userName;
        this.team = team;
        this.subgroup = subgroup;
        this.type = type;
        this.amount = amount;
        this.gameOrDetail = gameOrDetail;
        this.pointsMvp = pointsMvp;
        this.dayNumber = dayNumber;
        this.day = dayNumber;
        this.timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public int getSubgroup() { return subgroup; }
    public void setSubgroup(int subgroup) { this.subgroup = subgroup; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getGameOrDetail() { return gameOrDetail; }
    public void setGameOrDetail(String gameOrDetail) { this.gameOrDetail = gameOrDetail; }

    public double getPointsMvp() { return pointsMvp; }
    public void setPointsMvp(double pointsMvp) { this.pointsMvp = pointsMvp; }

    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int dayNumber) { 
        this.dayNumber = dayNumber; 
        this.day = dayNumber;
    }

    public Integer getDay() { return day; }
    public void setDay(Integer day) { 
        this.day = day; 
        if (day != null) this.dayNumber = day;
    }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}