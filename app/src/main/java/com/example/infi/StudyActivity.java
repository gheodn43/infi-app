package com.example.infi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StudyActivity extends AppCompatActivity {

    private Button showAnswerBtn;
    private LinearLayout levelBtns;
    private TextView frontContent, backContent;
    private boolean isAnswerShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        showAnswerBtn = findViewById(R.id.show_answer_btn);
        levelBtns = findViewById(R.id.layout_level_btns);
        frontContent = findViewById(R.id.txt_front_content);
        backContent = findViewById(R.id.txt_back_content);

        levelBtns.setVisibility(View.GONE);
        backContent.setAlpha(0);
        showAnswerBtn.setOnClickListener(v -> {
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
            backAnim.addListener(new AnimatorListenerAdapter() {
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

}
