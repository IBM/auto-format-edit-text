package com.ibm.autoformatedittext.util;

import com.ibm.autoformatedittext.model.EditTextState;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleMaskFilterTest {
    @Test
    public void filterTest() {
        SimpleMaskFilter maskFilter = new SimpleMaskFilter("##/##/##", "#", true);

        //Insert at beginning
        EditTextState newEditTextState = maskFilter.filter("ab/cd/", "efab/cd/", 0, 0, 2);
        assertEquals("efabcd", newEditTextState.getUnformattedText());
        assertEquals("ef/ab/cd", newEditTextState.getFormattedText());
        assertEquals(3, newEditTextState.getCursorStart());
        assertEquals(3, newEditTextState.getCursorEnd());

        //Insert at end
        newEditTextState = maskFilter.filter("ab/cd/", "ab/cd/ef", 6, 6, 2);
        assertEquals("abcdef", newEditTextState.getUnformattedText());
        assertEquals("ab/cd/ef", newEditTextState.getFormattedText());
        assertEquals(8, newEditTextState.getCursorStart());
        assertEquals(8, newEditTextState.getCursorEnd());




        //Insert in front of placeholder
        //Insert in front of non-placeholder

        //Replace at beginning
        //Replace at end
        //Replace in front of placeholder
        //Replace through placeholder
        //Replace in front of non-placeholder
        //Replace all
    }

    @Test
    public void filterTest_placeholders() {
        //Set valid placeholder
        //Set null placeholder
        //Set empty placeholder
        //Set placeholder with improper length
    }

    @Test
    public void filterTest_noMask() {
        //Null mask
        //Empty mask
    }

    @Test
    public void filterTest_outsideBounds() {
        //Replace smaller length section while complete, outside bounds
        //Insert one while complete, outside bounds
        //Insert many while complete, outside bounds
        //Insert many at start while complete, outside bounds
        //Insert many at end while complete, outside bounds
    }

    @Test
    public void filterTest_backspaceNonShift() {
        //Backspaced in front of placeholder, non-shiftMode
        //Backspaced in front of non-placeholder, non-shiftMode
        //Backspaced in front of two placeholders, non-shiftMode
    }

    @Test
    public void filterTest_backspaceShiftMode() {
        //Backspaced in front of placeholder, shiftMode
        //Backspaced in front of non-placeholder, shiftMode
        //Backspaced in front of two placeholders, shiftMode
    }
}
