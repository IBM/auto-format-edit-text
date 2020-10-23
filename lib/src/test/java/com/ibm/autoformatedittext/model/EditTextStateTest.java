package com.ibm.autoformatedittext.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class EditTextStateTest {
    private EditTextState testEditTextState = new EditTextState("07/19/1993", "07191993", 0, 5);

    @Test
    public void constructorTest_noRange() {
        EditTextState testEditTextStateNoRange = new EditTextState("07/19/1993", "07191993", 0);
        assertEquals(0, testEditTextStateNoRange.getSelectionStart());
        assertEquals(0, testEditTextStateNoRange.getSelectionEnd());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(-1157693412, testEditTextState.hashCode());

        EditTextState testEditTextState2 = new EditTextState("", "", 0, 0);
        assertNotEquals(testEditTextState.hashCode(), testEditTextState2.hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(testEditTextState, testEditTextState);

        //Testing instanceof with assertEquals will not actually call the equals method
        //noinspection SimplifiableJUnitAssertion,EqualsBetweenInconvertibleTypes
        assertFalse(testEditTextState.equals(""));

        EditTextState testEditTextStateIdentical = new EditTextState("07/19/1993", "07191993", 0, 5);
        assertEquals(testEditTextState, testEditTextStateIdentical);

        EditTextState testEditTextStateDifferentFormatted = new EditTextState("", "07191993", 0, 5);
        assertNotEquals(testEditTextState, testEditTextStateDifferentFormatted);

        EditTextState testEditTextStateDifferentUnformatted = new EditTextState("07/19/1993", "", 0, 5);
        assertNotEquals(testEditTextState, testEditTextStateDifferentUnformatted);

        EditTextState testEditTextStateDifferentStart = new EditTextState("07/19/1993", "07191993", 5, 5);
        assertNotEquals(testEditTextState, testEditTextStateDifferentStart);

        EditTextState testEditTextStateDifferentEnd = new EditTextState("07/19/1993", "07191993", 0, 0);
        assertNotEquals(testEditTextState, testEditTextStateDifferentEnd);
    }
}