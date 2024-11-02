package com.example.infi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.infi.Interface.DeckClickListener;
import com.example.infi.database.AppDatabase;
import com.example.infi.entity.Card;
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
    PopupMenu popupMenu;
    MenuInflater inflater;


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
            Intent intent = new Intent(MainActivity.this, DeckDetail.class);
            intent.putExtra("deck_id", deck.getDeck_id());
            startActivity(intent);
        }

        @Override
        public void longPress(Deck deck, CardView cardView) {
            // giữ lâu 1 desk thì hiện ra nút xóa, đổi tên
            popupMenu = new PopupMenu(MainActivity.this, cardView);
            inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.deck_option_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.menu_study) {
                        // Điều hướng đến màn hình học tập (Study)
                        Intent studyIntent = new Intent(MainActivity.this, StudyActivity.class);
                        studyIntent.putExtra("deck_id", deck.getDeck_id());
                        startActivity(studyIntent);
                        return true;

                    } else if (itemId == R.id.menu_delete) {
                        // Xóa Deck (Delete)
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Xác nhận")
                                .setMessage("Bạn có chắc chắn muốn xóa deck này không?")
                                .setPositiveButton("Có", (dialog, which) -> {
                                    deleteDeck(deck);
                                    Toast.makeText(MainActivity.this, "Delete deck successfully!", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("Không", null)
                                .show();


                        return true;

                    } else {
                        return false;
                    }
                }
            });

            popupMenu.show();

        }
    };

    // Refresh data
    @Override
    protected void onResume() {
        super.onResume();
        loadDecks();
    }

    private void loadDecks() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                decks.clear();
                decks.addAll(appDatabase.deckDao().getAllDecks());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deckAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void deleteDeck(Deck deck) {
        new Thread(() -> {
            appDatabase.deckDao().deleteDeck(deck);
            decks = appDatabase.deckDao().getAllDecks();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateRecycle(decks);
                }
            });
        }).start();
    }
}
