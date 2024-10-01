package com.example.infi.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "deck")
public class Deck {

    @PrimaryKey(autoGenerate = true)
    private int deck_id;
    private String deck_name;
    private String deck_path;
    private String parent_deck_path;
    private int new_count;
    private int learning_count;
    private int review_count;
    private int cooling_count;
    private String last_update;

    // Constructor
    public Deck(String deck_path) {
        this.deck_path = deck_path;
        String[] paths = deck_path.split("::");

        this.deck_name = paths[paths.length - 1];
        this.parent_deck_path = paths.length > 1 ? deck_path.substring(0, deck_path.lastIndexOf("::")) : null; // Lấy path của parent nếu có

        this.new_count = 0;
        this.learning_count = 0;
        this.review_count = 0;
        this.cooling_count = 0;

        updateTimestamp();
    }

    // Getters và Setters
    public int getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(int deck_id) {
        this.deck_id = deck_id;
        updateTimestamp();
    }

    public String getDeck_name() {
        return deck_name;
    }

    public void setDeck_name(String deck_name) {
        this.deck_name = deck_name;
        updateTimestamp();
    }

    public String getDeck_path() {
        return deck_path;
    }

    public void setDeck_path(String deck_path) {
        this.deck_path = deck_path;
        String[] paths = deck_path.split("::");
        this.deck_name = paths[paths.length - 1];
        this.parent_deck_path = paths.length > 1 ? deck_path.substring(0, deck_path.lastIndexOf("::")) : null;
        updateTimestamp();
    }

    public String getParent_deck_path() {
        return parent_deck_path;
    }

    public int getNew_count() {
        return new_count;
    }

    public void setNew_count(int new_count) {
        this.new_count = new_count;
        updateTimestamp();
    }

    public int getLearning_count() {
        return learning_count;
    }

    public void setLearning_count(int learning_count) {
        this.learning_count = learning_count;
        updateTimestamp();
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
        updateTimestamp();
    }

    public int getCooling_count() {
        return cooling_count;
    }

    public void setCooling_count(int cooling_count) {
        this.cooling_count = cooling_count;
        updateTimestamp();
    }

    public String getLast_update() {
        return last_update;
    }

    private void updateTimestamp() {
        this.last_update = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
