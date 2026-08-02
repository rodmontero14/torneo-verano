package com.torneo.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private double points;
    private int dayNumber;
    private String targetType; // "INDIVIDUAL" o "SUBGRUPO"

    // Guardamos "USER:{id}" o "SUBGROUP:{id}"
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> completedBy = new HashSet<>();

    public Challenge() {}

    public Challenge(String title, String description, double points, int dayNumber, String targetType) {
        this.title = title;
        this.description = description;
        this.points = points;
        this.dayNumber = dayNumber;
        this.targetType = targetType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPoints() { return points; }
    public void setPoints(double points) { this.points = points; }

    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public Set<String> getCompletedBy() { return completedBy; }
    public void setCompletedBy(Set<String> completedBy) { this.completedBy = completedBy; }

    public void addCompletedBy(String identifier) {
        this.completedBy.add(identifier);
    }

    // Lógica para saber si el reto ya ha sido completado
    public boolean isCompletedFor(User user) {
        if (this.targetType != null && !this.targetType.equalsIgnoreCase("INDIVIDUAL")) {
            // Si es de subgrupo, comprobamos si el subgrupo ya lo tiene marcado
            return this.completedBy.contains("SUBGROUP:" + user.getSubgroup());
        }
        // Si es individual, comprobamos el usuario concreto
        return this.completedBy.contains("USER:" + user.getId());
    }
}