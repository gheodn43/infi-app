package com.example.infi;

import com.example.infi.dataStructure.MinHeap;
import com.example.infi.entity.Card;

import java.util.ArrayList;
import java.util.List;

public class CardManager {
    private final MinHeap minHeap;
    public CardManager() {
        this.minHeap = new MinHeap();
    }

    public void selectCardTime(Card card, int selectedMinutes) {
        long currentTime = System.currentTimeMillis();
        long timeDelay = (long) selectedMinutes * 60 * 1000;
        long timeToShow = currentTime + timeDelay;
        minHeap.insert(card, timeToShow);
    }
    public Card displayFirstCard() {
        if (!minHeap.isEmpty()) {
            MinHeap.HeapNode firstNode = minHeap.peek();
            return firstNode.card;
        } else {
            return null;
        }
    }

    public Card displayNextCard() {
        if (!minHeap.isEmpty()) {
            MinHeap.HeapNode nextCard = minHeap.remove();
            return nextCard.card;
        } else {
            return null;
        }
    }

    public List<Card> getHeap() {
        List<Card> cards = new ArrayList<>();
        for (MinHeap.HeapNode node : minHeap.getHeap()) {
            cards.add(node.card);
        }
        return cards;
    }

    public void clearHeap() {
        minHeap.clear();
    }
}
