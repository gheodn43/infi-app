package com.example.infi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.infi.dao.CardDao;
import com.example.infi.dao.DeckDao;
import com.example.infi.database.AppDatabase;
import com.example.infi.entity.Card;
import com.example.infi.entity.Deck;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DeckDetail extends AppCompatActivity {
    Button studyBtn, addCardBtn;
    EditText frontOfCard, backOfCard;
    TextView newCount, learningCount, reviewCount;

    private CardDao cardDao;
    private DeckDao deckDao;
    private Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deck_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các biến với giá trị từ findViewById
        studyBtn = findViewById(R.id.study_btn);
        addCardBtn = findViewById(R.id.create_card_button);
        frontOfCard = findViewById(R.id.card_front_input);
        backOfCard = findViewById(R.id.card_back_input);
        newCount = findViewById(R.id.txt_new_card_count);
        learningCount = findViewById(R.id.txt_learning_card_count);
        reviewCount = findViewById(R.id.txt_review_card_count);

        AppDatabase db = AppDatabase.getInstance(this);
        cardDao = db.cardDao();
        deckDao = db.deckDao();

        Intent intent = getIntent();
        long deckId = intent.getLongExtra("deck_id", -1);

        new Thread(() -> {
            deck = deckDao.getDeckById(deckId);
            runOnUiThread(() -> {
                if (deck != null) {
                    newCount.setText(String.valueOf(deck.getNew_count()));
                    learningCount.setText(String.valueOf(deck.getLearning_count()));
                    reviewCount.setText(String.valueOf(deck.getReview_count()));
                } else {
                    Toast.makeText(DeckDetail.this, "Deck not found!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

        // Chuyển hướng tới màn hình học tập
        studyBtn.setOnClickListener(view -> {
            Intent studyIntent = new Intent(DeckDetail.this, StudyActivity.class);
            studyIntent.putExtra("deck_id", deck.getDeck_id());
            startActivity(studyIntent);
        });

        // Handle sự kiện thêm mới
        addCardBtn.setOnClickListener(view -> {
            String contentOfFront = frontOfCard.getText().toString().trim();
            String contentOfBack = backOfCard.getText().toString().trim();

            if (!contentOfFront.isEmpty() || !contentOfBack.isEmpty()) {
                new Thread(() -> {
                    if (deck != null) {
                        Card newCard = new Card(deckId, contentOfFront, contentOfBack);
                        cardDao.insertCard(newCard);
                        deckDao.incrementNewCount(deckId, getCurrentTime());

                        deck = deckDao.getDeckById(deckId);
                        runOnUiThread(() -> {
                            Toast.makeText(DeckDetail.this, "Card added successfully!", Toast.LENGTH_SHORT).show();
                            frontOfCard.setText("");
                            backOfCard.setText("");
                            // Cập nhật realtime tren UI
                            newCount.setText(String.valueOf(deck.getNew_count()));
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(DeckDetail.this, "Deck ID does not exist!", Toast.LENGTH_SHORT).show());
                    }
                }).start();
            } else {
                Toast.makeText(DeckDetail.this, "Please fill in both front and back of card.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return LocalDateTime.now().format(formatter);
    }
}
