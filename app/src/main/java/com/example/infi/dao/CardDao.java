package com.example.infi.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.infi.entity.Card;
import java.util.List;

@Dao
public interface CardDao {
    @Insert
    void insertCard(Card card);

    @Update
    void updateCard(Card card);

    @Query("SELECT * FROM card WHERE deck_id = :deckId " +
            "AND card_status IN ('REVIEW_CARD', 'NEW_CARD', 'LEARNING_CARD') " +
            "ORDER BY CASE card_status " +
            "WHEN 'REVIEW_CARD' THEN 1 " +
            "WHEN 'NEW_CARD' THEN 2 " +
            "WHEN 'LEARNING_CARD' THEN 3 END")
    List<Card> getCardsByStatus(long deckId);

}

