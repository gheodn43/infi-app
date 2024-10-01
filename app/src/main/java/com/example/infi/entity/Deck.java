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
        setDeck_path(deck_path);
        this.new_count = 0;
        this.learning_count = 0;
        this.review_count = 0;
        this.cooling_count = 0;
        this.last_update = this.getCurrentTime();
    }

    // Getters và Setters
    public int getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(int deck_id) {
        this.deck_id = deck_id;
        setLast_update(this.getCurrentTime());
    }

    public String getDeck_name() {
        return deck_name;
    }

    public void setDeck_name(String deck_name) {
        this.deck_name = deck_name;
        updateDeckPathAfterNameChange();
        setLast_update(this.getCurrentTime());
    }

    public String getDeck_path() {
        return deck_path;
    }

    public void setDeck_path(String deck_path) {
        this.deck_path = deck_path;
        updateDeckNameAndParentPathAfterPathChange();
        setLast_update(this.getCurrentTime());
    }

    public String getParent_deck_path() {
        return parent_deck_path;
    }
    public void setParent_deck_path(String parent_deck_path) {
        this.parent_deck_path = parent_deck_path;
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
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
    private void updateDeckPathAfterNameChange() {
        if (this.parent_deck_path != null) {
            this.deck_path = this.parent_deck_path + "::" + this.deck_name;
        } else {
            this.deck_path = this.deck_name;
        }
    }
    private void updateDeckNameAndParentPathAfterPathChange() {
        String[] paths = this.deck_path.split("::");
        this.deck_name = paths[paths.length - 1];
        this.parent_deck_path = paths.length > 1 ? this.deck_path.substring(0, this.deck_path.lastIndexOf("::")) : null;
    }
}
