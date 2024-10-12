package com.example.infi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.infi.Interface.DeckClickListener;
import com.example.infi.database.AppDatabase;
import com.example.infi.entity.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DeckAdapter deckAdapter;
    AppDatabase appDatabase;
    List<Deck> decks = new ArrayList<>();
    Button createNewDeckBtn; // Khai báo button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.deckRv);
        createNewDeckBtn = findViewById(R.id.createNewDeckBtn); // Khởi tạo button
        createNewDeckBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreatDeck.class);
            startActivity(intent);
        });
        appDatabase = AppDatabase.getInstance(this);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                decks = appDatabase.deckDao().getAllDecks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateRecycle(decks);
                    }
                });
            }
        });
    }

    private void updateRecycle(List<Deck> decks) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        deckAdapter = new DeckAdapter(MainActivity.this, decks, deckClickListener);
        recyclerView.setAdapter(deckAdapter);
    }

    private final DeckClickListener deckClickListener = new DeckClickListener() {
        @Override
        public void onClick(Deck deck) {
            // click thì mở activity deck
        }

        @Override
        public void longPress(Deck deck, CardView cardView) {
            // giữ lâu thì hiện ra nút xóa, đổi tên
        }
    };
}
