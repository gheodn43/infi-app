package com.example.infi.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity(tableName = "deck")
public class Deck {

    @PrimaryKey(autoGenerate = true)
    private long deck_id;

    @ColumnInfo(name = "deck_name")
    private String deck_name;

    @ColumnInfo(name = "new_count")
    private int new_count;

    @ColumnInfo(name = "learning_count")
    private int learning_count;

    @ColumnInfo(name = "review_count")
    private int review_count;

    @ColumnInfo(name = "cooling_count")
    private int cooling_count;

    @ColumnInfo(name = "last_update")
    private String last_update;

    // Constructor
    public Deck(String deck_name) {
        this.deck_name = deck_name;
        this.new_count = 0;
        this.learning_count = 0;
        this.review_count = 0;
        this.cooling_count = 0;
        this.last_update = getCurrentTime();
    }

    // Getters và Setters
    public long getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(long deck_id) {
        this.deck_id = deck_id;
        setLast_update(this.getCurrentTime());
    }

    public String getDeck_name() {
        return deck_name;
    }

    public void setDeck_name(String deck_name) {
        this.deck_name = deck_name;
        setLast_update(this.getCurrentTime());
    }

    public int getNew_count() {
        return new_count;
    }

    public void setNew_count(int new_count) {
        this.new_count = new_count;
        setLast_update(this.getCurrentTime());
    }

    public int getLearning_count() {
        return learning_count;
    }

    public void setLearning_count(int learning_count) {
        this.learning_count = learning_count;
        setLast_update(this.getCurrentTime());
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
        setLast_update(this.getCurrentTime());
    }

    public int getCooling_count() {
        return cooling_count;
    }

    public void setCooling_count(int cooling_count) {
        this.cooling_count = cooling_count;
        setLast_update(this.getCurrentTime());
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return LocalDateTime.now().format(formatter);
    }
}
