package com.example.m_hike;

public class ObservationData {
    private String name;
    private String time;
    private String comment;
    private int hikingId;

    public ObservationData(String name, String time, String comment, int hikingId) {
        this.name = name;
        this.time = time;
        this.comment = comment;
        this.hikingId = hikingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getHikingId() {
        return hikingId;
    }

    public void setHikingId(int hikingId) {
        this.hikingId = hikingId;
    }
}
