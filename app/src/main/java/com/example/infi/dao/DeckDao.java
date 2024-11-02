package com.example.infi.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.infi.entity.Deck;
import java.util.List;

@Dao
public interface DeckDao {

    @Insert
    void insertDeck(Deck deck);

    @Update
    void updateDeck(Deck deck);

    @Delete
    void deleteDeck(Deck deck);

    @Query("SELECT * FROM deck")
    List<Deck> getAllDecks();

    @Query("SELECT * FROM deck WHERE deck_id = :deck_id")
    Deck getDeckById(long deck_id);

    @Query("SELECT * FROM deck WHERE deck_name LIKE :deck_name")
    List<Deck> findDecksByName(String deck_name);

    @Query("UPDATE deck SET new_count = new_count + 1, last_update = :lastUpdate WHERE deck_id = :deck_id")
    void incrementNewCount(long deck_id, String lastUpdate);

    // Chuyển từ new -> learning
    @Query("UPDATE deck SET new_count = new_count - 1, learning_count = learning_count + 1, last_update = :lastUpdate WHERE deck_id = :deck_id AND new_count > 0")
    void moveNewToLearning(long deck_id, String lastUpdate);

    // Chuyển từ new -> cooling
    @Query("UPDATE deck SET new_count = new_count - 1, cooling_count = cooling_count + 1, last_update = :lastUpdate WHERE deck_id = :deck_id AND new_count > 0")
    void moveNewToCooling(long deck_id, String lastUpdate);

    // Chuyển từ learning -> cooling
    @Query("UPDATE deck SET learning_count = learning_count - 1, cooling_count = cooling_count + 1, last_update = :lastUpdate WHERE deck_id = :deck_id AND learning_count > 0")
    void moveLearningToCooling(long deck_id, String lastUpdate);

    // Chuyển từ cooling -> review
    @Query("UPDATE deck SET cooling_count = cooling_count - 1, review_count = review_count + 1, last_update = :lastUpdate WHERE deck_id = :deck_id AND cooling_count > 0")
    void moveCoolingToReview(long deck_id, String lastUpdate);

    // Chuyển từ review -> cooling
    @Query("UPDATE deck SET review_count = review_count - 1, cooling_count = cooling_count + 1, last_update = :lastUpdate WHERE deck_id = :deck_id AND review_count > 0")
    void moveReviewToCooling(long deck_id, String lastUpdate);

    // Chuyển từ review -> learning
    @Query("UPDATE deck SET review_count = review_count - 1, learning_count = learning_count + 1, last_update = :lastUpdate WHERE deck_id = :deck_id AND review_count > 0")
    void moveReviewToLearning(long deck_id, String lastUpdate);

}

