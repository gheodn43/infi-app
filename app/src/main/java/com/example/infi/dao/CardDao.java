package com.example.infi.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.infi.entity.Deck;
import com.example.infi.entity.Card;
import com.example.infi.entity.AvgCompTime;
import java.util.List;

@Dao
public interface CardDao {
    @Insert
    void insertCard(Card card);
}
