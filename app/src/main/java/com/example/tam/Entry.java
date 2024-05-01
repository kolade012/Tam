package com.example.tam;

public class Entry implements Comparable<Entry> {
    private String time;
    private String date;
    private String courseCode;
    private String venue;
    private String key; // Add a key field to store Firebase key

    // Required default constructor for Firebase
    public Entry() {
    }

    public Entry(String time, String date, String courseCode, String venue) {
        this.time = time;
        this.date = date;
        this.courseCode = courseCode;
        this.venue = venue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(Entry other) {
        // Compare entries based on date and time
        // First, compare dates
        int dateComparison = this.date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison;
        }

        // If dates are the same, compare times
        return this.time.compareTo(other.time);
    }
}
