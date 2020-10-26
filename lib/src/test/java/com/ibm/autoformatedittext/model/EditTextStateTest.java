package com.ibm.autoformatedittext.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class EditTextStateTest {
    private EditTextState testEditTextState = new EditTextState("07/19/1993", "07191993", 0, 5);

    @Test
    public void testConstructor_noRange() {
        //Test constructor with no selection range
        EditTextState editTextState1 = new EditTextState("07/19/1993", "07191993", 0);
        assertEquals(0, editTextState1.getSelectionStart());
        assertEquals(0, editTextState1.getSelectionEnd());
    }

    @Test
    public void testGetFormattedText() {
        assertEquals("07/19/1993", testEditTextState.getFormattedText());
    }

    @Test
    public void testGetUnformattedText() {
        assertEquals("07191993", testEditTextState.getUnformattedText());
    }

    @Test
    public void testGetSelectionStart() {
        assertEquals(0, testEditTextState.getSelectionStart());
    }

    @Test
    public void testGetSelectionEnd() {
        assertEquals(5, testEditTextState.getSelectionEnd());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(-1157693412, testEditTextState.hashCode());

        EditTextState editTextState1 = new EditTextState("", "", 0, 0);
        assertNotEquals(testEditTextState.hashCode(), editTextState1.hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(testEditTextState, testEditTextState);

        //Testing instanceof with assertEquals will not actually call the equals method
        //noinspection SimplifiableJUnitAssertion,EqualsBetweenInconvertibleTypes
        assertFalse(testEditTextState.equals(""));

        EditTextState editTextState1 = new EditTextState("07/19/1993", "07191993", 0, 5);
        assertEquals(testEditTextState, editTextState1);

        //Different formatted text
        EditTextState editTextState2 = new EditTextState("", "07191993", 0, 5);
        assertNotEquals(testEditTextState, editTextState2);

        //Different unformatted text
        EditTextState editTextState3 = new EditTextState("07/19/1993", "", 0, 5);
        assertNotEquals(testEditTextState, editTextState3);

        //Different selection start
        EditTextState editTextState4 = new EditTextState("07/19/1993", "07191993", 5, 5);
        assertNotEquals(testEditTextState, editTextState4);

        //Different selection end
        EditTextState editTextState5 = new EditTextState("07/19/1993", "07191993", 0, 0);
        assertNotEquals(testEditTextState, editTextState5);
    }
}