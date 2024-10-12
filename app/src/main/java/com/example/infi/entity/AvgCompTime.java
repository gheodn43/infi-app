package com.example.infi.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import java.math.BigDecimal;
@Entity(
        tableName = "avg_comp_time",
        foreignKeys = {
                @ForeignKey(entity = Deck.class, parentColumns = "deck_id", childColumns = "deck_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Card.class, parentColumns = "card_id", childColumns = "card_id", onDelete = ForeignKey.CASCADE)
        },
        primaryKeys = {"deck_id", "card_id"}
)
public class AvgCompTime {
    private long deck_id;       // Khóa ngoại liên kết với bảng Deck
    private long card_id;       // Khóa ngoại liên kết với bảng Card
    private int learning_count;    // Số lần học
    private BigDecimal total_time;

    // Constructor
    public AvgCompTime(long deck_id, long card_id) {
        this.deck_id = deck_id;
        this.card_id = card_id;
        this.learning_count = 0;
        this.total_time = new BigDecimal("0.00");;
    }

    public long getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(long deck_id) {
        this.deck_id = deck_id;
    }

    public long getCard_id() {
        return card_id;
    }

    public void setCard_id(long card_id) {
        this.card_id = card_id;
    }

    public int getLearning_count() {
        return learning_count;
    }

    public void setLearning_count(int learning_count) {
        this.learning_count = learning_count;
    }

    public BigDecimal getTotal_time() {
        return total_time;
    }

    public void setTotal_time(BigDecimal total_time) {
        this.total_time = total_time;
    }

    public void update_avg_comp_time(BigDecimal timeToComp) {
        if (timeToComp.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Time to complete cannot be negative.");
        }
        BigDecimal maxTimeToAdd = new BigDecimal("15.00");
        BigDecimal timeToAdd = timeToComp.min(maxTimeToAdd);
        setLearning_count(getLearning_count() + 1);
        setTotal_time(getTotal_time().add(timeToAdd));
    }


}
