package com.example.infi;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.infi.entity.Deck;

public class DeckTest {

    private Deck deck;

    @Before
    public void setUp() {
        // Khởi tạo đối tượng Deck
        deck = new Deck("deck1::deck2::deck3");
    }

    @Test
    public void testConstructor() {
        // Kiểm tra các giá trị sau khi khởi tạo
        assertEquals("deck3", deck.getDeck_name());
        assertEquals("deck1::deck2", deck.getParent_deck_path());
        assertEquals("deck1::deck2::deck3", deck.getDeck_path());
    }

    @Test
    public void testSetDeckName() {
        // Thay đổi deck_name và kiểm tra deck_path
        deck.setDeck_name("deck4");
        assertEquals("deck4", deck.getDeck_name());
        assertEquals("deck1::deck2", deck.getParent_deck_path());
        assertEquals("deck1::deck2::deck4", deck.getDeck_path());
    }

    @Test
    public void testSetDeckPath_withNewParent() {
        // Thay đổi deck_path và kiểm tra deck_name và parent_deck_path
        deck.setDeck_path("deck1::deck4::deck5");
        assertEquals("deck5", deck.getDeck_name());
        assertEquals("deck1::deck4", deck.getParent_deck_path());
    }

    @Test
    public void testSetDeckPath_withoutParent() {
        // Thay đổi deck_path khi không có parent_deck_path
        deck.setDeck_path("deck6");
        assertEquals("deck6", deck.getDeck_name());
        assertNull(deck.getParent_deck_path());
    }

    @Test
    public void testTimestampUpdateOnDeckPathChange() throws InterruptedException {
        Deck deck = new Deck("deck1::deck2");
        String oldTimestamp = deck.getLast_update();

        // Thêm khoảng trễ trước khi thay đổi
        Thread.sleep(1000);

        deck.setDeck_path("deck1::deck3");
        String newTimestamp = deck.getLast_update();

        // Kiểm tra rằng timestamp đã thay đổi
        assertNotEquals("Values should be different", oldTimestamp, newTimestamp);
    }


}
