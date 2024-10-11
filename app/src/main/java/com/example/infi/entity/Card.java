package com.example.infi.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity(
        tableName = "card",
        foreignKeys = @ForeignKey(
                entity = Deck.class,
                parentColumns = "deck_id",
                childColumns = "deck_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class Card {

    @PrimaryKey(autoGenerate = true)
    private long card_id;

    @ColumnInfo(name = "deck_id")
    private long deck_id;

    @ColumnInfo(name = "card_front")
    private String card_front;

    @ColumnInfo(name = "card_back")
    private String card_back;

    @ColumnInfo(name = "card_diff")
    private int card_diff;

    @ColumnInfo(name = "card_step")
    private int card_step;

    @ColumnInfo(name = "card_status")
    private String card_status;

    @ColumnInfo(name = "again")
    private String again;

    @ColumnInfo(name = "hard")
    private String hard;

    @ColumnInfo(name = "good")
    private String good;

    @ColumnInfo(name = "easy")
    private String easy;

    @ColumnInfo(name = "overdue_at")
    private String overdue_at;

    @Ignore
    private AvgCompTime avgCompTime;

    public Card(long deck_id, String card_front, String card_back) {
        this.deck_id = deck_id;
        this.card_front = card_front;
        this.card_back = card_back;
        this.card_diff = 1;
        this.card_step = 0;
        this.card_status = "NEW_CARD"; // LEARNING_CARD, REVIEW_CARD, COOLING_CARD
        this.again = "1m";
        this.hard = "5m";
        this.good = "10m";
        this.easy = "1d";
        this.overdue_at = null;
    }

    public long getCard_id() {
        return card_id;
    }

    public void setCard_id(long card_id) {
        this.card_id = card_id;
    }

    public long getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(long deck_id) {
        this.deck_id = deck_id;
    }

    public String getCard_front() {
        return card_front;
    }

    public void setCard_front(String card_front) {
        this.card_front = card_front;
    }

    public String getCard_back() {
        return card_back;
    }

    public void setCard_back(String card_back) {
        this.card_back = card_back;
    }

    public int getCard_diff() {
        return card_diff;
    }

    public void setCard_diff(int card_diff) {
        this.card_diff = card_diff;
    }

    public int getCard_step() {
        return card_step;
    }

    public void setCard_step(int card_step) {
        this.card_step = card_step;
    }

    public String getCard_status() {
        return card_status;
    }

    public void setCard_status(String card_status) {
        this.card_status = card_status;
    }

    public String getAgain() {
        return again;
    }

    public void setAgain(String again) {
        this.again = again;
    }

    public String getHard() {
        return hard;
    }

    public void setHard(String hard) {
        this.hard = hard;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getEasy() {
        return easy;
    }

    public void setEasy(String easy) {
        this.easy = easy;
    }

    public String getOverdue_at() {
        return overdue_at;
    }

    public void setAvgCompTime(AvgCompTime avgCompTime) {
        this.avgCompTime = avgCompTime;
    }

    public AvgCompTime getAvgCompTime() {
        return avgCompTime;
    }

    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return LocalDateTime.now().format(formatter);
    }

    // Phương thức thiết lập overdue_at
    public void setOverdue_at(String delay) {
        LocalDateTime now = LocalDateTime.now();
        int timeValue = Integer.parseInt(delay.substring(0, delay.length() - 1));
        char timeUnit = delay.charAt(delay.length() - 1);

        switch (timeUnit) {
            case 'd':
                now = now.plusDays(timeValue);
                break;
            case 'w':
                now = now.plusWeeks(timeValue);
                break;
            case 'M':
                now = now.plusMonths(timeValue);
                break;
            case 'y':
                now = now.plusYears(timeValue);
                break;
            default:
                throw new IllegalArgumentException("Invalid time unit");
        }

        this.overdue_at = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));
        this.card_status = "COOLING_CARD";
    }

    private void changeStepTo1() {
        setAgain("5m");
        setHard("10m");
        setGood("1d");
        setEasy("3d");
    }
    private void changeStatusToLearning(){
        setCard_status("LEARNING_CARD");
    }
    private void changeStatusToCooling(){
        setCard_status("COOLING_CARD");
    }
    private void changeStatusToReview(){
        setCard_status("REVIEW_CARD");
    }
    private void updateAvgCompTime(BigDecimal timeToComp) {
        if (avgCompTime != null) {
            avgCompTime.update_avg_comp_time(timeToComp);
        } else {
            throw new IllegalStateException("AvgCompTime is not initialized.");
        }
    }
    public int calculateDifficulty(BigDecimal timeToComp) {
        int learning_count_before = avgCompTime.getLearning_count();
        BigDecimal total_time_before = avgCompTime.getTotal_time();

        BigDecimal avg_time_before = (learning_count_before > 0)
                ? total_time_before.divide(BigDecimal.valueOf(learning_count_before), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        this.updateAvgCompTime(timeToComp);

        int learning_count_after = avgCompTime.getLearning_count();
        BigDecimal total_time_after = avgCompTime.getTotal_time();

        BigDecimal avg_time_after = (learning_count_after > 0)
                ? total_time_after.divide(BigDecimal.valueOf(learning_count_after), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal time_change = avg_time_before.subtract(avg_time_after);
        time_change = time_change.setScale(0, RoundingMode.HALF_UP);
        // Giới hạn time_change trong khoảng từ -15 đến 15
        time_change = time_change.max(BigDecimal.valueOf(-15)).min(BigDecimal.valueOf(15));
        BigDecimal difficultyChange = time_change.divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP);
        int difficultyChangeInt = difficultyChange.intValue();
        int difficulty = getCard_diff() - difficultyChangeInt;
        return Math.max(1, Math.min(5, difficulty));
    }

    private String estNextDelay(String current_delay, int jumping_value) {
        int timeValue = Integer.parseInt(current_delay.substring(0, current_delay.length() - 1));
        char timeUnit = current_delay.charAt(current_delay.length() - 1);
        int newTimeValue = timeValue + ((timeValue * jumping_value) / getCard_diff());
        return newTimeValue + String.valueOf(timeUnit);
    }

    public void ms5(String level) {
        final String AGAIN = "again";
        final String HARD = "hard";
        final String GOOD = "good";
        final String EASY = "easy";

        switch (level) {
            case AGAIN:
                // Handle case "again"
                handleAgain();
                break;
            case HARD:
                // Handle case "hard"
                handleHard();
                break;
            case GOOD:
                // Handle case "good"
                handleGood();
                break;
            case EASY:
                // Handle case "easy"
                handleEasy();
                break;
            default:
                // Handle invalid level
                break;
        }
    }

    private void handleAgain() {
        if (getCard_step() == 2) {
            this.changeStepTo1();
            this.changeStatusToLearning();
            setCard_step(1);
        }
    }

    private void handleHard() {
        if (getCard_step() == 2) {
            setOverdue_at(getHard());
        }
    }

    private void handleGood() {
        final int JUMPING_GOOD = 2;
        setOverdue_at(getGood());
        if (getCard_step() == 1) {
            setAgain("10m");
            setAgain(this.estNextDelay("1d", JUMPING_GOOD));
            setAgain(this.estNextDelay("3d", JUMPING_GOOD));
            setAgain(this.estNextDelay("5d", JUMPING_GOOD));
        } else if (getCard_step() == 2) {
            setAgain(this.estNextDelay(getHard(), JUMPING_GOOD));
            setAgain(this.estNextDelay(getGood(), JUMPING_GOOD));
            setAgain(this.estNextDelay(getEasy(), JUMPING_GOOD));
        }
    }

    private void handleEasy() {
        final int JUMPING_EASY = 3;
        setOverdue_at(getEasy());
        if (getCard_step() == 0) {
            this.changeStepTo1();
            setCard_step(1);
        } else if (getCard_step() == 1) {
            setAgain("10m");
            setAgain(this.estNextDelay("1d", JUMPING_EASY));
            setAgain(this.estNextDelay("3d", JUMPING_EASY));
            setAgain(this.estNextDelay("5d", JUMPING_EASY));
            setCard_step(2);
        } else {
            setAgain(this.estNextDelay(getHard(), JUMPING_EASY));
            setAgain(this.estNextDelay(getGood(), JUMPING_EASY));
            setAgain(this.estNextDelay(getEasy(), JUMPING_EASY));
        }
    }

}
