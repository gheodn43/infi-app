package com.example.infi.dataStructure;
import com.example.infi.entity.Card;

import java.util.ArrayList;
import java.util.List;

public class MinHeap {
    private List<HeapNode> heap;

    public MinHeap() {
        this.heap = new ArrayList<>();
    }

    public void insert(Card card, long timeToShow) {
        heap.add(new HeapNode(card, timeToShow));
        bubbleUp();
    }

    private void bubbleUp() {
        int index = heap.size() - 1;
        HeapNode element = heap.get(index);

        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            HeapNode parent = heap.get(parentIndex);

            if (element.timeToShow >= parent.timeToShow) {
                break;
            }

            heap.set(index, parent);
            index = parentIndex;
        }
        heap.set(index, element);
    }

    public HeapNode remove() {
        if (heap.isEmpty()) return null;

        HeapNode min = heap.get(0);
        HeapNode end = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, end);
            sinkDown();
        }
        return min;
    }

    private void sinkDown() {
        int index = 0;
        int length = heap.size();
        HeapNode element = heap.get(0);

        while (true) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;
            int swap = -1;

            if (leftChildIndex < length) {
                HeapNode leftChild = heap.get(leftChildIndex);
                if (leftChild.timeToShow < element.timeToShow) {
                    swap = leftChildIndex;
                }
            }

            if (rightChildIndex < length) {
                HeapNode rightChild = heap.get(rightChildIndex);
                if ((swap == -1 && rightChild.timeToShow < element.timeToShow) ||
                        (swap != -1 && rightChild.timeToShow < heap.get(swap).timeToShow)) {
                    swap = rightChildIndex;
                }
            }

            if (swap == -1) break;

            heap.set(index, heap.get(swap));
            index = swap;
        }
        heap.set(index, element);
    }

    public HeapNode peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public List<HeapNode> getHeap() {
        return heap;
    }

    public void clear() {
        heap.clear();
    }

    public static class HeapNode {
        public Card card;
        public long timeToShow;

        public HeapNode(Card card, long timeToShow) {
            this.card = card;
            this.timeToShow = timeToShow;
        }
    }
}
