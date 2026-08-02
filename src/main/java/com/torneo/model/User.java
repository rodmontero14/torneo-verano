package com.torneo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    private String id;
    private String name;
    private String password;
    private int number;
    private String team; // "PAPICHECHU" o "TUTIORODRI"
    private int subgroup; // 1 a 6
    private String trait; // ⚽, 🚲, 🌊, 🃏, 🏃‍♂️, 🎤, ⏰
    private boolean admin;

    public User() {}

    public User(String id, String name, String password, int number, String team, int subgroup, String trait, boolean admin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.number = number;
        this.team = team;
        this.subgroup = subgroup;
        this.trait = trait;
        this.admin = admin;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public int getSubgroup() { return subgroup; }
    public void setSubgroup(int subgroup) { this.subgroup = subgroup; }

    public String getTrait() { return trait; }
    public void setTrait(String trait) { this.trait = trait; }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
}
