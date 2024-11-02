package com.example.infi.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import com.example.infi.dao.CardDao;
import com.example.infi.dao.DeckDao;
import com.example.infi.entity.Card;
import com.example.infi.entity.Deck;
import com.example.infi.pipe.Converters;

@Database(entities = {Deck.class, Card.class}, version = 6, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DeckDao deckDao();
    public abstract CardDao cardDao();
    private static AppDatabase INSTANCE;
    public static synchronized  AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "infi_database")
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }
}
