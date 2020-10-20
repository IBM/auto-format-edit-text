package com.ibm.autoformatedittext.model;

import com.ibm.autoformatedittext.util.HideFormatter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HideFormatterTest {
    private void formatAndAssertEquals(String expectedValue, String input, String format) {
        String formattedText = HideFormatter.format(input, format);
        assertEquals(expectedValue, formattedText);
    }

    @Test
    public void testFormat_nullCases() {
        String formattedText = HideFormatter.format(null, null);
        assertNull(formattedText);

        formattedText = HideFormatter.format("", null);
        assertNull(formattedText);

        formattedText = HideFormatter.format(null, "[0][1][2]***");
        assertEquals("[0][1][2]***", formattedText);
    }

    @Test
    public void testFormat_placeholderLocation() {
        formatAndAssertEquals("a", "abcdefg", "[0]");
        formatAndAssertEquals("a***", "abcdefg", "[0]***");
        formatAndAssertEquals("ab***", "abcdefg", "[0][1]***");
        formatAndAssertEquals("**abc*", "abcdefg", "**[0][1][2]*");
        formatAndAssertEquals("***abcd", "abcdefg", "***[0][1][2][3]");
        formatAndAssertEquals("***a", "abcdefg", "***[0]");
        formatAndAssertEquals("abcdefg", "abcdefg", "[0][1][2][3][4][5][6]");
    }

    @Test
    public void testFormat_alternateSequencesAndRanges() {
        formatAndAssertEquals("***defg", "abcdefg", "***[3][4][5][6]");
        formatAndAssertEquals("**efg*", "abcdefg", "**[4][5][6]*");
        formatAndAssertEquals("***defg", "abcdefg", "***[3-6]");
        formatAndAssertEquals("defg***", "abcdefg", "[3-6]***");
        formatAndAssertEquals("**fb*de", "abcdefg", "**[5][1]*[3][4]");
        formatAndAssertEquals("fbdeagc", "abcdefg", "[5][1][3][4][0][6][2]");
        formatAndAssertEquals("abcdefg", "abcdefg", "[0-6]");
        formatAndAssertEquals("c", "abcdefg", "[2-2]");
    }

    @Test
    public void testFormat_noPatternMatch() {
        formatAndAssertEquals("", "abcdefg", "");
        formatAndAssertEquals(" ", "abcdefg", " ");
        formatAndAssertEquals("", "abcdefg", "");
        formatAndAssertEquals("***3456", "abcdefg", "***3456");
        formatAndAssertEquals("***[n][n][n][n]", "abcdefg", "***[n][n][n][n]");
        formatAndAssertEquals("**[7-*", "abcdefg", "**[7-*");
        formatAndAssertEquals("**[7-]*", "abcdefg", "**[7-]*");
        formatAndAssertEquals("***d[4-a]fg", "abcdefg", "***[3][4-a][5][6]");
        formatAndAssertEquals("***d[]fg", "abcdefg", "***[3][][5][6]");
    }

    @Test
    public void testFormat_patternMatchButInvalidRange() {
        formatAndAssertEquals("", "abcdefg", "[4-0]");
        formatAndAssertEquals("***", "abcdefg", "*[6-3]**");
        formatAndAssertEquals("*a**", "abcdefg", "*[0][0-7]**");
        formatAndAssertEquals("**dfg*", "abcdefg", "**[3][4-0][5][6]*");
    }

    @Test
    public void testFormat_symbols() {
        formatAndAssertEquals("a***", "a[b-@]4", "[0]***");
        formatAndAssertEquals("a[***", "a[b-@]4", "[0][1]***");
        formatAndAssertEquals("*a[b**", "a[b-@]4", "*[0][1][2]**");
        formatAndAssertEquals("***a[b-", "a[b-@]4", "***[0][1][2][3]");
        formatAndAssertEquals("***]", "a[b-@]4", "***[5]");
        formatAndAssertEquals("a[b-@]4", "a[b-@]4", "[0][1][2][3][4][5][6]");
    }
}