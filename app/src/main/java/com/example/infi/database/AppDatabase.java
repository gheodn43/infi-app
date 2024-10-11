package com.example.infi.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.infi.dao.DeckDao;
import com.example.infi.entity.Deck;

@Database(entities = {Deck.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DeckDao deckDao();
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
