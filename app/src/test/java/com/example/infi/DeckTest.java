package com.example.infi;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.example.infi.entity.Deck;

public class DeckTest {

    private Deck deck;

    @Before
    public void setUp() {
        // Khởi tạo đối tượng Deck với tên deck_name
        deck = new Deck("deck3");
    }

    @Test
    public void testConstructor() {
        // Kiểm tra các giá trị sau khi khởi tạo
        assertEquals("deck3", deck.getDeck_name());
        assertEquals(0, deck.getNew_count());
        assertEquals(0, deck.getLearning_count());
        assertEquals(0, deck.getReview_count());
        assertEquals(0, deck.getCooling_count());
        assertNotNull(deck.getLast_update());
    }

    @Test
    public void testSetDeckName() {
        // Thay đổi deck_name và kiểm tra
        deck.setDeck_name("deck4");
        assertEquals("deck4", deck.getDeck_name());
        assertNotNull(deck.getLast_update());
    }

    @Test
    public void testSetNewCount() {
        // Thay đổi new_count và kiểm tra
        deck.setNew_count(5);
        assertEquals(5, deck.getNew_count());
        assertNotNull(deck.getLast_update());
    }

    @Test
    public void testSetLearningCount() {
        // Thay đổi learning_count và kiểm tra
        deck.setLearning_count(3);
        assertEquals(3, deck.getLearning_count());
        assertNotNull(deck.getLast_update());
    }

    @Test
    public void testSetReviewCount() {
        // Thay đổi review_count và kiểm tra
        deck.setReview_count(7);
        assertEquals(7, deck.getReview_count());
        assertNotNull(deck.getLast_update());
    }

    @Test
    public void testSetCoolingCount() {
        // Thay đổi cooling_count và kiểm tra
        deck.setCooling_count(2);
        assertEquals(2, deck.getCooling_count());
        assertNotNull(deck.getLast_update());
    }

    @Test
    public void testTimestampUpdateOnCountChange() throws InterruptedException {
        // Lấy giá trị timestamp ban đầu
        String oldTimestamp = deck.getLast_update();

        // Thêm khoảng trễ trước khi thay đổi giá trị
        Thread.sleep(1000);

        // Thay đổi một giá trị và kiểm tra timestamp mới
        deck.setNew_count(5);
        String newTimestamp = deck.getLast_update();

        // Kiểm tra rằng timestamp đã thay đổi
        assertNotEquals("Timestamp should be different", oldTimestamp, newTimestamp);
    }
}
