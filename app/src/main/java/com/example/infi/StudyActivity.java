package com.example.infi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;


import com.example.infi.dao.CardDao;
import com.example.infi.dao.DeckDao;
import com.example.infi.database.AppDatabase;
import com.example.infi.entity.Card;
import com.example.infi.entity.Deck;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class StudyActivity extends AppCompatActivity {

    private Button exitBtn, showAnswerBtn, againBtn, hardBtn, goodBtn, easyBtn;
    private LinearLayout levelBtns;
    private TextView deckName, frontContent, backContent, againTime, hardTime, goodTime, easyTime, timerText;
    private boolean isAnswerShown = false;
    private CardManager cardManager;
    private CardDao cardDao;
    private DeckDao deckDao;
    private Card card;

    private Handler handler;
    private Runnable timerRunnable;
    private long startTime, deckId;
    private String stoppedTime;
    private Intent intent;

    private Deck deck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        intent = getIntent();
        deckId = intent.getLongExtra("deck_id", -1);

        initializeViews();


        cardManager = new CardManager();
        AppDatabase db = AppDatabase.getInstance(this);
        cardDao = db.cardDao();
        deckDao = db.deckDao();

        new Thread(() -> {
            deck = deckDao.getDeckById(deckId);
            runOnUiThread(() -> {
                if (deck != null) {
                    deckName.setText(String.valueOf(deck.getDeck_name()));
                } else {
                    Toast.makeText(StudyActivity.this, "Deck not found!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

        setupButtonClickListeners();
        loadCards();


        levelBtns.setVisibility(View.GONE);
        backContent.setAlpha(0);
        showAnswerBtn.setOnClickListener(v -> {
            stopTimer();
            flipCard();
            showAnswerBtn.setVisibility(View.GONE);
            levelBtns.setVisibility(View.VISIBLE);
            isAnswerShown = true;
        });
        frontContent.setOnClickListener(v -> {
            if (isAnswerShown) {
                flipCard();
            }
        });

        backContent.setOnClickListener(v -> {
            if (isAnswerShown) {
                flipCard();
            }
        });
    }

    private void initializeViews() {
        deckName = findViewById(R.id.text_view_deck_name);
        exitBtn = findViewById(R.id.btn_exit_study);
        showAnswerBtn = findViewById(R.id.show_answer_btn);
        levelBtns = findViewById(R.id.layout_level_btns);
        againBtn = findViewById(R.id.btn_again_level);
        hardBtn = findViewById(R.id.btn_hard_level);
        goodBtn = findViewById(R.id.btn_good_level);
        easyBtn = findViewById(R.id.btn_easy_level);
        frontContent = findViewById(R.id.txt_front_content);
        backContent = findViewById(R.id.txt_back_content);
        againTime = findViewById(R.id.txt_again_delay);
        hardTime = findViewById(R.id.txt_hard_delay);
        goodTime = findViewById(R.id.txt_good_delay);
        easyTime = findViewById(R.id.txt_easy_delay);
        timerText = findViewById(R.id.text_view_timer);
    }

    private void loadCards() {
        new Thread(() -> {
            List<Card> cards = cardDao.getCardsByStatus(deckId);
            runOnUiThread(() -> {
                if (!cards.isEmpty()) {
                    for (Card card : cards) {
                        cardManager.selectCardTime(card, 0);
                    }
                    displayNextCard();
                } else {
                    frontContent.setText("No cards available to study.");
                    showAnswerBtn.setVisibility(View.GONE);
                }
            });
        }).start();
    }


    private void displayNextCard() {
        card = cardManager.displayNextCard();
        resetState();
        if (card != null) {
            startTimer();
            frontContent.setText(card.getCard_front());
            backContent.setText(card.getCard_back());
            againTime.setText(card.getAgain());
            hardTime.setText(card.getHard());
            goodTime.setText(card.getGood());
            easyTime.setText(card.getEasy());
        } else {
            frontContent.setText("No cards available to study.");
            showAnswerBtn.setVisibility(View.GONE);
        }
    }

    private void resetState() {
        if (backContent.getAlpha() == 1) {
            flipCard();
        }
        isAnswerShown = false;
        levelBtns.setVisibility(View.GONE);
        showAnswerBtn.setVisibility(View.VISIBLE);
        resetTimer();
    }


    private void setupButtonClickListeners() {
        againBtn.setOnClickListener(v -> handleCardReview("again", card.getAgain()));
        hardBtn.setOnClickListener(v -> handleCardReview("hard", card.getHard()));
        goodBtn.setOnClickListener(v -> handleCardReview("good", card.getGood()));
        easyBtn.setOnClickListener(v -> handleCardReview("easy", card.getEasy()));
        exitBtn.setOnClickListener(v -> handleExitStudy());
    }

    private void handleExitStudy() {
        List<Card> cardsInHeap = cardManager.getHeap();
        int count = cardsInHeap.size() + 1;
        if (card != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn còn " + count + " thẻ chưa học! Lưu kết quả và học sau?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        Card currentCard = card;
                        new Thread(() -> cardDao.updateCard(currentCard)).start();
                        for (Card cardInheap : cardsInHeap) {
                            new Thread(() -> cardDao.updateCard(cardInheap)).start();
                        }
                        Intent intent = new Intent(StudyActivity.this, MainActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Không", null)
                    .show();
        } else {
            Intent intent = new Intent(StudyActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void handleCardReview(String level, String delay) {
        BigDecimal timeToComp = convertTimeToBigDecimal(stoppedTime);
        int newDiff = card.calculateDifficulty(timeToComp);
        card.setCard_diff(newDiff);
        switch (card.getCard_step()) {
            case 0:
                if (level.equals("again") || level.equals("hard") || level.equals("good")) {
                    if (!card.getCard_status().equals("LEARNING_CARD")) {
                        card.changeStatusToLearning();
                        new Thread(() -> deckDao.moveNewToLearning(deckId, getCurrentTime())).start();
                    }

                    int delayValue = Integer.parseInt(delay.substring(0, delay.length() - 1));
                    cardManager.selectCardTime(card, delayValue);
                    displayNextCard();
                } else {
                    if (card.getCard_status().equals("LEARNING_CARD")) {
                        new Thread(() -> deckDao.moveLearningToCooling(deckId, getCurrentTime())).start();
                    } else {
                        new Thread(() -> deckDao.moveNewToCooling(deckId, getCurrentTime())).start();
                    }
                    card.ms5(level);
                    Card currentCard = card;
                    new Thread(() -> cardDao.updateCard(currentCard)).start();
                    displayNextCard();
                }
                break;
            case 1:
                if (level.equals("again") || level.equals("hard")) {
                    if (!card.getCard_status().equals("LEARNING_CARD")) {
                        card.changeStatusToLearning();
                        new Thread(() -> deckDao.moveReviewToLearning(deckId, getCurrentTime())).start();
                    }
                    int delayValue = Integer.parseInt(delay.substring(0, delay.length() - 1));
                    cardManager.selectCardTime(card, delayValue);
                    displayNextCard();
                } else {
                    if (card.getCard_status().equals("LEARNING_CARD")) {
                        new Thread(() -> deckDao.moveLearningToCooling(deckId, getCurrentTime())).start();
                    } else {
                        new Thread(() -> deckDao.moveReviewToCooling(deckId, getCurrentTime())).start();
                    }
                    card.ms5(level);
                    Card currentCard = card;
                    new Thread(() -> cardDao.updateCard(currentCard)).start();
                    displayNextCard();
                }
                break;
            case 2:
                if (level.equals("again")) {
                    card.ms5(level);
                    if (!card.getCard_status().equals("LEARNING_CARD")) {
                        new Thread(() -> deckDao.moveReviewToLearning(deckId, getCurrentTime())).start();
                    }
                    int delayValue = Integer.parseInt(delay.substring(0, delay.length() - 1));
                    cardManager.selectCardTime(card, delayValue);
                    displayNextCard();
                } else {
                    if (card.getCard_status().equals("LEARNING_CARD")) {
                        new Thread(() -> deckDao.moveLearningToCooling(deckId, getCurrentTime())).start();
                    } else {
                        new Thread(() -> deckDao.moveReviewToCooling(deckId, getCurrentTime())).start();
                    }
                    card.ms5(level);
                    Card currentCard = card;
                    new Thread(() -> cardDao.updateCard(currentCard)).start();
                    displayNextCard();
                }
                break;
            default:
                break;
        }

    }

    private void startTimer() {
        handler = new Handler();
        startTime = System.currentTimeMillis();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                int seconds = (int) (elapsedTime / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                int milliseconds = (int) (elapsedTime % 1000 / 10);
                timerText.setText(String.format("%02d:%02d.%02d", minutes, seconds, milliseconds));
                if (minutes < 1) {  // Continue counting up to 59.59
                    handler.postDelayed(this, 10);
                }
            }
        };
        handler.post(timerRunnable);
    }

    private void stopTimer() {
        if (handler != null && timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
            long elapsedTime = System.currentTimeMillis() - startTime;
            int seconds = (int) (elapsedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (elapsedTime % 1000 / 10);
            stoppedTime = String.format("%02d:%02d.%02d", minutes, seconds, milliseconds);
        }
    }

    private void resetTimer() {
        stopTimer();
        timerText.setText("00:00.00");
    }

    public BigDecimal convertTimeToBigDecimal(String time) {
        String[] parts = time.split(":");
        String secondsAndMillis = parts[1];
        return new BigDecimal(secondsAndMillis);
    }

    private void flipCard() {
        AnimatorSet frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.front_animator);
        AnimatorSet backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.back_animator);

        if (frontContent.getAlpha() == 1) {
            frontAnim.setTarget(frontContent);
            backAnim.setTarget(backContent);
            frontAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    frontContent.setAlpha(0);
                    backContent.setAlpha(1);
                }
            });
            frontAnim.start();
            backAnim.start();
        } else {
            frontAnim.setTarget(backContent);
            backAnim.setTarget(frontContent);
            frontAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    backContent.setAlpha(0);
                    frontContent.setAlpha(1);
                }
            });
            frontAnim.start();
            backAnim.start();
        }
    }

    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return LocalDateTime.now().format(formatter);
    }
}
