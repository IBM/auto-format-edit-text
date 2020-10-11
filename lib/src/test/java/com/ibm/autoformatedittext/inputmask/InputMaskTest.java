package com.ibm.autoformatedittext.inputmask;

import org.junit.*;

import static org.junit.Assert.*;

public class InputMaskTest {
    @Test
    public void testConstructor1() {
        InputMask mask = new InputMask("0", '#');
        assertEquals("0", mask.getInputMaskString());
        assertEquals('#', mask.getPlaceholder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() {
        new InputMask(null, '#');
    }

    @Test
    public void testIsPlaceholder1() {
        InputMask mask = new InputMask("", '#');
        assertFalse(mask.isPlaceholder(-1));
        assertFalse(mask.isPlaceholder(0));
    }

    @Test
    public void testIsPlaceholder2() {
        InputMask mask = new InputMask("/",'#');
        assertFalse(mask.isPlaceholder(-1));
        assertFalse(mask.isPlaceholder(0));
    }

    @Test
    public void testIsPlaceholder3() {
        InputMask mask = new InputMask("#", '#');
        assertFalse(mask.isPlaceholder(-1));
        assertFalse(mask.isPlaceholder(1));
        assertTrue(mask.isPlaceholder(0));
    }

    @Test
    public void testIsPlaceholder4() {
        InputMask mask = new InputMask("/##", '#');
        assertFalse(mask.isPlaceholder(-1));
        assertFalse(mask.isPlaceholder(0));
        assertFalse(mask.isPlaceholder(3));
        assertTrue(mask.isPlaceholder(1));
        assertTrue(mask.isPlaceholder(2));
    }

    @Test
    public void testIsPlaceholder5() {
        InputMask mask = new InputMask("#//", '#');
        assertFalse(mask.isPlaceholder(-1));
        assertFalse(mask.isPlaceholder(1));
        assertFalse(mask.isPlaceholder(2));
        assertTrue(mask.isPlaceholder(0));
    }

    @Test
    public void testIsPlaceholder6() {
        InputMask mask = new InputMask("///", '#');
        assertFalse(mask.isPlaceholder(-1));
        assertFalse(mask.isPlaceholder(0));
        assertFalse(mask.isPlaceholder(1));
        assertFalse(mask.isPlaceholder(2));
        assertFalse(mask.isPlaceholder(3));
    }

    @Test
    public void testIsPlaceholder7() {
        InputMask mask = new InputMask("###", '#');
        assertFalse(mask.isPlaceholder(-1));
        assertFalse(mask.isPlaceholder(3));
        assertTrue(mask.isPlaceholder(0));
        assertTrue(mask.isPlaceholder(1));
        assertTrue(mask.isPlaceholder(2));
    }

    @Test
    public void testIsPlaceholder8() {
        InputMask mask = new InputMask("#//#", '#');
        assertFalse(mask.isPlaceholder(-1));
        assertFalse(mask.isPlaceholder(1));
        assertFalse(mask.isPlaceholder(2));
        assertFalse(mask.isPlaceholder(4));
        assertTrue(mask.isPlaceholder(0));
        assertTrue(mask.isPlaceholder(3));
    }

    @Test
    public void testIsPlaceholder9() {
        InputMask mask = new InputMask("#/##", '#');
        assertFalse(mask.isPlaceholder(-1));
        assertFalse(mask.isPlaceholder(1));
        assertFalse(mask.isPlaceholder(4));
        assertTrue(mask.isPlaceholder(0));
        assertTrue(mask.isPlaceholder(2));
        assertTrue(mask.isPlaceholder(3));
    }

    @Test
    public void testIsPlaceholder10() {
        InputMask mask = new InputMask("#/#/#", '#');
        assertFalse(mask.isPlaceholder(-1));
        assertFalse(mask.isPlaceholder(1));
        assertFalse(mask.isPlaceholder(3));
        assertFalse(mask.isPlaceholder(5));
        assertTrue(mask.isPlaceholder(0));
        assertTrue(mask.isPlaceholder(2));
        assertTrue(mask.isPlaceholder(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatches1() {
        InputMask mask = new InputMask("", '#');
        mask.matches(null);
    }

    @Test
    public void testMatches2() {
        InputMask mask = new InputMask("", '#');
        assertFalse(mask.matches("0"));
        assertTrue(mask.matches(""));
    }

    @Test
    public void testMatches3() {
        InputMask mask = new InputMask("/", '#');
        assertFalse(mask.matches(""));
        assertFalse(mask.matches("0"));
        assertFalse(mask.matches("0/"));
        assertFalse(mask.matches("/0"));
        assertFalse(mask.matches("//"));
        assertTrue(mask.matches("/"));
    }

    @Test
    public void testMatches4() {
        InputMask mask = new InputMask("#", '#');
        assertFalse(mask.matches(""));
        assertFalse(mask.matches("0/"));
        assertFalse(mask.matches("/0"));
        assertFalse(mask.matches("00"));
        assertTrue(mask.matches("0"));
        assertTrue(mask.matches("#"));
    }

    @Test
    public void testMatches5() {
        InputMask mask = new InputMask("/##", '#');
        assertFalse(mask.matches(""));
        assertFalse(mask.matches("/0"));
        assertFalse(mask.matches("/000"));
        assertTrue(mask.matches("/00"));
        assertTrue(mask.matches("/##"));
    }

    @Test
    public void testMatches6() {
        InputMask mask = new InputMask("##/", '#');
        assertFalse(mask.matches(""));
        assertFalse(mask.matches("0/"));
        assertFalse(mask.matches("00"));
        assertFalse(mask.matches("00/0"));
        assertTrue(mask.matches("00/"));
        assertTrue(mask.matches("##/"));
    }

    @Test
    public void testMatches7() {
        InputMask mask = new InputMask("///", '#');
        assertFalse(mask.matches(""));
        assertFalse(mask.matches("000"));
        assertFalse(mask.matches("//"));
        assertFalse(mask.matches("///0"));
        assertTrue(mask.matches("///"));
    }

    @Test
    public void testMatches8() {
        InputMask mask = new InputMask("###", '#');
        assertFalse(mask.matches(""));
        assertFalse(mask.matches("00"));
        assertFalse(mask.matches("0000"));
        assertTrue(mask.matches("000"));
        assertTrue(mask.matches("###"));
    }

    @Test
    public void testMatches9() {
        InputMask mask = new InputMask("#//#", '#');
        assertFalse(mask.matches(""));
        assertFalse(mask.matches("0//"));
        assertFalse(mask.matches("//0"));
        assertFalse(mask.matches("0000"));
        assertFalse(mask.matches("0//00"));
        assertTrue(mask.matches("0//0"));
        assertTrue(mask.matches("#//#"));
    }

    @Test
    public void testMatches10() {
        InputMask mask = new InputMask("#/##", '#');
        assertFalse(mask.matches(""));
        assertFalse(mask.matches("0/0"));
        assertFalse(mask.matches("/00"));
        assertFalse(mask.matches("0000"));
        assertFalse(mask.matches("0/000"));
        assertFalse(mask.matches("0/00/"));
        assertTrue(mask.matches("0/00"));
        assertTrue(mask.matches("#/##"));
    }

    @Test
    public void testMatches11() {
        InputMask mask = new InputMask("#/#/#", '#');
        assertFalse(mask.matches(""));
        assertFalse(mask.matches("0/0/"));
        assertFalse(mask.matches("/0/0"));
        assertFalse(mask.matches("00000"));
        assertFalse(mask.matches("0/0/00"));
        assertFalse(mask.matches("0/0/0/"));
        assertTrue(mask.matches("0/0/0"));
        assertTrue(mask.matches("#/#/#"));
    }

    @Test
    public void testGetUnformattedLength1() {
        InputMask mask = new InputMask("", '#');
        assertEquals(0, mask.getUnformattedLength());
    }

    @Test
    public void testGetUnformattedLength2() {
        InputMask mask = new InputMask("/", '#');
        assertEquals(0, mask.getUnformattedLength());
    }

    @Test
    public void testGetUnformattedLength3() {
        InputMask mask = new InputMask("#", '#');
        assertEquals(1, mask.getUnformattedLength());
    }

    @Test
    public void testGetUnformattedLength4() {
        InputMask mask = new InputMask("/##", '#');
        assertEquals(2, mask.getUnformattedLength());
    }

    @Test
    public void testGetUnformattedLength5() {
        InputMask mask = new InputMask("##/", '#');
        assertEquals(2, mask.getUnformattedLength());
    }

    @Test
    public void testGetUnformattedLength6() {
        InputMask mask = new InputMask("///", '#');
        assertEquals(0, mask.getUnformattedLength());
    }

    @Test
    public void testGetUnformattedLength7() {
        InputMask mask = new InputMask("###", '#');
        assertEquals(3, mask.getUnformattedLength());
    }

    @Test
    public void testGetUnformattedLength8() {
        InputMask mask = new InputMask("#//#", '#');
        assertEquals(2, mask.getUnformattedLength());
    }

    @Test
    public void testGetUnformattedLength9() {
        InputMask mask = new InputMask("#/##", '#');
        assertEquals(3, mask.getUnformattedLength());
    }

    @Test
    public void testGetUnformattedLength10() {
        InputMask mask = new InputMask("#/#/#", '#');
        assertEquals(3, mask.getUnformattedLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatText1() {
        InputMask mask = new InputMask("", '#');
        mask.formatText(null);
    }

    @Test
    public void testFormatText2() {
        InputMask mask = new InputMask("", '#');
        assertEquals("", mask.formatText(""));
        assertEquals("", mask.formatText("0"));
    }

    @Test
    public void testFormatText3() {
        InputMask mask = new InputMask("/", '#');
        assertEquals("", mask.formatText(""));
        assertEquals("/", mask.formatText("0"));
        assertEquals("/", mask.formatText("01"));
    }

    @Test
    public void testFormatText4() {
        InputMask mask = new InputMask("#", '#');
        assertEquals("", mask.formatText(""));
        assertEquals("0", mask.formatText("0"));
        assertEquals("0", mask.formatText("01"));
    }

    @Test
    public void testFormatText5() {
        InputMask mask = new InputMask("/##", '#');
        assertEquals("", mask.formatText(""));
        assertEquals("/0", mask.formatText("0"));
        assertEquals("/01", mask.formatText("01"));
        assertEquals("/01", mask.formatText("012"));
    }

    @Test
    public void testFormatText6() {
        InputMask mask = new InputMask("##/", '#');
        assertEquals("", mask.formatText(""));
        assertEquals("0", mask.formatText("0"));
        assertEquals("01/", mask.formatText("01"));
        assertEquals("01/", mask.formatText("012"));
    }

    @Test
    public void testFormatText7() {
        InputMask mask = new InputMask("///", '#');
        assertEquals("", mask.formatText(""));
        assertEquals("///", mask.formatText("0"));
        assertEquals("///", mask.formatText("01"));
        assertEquals("///", mask.formatText("012"));
        assertEquals("///", mask.formatText("0123"));
    }

    @Test
    public void testFormatText8() {
        InputMask mask = new InputMask("###", '#');
        assertEquals("", mask.formatText(""));
        assertEquals("0", mask.formatText("0"));
        assertEquals("01", mask.formatText("01"));
        assertEquals("012", mask.formatText("012"));
        assertEquals("012", mask.formatText("0123"));
    }

    @Test
    public void testFormatText9() {
        InputMask mask = new InputMask("#//#", '#');
        assertEquals("", mask.formatText(""));
        assertEquals("0//", mask.formatText("0"));
        assertEquals("0//1", mask.formatText("01"));
        assertEquals("0//1", mask.formatText("012"));
    }

    @Test
    public void testFormatText10() {
        InputMask mask = new InputMask("#/##", '#');
        assertEquals("", mask.formatText(""));
        assertEquals("0/", mask.formatText("0"));
        assertEquals("0/1", mask.formatText("01"));
        assertEquals("0/12", mask.formatText("012"));
        assertEquals("0/12", mask.formatText("0123"));
    }

    @Test
    public void testFormatText11() {
        InputMask mask = new InputMask("#/#/#", '#');
        assertEquals("", mask.formatText(""));
        assertEquals("0/", mask.formatText("0"));
        assertEquals("0/1/", mask.formatText("01"));
        assertEquals("0/1/2", mask.formatText("012"));
        assertEquals("0/1/2", mask.formatText("0123"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnformatText1() {
        InputMask mask = new InputMask("", '#');
        mask.unformatText(null, 0, 0);
    }

    @Test
    public void testUnformatText2() {
        InputMask mask = new InputMask("", '#');
        assertEquals("", mask.unformatText("", 0, 0));
    }

    @Test
    public void testUnformatText3() {
        InputMask mask = new InputMask("/", '#');
        assertEquals("", mask.unformatText("/", 0, 0));
        assertEquals("", mask.unformatText("/", 0, 1));
    }

    @Test
    public void testUnformatText4() {
        InputMask mask = new InputMask("#", '#');
        assertEquals("", mask.unformatText("0", 0, 0));
        assertEquals("0", mask.unformatText("0", 0, 1));
    }

    @Test
    public void testUnformatText5() {
        InputMask mask = new InputMask("/##", '#');
        assertEquals("", mask.unformatText("/01", 0, 0));
        assertEquals("", mask.unformatText("/01", 0, 1));
        assertEquals("0", mask.unformatText("/01", 0, 2));
        assertEquals("01", mask.unformatText("/01", 0, 3));

        assertEquals("0", mask.unformatText("/01", 1, 2));
        assertEquals("01", mask.unformatText("/01", 1, 3));

        assertEquals("1", mask.unformatText("/01", 2, 3));
    }

    @Test
    public void testUnformatText6() {
        InputMask mask = new InputMask("##/", '#');
        assertEquals("", mask.unformatText("01/", 0, 0));
        assertEquals("0", mask.unformatText("01/", 0, 1));
        assertEquals("01", mask.unformatText("01/", 0, 2));
        assertEquals("01", mask.unformatText("01/", 0, 3));

        assertEquals("1", mask.unformatText("01/", 1, 2));
        assertEquals("1", mask.unformatText("01/", 1, 3));

        assertEquals("", mask.unformatText("01/", 2, 3));
    }

    @Test
    public void testUnformatText7() {
        InputMask mask = new InputMask("///", '#');
        assertEquals("", mask.unformatText("///", 0, 0));
        assertEquals("", mask.unformatText("///", 0, 1));
        assertEquals("", mask.unformatText("///", 0, 2));
        assertEquals("", mask.unformatText("///", 0, 3));

        assertEquals("", mask.unformatText("///", 1, 2));
        assertEquals("", mask.unformatText("///", 1, 3));

        assertEquals("", mask.unformatText("///", 2, 3));
    }

    @Test
    public void testUnformatText8() {
        InputMask mask = new InputMask("###", '#');
        assertEquals("", mask.unformatText("012", 0, 0));
        assertEquals("0", mask.unformatText("012", 0, 1));
        assertEquals("01", mask.unformatText("012", 0, 2));
        assertEquals("012", mask.unformatText("012", 0, 3));

        assertEquals("1", mask.unformatText("012", 1, 2));
        assertEquals("12", mask.unformatText("012", 1, 3));

        assertEquals("2", mask.unformatText("012", 2, 3));
    }

    @Test
    public void testUnformatText9() {
        InputMask mask = new InputMask("#//#", '#');
        assertEquals("", mask.unformatText("0//1", 0, 0));
        assertEquals("0", mask.unformatText("0//1", 0, 1));
        assertEquals("0", mask.unformatText("0//1", 0, 2));
        assertEquals("0", mask.unformatText("0//1", 0, 3));
        assertEquals("01", mask.unformatText("0//1", 0, 4));

        assertEquals("", mask.unformatText("0//1", 1, 2));
        assertEquals("", mask.unformatText("0//1", 1, 3));
        assertEquals("1", mask.unformatText("0//1", 1, 4));

        assertEquals("", mask.unformatText("0//1", 2, 3));
        assertEquals("1", mask.unformatText("0//1", 2, 4));

        assertEquals("1", mask.unformatText("0//1", 3, 4));
    }

    @Test
    public void testUnformatText10() {
        InputMask mask = new InputMask("#/##", '#');
        assertEquals("", mask.unformatText("0/12", 0, 0));
        assertEquals("0", mask.unformatText("0/12", 0, 1));
        assertEquals("0", mask.unformatText("0/12", 0, 2));
        assertEquals("01", mask.unformatText("0/12", 0, 3));
        assertEquals("012", mask.unformatText("0/12", 0, 4));

        assertEquals("", mask.unformatText("0/12", 1, 2));
        assertEquals("1", mask.unformatText("0/12", 1, 3));
        assertEquals("12", mask.unformatText("0/12", 1, 4));

        assertEquals("1", mask.unformatText("0/12", 2, 3));
        assertEquals("12", mask.unformatText("0/12", 2, 4));

        assertEquals("2", mask.unformatText("0/12", 3, 4));
    }

    @Test
    public void testUnformatText11() {
        InputMask mask = new InputMask("#/#/#", '#');
        assertEquals("", mask.unformatText("0/1/2", 0, 0));
        assertEquals("0", mask.unformatText("0/1/2", 0, 1));
        assertEquals("0", mask.unformatText("0/1/2", 0, 2));
        assertEquals("01", mask.unformatText("0/1/2", 0, 3));
        assertEquals("01", mask.unformatText("0/1/2", 0, 4));
        assertEquals("012", mask.unformatText("0/1/2", 0, 5));

        assertEquals("", mask.unformatText("0/1/2", 1, 2));
        assertEquals("1", mask.unformatText("0/1/2", 1, 3));
        assertEquals("1", mask.unformatText("0/1/2", 1, 4));
        assertEquals("12", mask.unformatText("0/1/2", 1, 5));

        assertEquals("1", mask.unformatText("0/1/2", 2, 3));
        assertEquals("1", mask.unformatText("0/1/2", 2, 4));
        assertEquals("12", mask.unformatText("0/1/2", 2, 5));

        assertEquals("", mask.unformatText("0/1/2", 3, 4));
        assertEquals("2", mask.unformatText("0/1/2", 3, 5));

        assertEquals("2", mask.unformatText("0/1/2", 4, 5));
    }

    @Test
    public void testUnformatText12() {
        InputMask mask = new InputMask("#/#/#", '#');
        assertEquals("024", mask.unformatText("01234", 0, 5));
        assertEquals("024", mask.unformatText("012345", 0, 5));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUnformatText13() {
        InputMask mask = new InputMask("#/#/#", '#');
        mask.unformatText("", 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUnformatText14() {
        InputMask mask = new InputMask("#", '#');
        mask.unformatText("0/1/2", 0, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUnformatText15() {
        InputMask mask = new InputMask("#", '#');
        mask.unformatText("0/1/2", -1, 0);
    }

    @Test
    public void testGetInputMaskString() {
        InputMask mask = new InputMask("##-##", '#');
        assertEquals("##-##", mask.getInputMaskString());
    }

    @Test
    public void testSetInputMaskString1() {
        InputMask mask = new InputMask("", '#');
        mask.setInputMaskString("###");
        assertEquals("###", mask.getInputMaskString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInputMaskString2() {
        InputMask mask = new InputMask("", '#');
        mask.setInputMaskString(null);
    }

    @Test
    public void testGetPlaceholder() {
        InputMask mask = new InputMask("#", '#');
        assertEquals('#', mask.getPlaceholder());
    }

    @Test
    public void testSetPlaceholder() {
        InputMask mask = new InputMask("", '#');
        mask.setPlaceholder('*');
        assertEquals('*', mask.getPlaceholder());
    }
}