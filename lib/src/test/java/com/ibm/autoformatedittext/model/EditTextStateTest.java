package com.ibm.autoformatedittext.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EditTextStateTest {
    @Test
    public void constructorTest() {
        EditTextState editTextState = new EditTextState("07/19/1993", "07191993", 0, 5);
        assertEquals("07/19/1993", editTextState.getFormattedText());
        assertEquals("07191993", editTextState.getUnformattedText());
        assertEquals(0, editTextState.getCursorStart());
        assertEquals(5, editTextState.getCursorEnd());
    }

    @Test
    public void constructorTest_noRange() {
        EditTextState editTextState = new EditTextState("07/19/1993", "07191993", 0);
        assertEquals("07/19/1993", editTextState.getFormattedText());
        assertEquals("07191993", editTextState.getUnformattedText());
        assertEquals(0, editTextState.getCursorStart());
        assertEquals(0, editTextState.getCursorEnd());
    }
}