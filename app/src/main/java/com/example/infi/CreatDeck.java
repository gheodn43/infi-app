package com.example.infi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.infi.dao.DeckDao;
import com.example.infi.database.AppDatabase;
import com.example.infi.entity.Deck;

public class CreatDeck extends AppCompatActivity {

    private EditText deckNameInput;
    private Button createDeckButton;
    private DeckDao deckDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creat_deck);

        // Áp dụng insets cho padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Kết nối các thành phần trong layout
        deckNameInput = findViewById(R.id.deck_name_input);
        createDeckButton = findViewById(R.id.create_deck_button);

        // Lấy DeckDao từ cơ sở dữ liệu
        AppDatabase db = AppDatabase.getInstance(this);
        deckDao = db.deckDao();

        createDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deckName = deckNameInput.getText().toString().trim();
                if (!deckName.isEmpty()) {
                    Deck newDeck = new Deck(deckName);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            deckDao.insertDeck(newDeck);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CreatDeck.this, "Deck created successfully!", Toast.LENGTH_SHORT).show();
                                    deckNameInput.setText("");  // Clear input field
//                                    finish();
                                }
                            });
                        }
                    }).start();
                } else {
                    // Hiển thị Toast khi deck name trống
                    Toast.makeText(CreatDeck.this, "Please enter a deck name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
