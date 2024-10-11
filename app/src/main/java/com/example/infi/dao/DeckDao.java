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
    Deck getDeckById(int deck_id);

    @Query("SELECT * FROM deck WHERE deck_name LIKE :deck_name")
    List<Deck> findDecksByName(String deck_name);
}
