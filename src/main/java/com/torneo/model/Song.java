package com.torneo.model;

import jakarta.persistence.*;

@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
    private String team;
    private int dayNumber;

    public Song() {}

    public Song(String title, String artist, String team, int dayNumber) {
        this.title = title;
        this.artist = artist;
        this.team = team;
        this.dayNumber = dayNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }
}