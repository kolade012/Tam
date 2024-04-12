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
        // Implement comparison logic based on date and time
        // For example:
        // return this.date.compareTo(other.date); // Compare dates
        // Or combine date and time for comparison
        // String thisDateTime = this.date + " " + this.time;
        // String otherDateTime = other.date + " " + other.time;
        // return thisDateTime.compareTo(otherDateTime);
        return 0; // Placeholder, update according to your logic
    }
}
