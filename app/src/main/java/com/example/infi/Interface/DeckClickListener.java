package com.example.infi.Interface;

import androidx.cardview.widget.CardView;

import com.example.infi.entity.Deck;

public interface DeckClickListener {
    void onClick(Deck deck);
    void longPress(Deck deck, CardView cardView);
}
