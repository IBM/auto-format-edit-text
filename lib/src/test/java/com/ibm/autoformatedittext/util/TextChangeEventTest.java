package com.ibm.autoformatedittext.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextChangeEventTest {
    private TextChangeEvent testTextChangeEvent = new TextChangeEvent("012897", "01234567", 3,2, 4);

    @Test
    public void testGetTextBefore() {
        assertEquals("012897", testTextChangeEvent.getTextBefore());
    }

    @Test
    public void testGetTextAfter() {
        assertEquals("01234567", testTextChangeEvent.getTextAfter());
    }

    @Test
    public void testGetSelectionStart() {
        assertEquals(3, testTextChangeEvent.getSelectionStart());
    }

    @Test
    public void testGetSelectionLength() {
        assertEquals(2, testTextChangeEvent.getSelectionLength());
    }

    @Test
    public void getInsertedLength() {
        assertEquals(4, testTextChangeEvent.getInsertedLength());
    }

    @Test
    public void testGetSelectionEnd() {
        assertEquals(5, testTextChangeEvent.getSelectionEnd());
    }

    @Test
    public void testGetRemovedText() {
        assertEquals("89", testTextChangeEvent.getRemovedText());
    }

    @Test
    public void testGetInsertedText() {
        assertEquals("3456", testTextChangeEvent.getInsertedText());
    }

    @Test
    public void testIsBackspace() {
        //One character is removed but a character is also inserted
        TextChangeEvent textChangeEvent1 = new TextChangeEvent("01284567", "01234567", 3,1, 1);
        assertFalse(textChangeEvent1.isBackspace());

        //No characters are inserted but more then one is removed
        TextChangeEvent textChangeEvent2 = new TextChangeEvent("0128934567", "01234567", 3,2, 0);
        assertFalse(textChangeEvent2.isBackspace());

        //One character is removed and no new characters are inserted, this is a backspace
        TextChangeEvent textChangeEvent3 = new TextChangeEvent("012834567", "01234567", 3,1, 0);
        assertTrue(textChangeEvent3.isBackspace());
    }
}
