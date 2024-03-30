package com.anahhas.webutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringUtilsTest {
    
    @Test
    public void shouldReturnZeroLengthForNullString() {
        assertEquals(0, StringUtils.length(null));
    }

    @Test
    public void shouldReturnZeroLengthForEmptyString() {
        assertEquals(0, StringUtils.length(""));
    }

    @Test
    public void shouldUpperCaseString() {
        assertEquals("ABC123;@.", StringUtils.upperCase("abc123;@."));
    }

    @Test
    public void shoulLowerCaseString() {
        assertEquals("abc123;@.", StringUtils.lowerCase("ABC123;@."));
    }

    @Test
    public void shouldAssertBlankForNullString() {
        assertTrue(StringUtils.isBlank(null));
        assertFalse(StringUtils.isNotBlank(null));
    }

    @Test
    public void shouldAssertBlankForEmptyString() {
        assertTrue(StringUtils.isBlank(""));
        assertFalse(StringUtils.isNotBlank(""));
    }

    @Test
    public void shouldAssertBlankWhiteSpaceString() {
        assertTrue(StringUtils.isBlank("  "));
        assertFalse(StringUtils.isNotBlank("  "));
    }

    @Test
    public void shouldAssertContainedString() {
        assertTrue(StringUtils.contains("OneTwoThree", "oTh"));
        assertFalse(StringUtils.contains("OneTwoThree", "oThx"));
        assertTrue(StringUtils.contains("OneTwoThree", ""));
        assertFalse(StringUtils.contains("OneTwoThree", null));
        assertFalse(StringUtils.contains("OneTwoThree", " "));
        assertTrue(StringUtils.contains("OneTwoThree", "OneTwoThree"));
        assertFalse(StringUtils.contains("OneTwoThree", "OneTwoThree "));
        assertTrue(StringUtils.contains("OneTwoThree ", " "));
        assertFalse(StringUtils.contains("OneTwoThree ", "  "));
        assertTrue(StringUtils.contains("OneTwoThree123", "ree123"));
        assertFalse(StringUtils.contains("OneTwoThree", "ree123 "));
        assertFalse(StringUtils.contains(null, null));
    }

    @Test
    public void shouldAssertContainedCaseInsensitiveString() {

        //should pass all previous
        assertTrue(StringUtils.containsIgnoreCase("OneTwoThree", "oTh"));
        assertFalse(StringUtils.containsIgnoreCase("OneTwoThree", "oThx"));
        assertTrue(StringUtils.containsIgnoreCase("OneTwoThree", ""));
        assertFalse(StringUtils.containsIgnoreCase("OneTwoThree", null));
        assertFalse(StringUtils.containsIgnoreCase("OneTwoThree", " "));
        assertTrue(StringUtils.containsIgnoreCase("OneTwoThree", "OneTwoThree"));
        assertFalse(StringUtils.containsIgnoreCase("OneTwoThree", "OneTwoThree "));
        assertTrue(StringUtils.containsIgnoreCase("OneTwoThree ", " "));
        assertFalse(StringUtils.containsIgnoreCase("OneTwoThree ", "  "));

        //case insensitive
        assertTrue(StringUtils.containsIgnoreCase("OneTwoThree", "oth"));
        assertTrue(StringUtils.containsIgnoreCase("onetwothree", "oTh"));
        assertFalse(StringUtils.containsIgnoreCase("OneTwoThree", "othx"));
        assertTrue(StringUtils.containsIgnoreCase("OneTwoThree", "onetwothree"));
        assertTrue(StringUtils.containsIgnoreCase("onetwothree", "OneTwoThree"));
        assertFalse(StringUtils.containsIgnoreCase("OneTwoThree", "onetwothree "));
        assertTrue(StringUtils.containsIgnoreCase("AbC123", "abc123"));
        assertTrue(StringUtils.containsIgnoreCase("abc123", "AbC123"));
        assertFalse(StringUtils.contains(null, null));

    }

    @Test
    public void shouldConcatNonNullStrings() {
        assertEquals("ABC123def", StringUtils.concat(null, "ABC", "123", null, "def"));
    }

    @Test
    public void shouldConcatNonNullStringsUsingDelimiter() {
        assertEquals("ABC;123;def", StringUtils.join(";", "ABC", "123", null, "def"));
    }

    @Test
    public void shouldConcatNonBlankStringsUsingDelimiter() {
        assertEquals("ABC;123;def",
            StringUtils.joinIgnoreBlank(";", "ABC", "123", null, "", " ", "def"));
    }

    @Test
    public void shouldFilterAndConcatStringsUsingDelimiter() {
        assertEquals("ABC#AEF#AGH", StringUtils.joinFiltered("#", s -> s.charAt(0) == 'A', 
            "ABC", "123", "AEF", "AGH", "def", "abcd"));   
    }

    @Test
    public void shouldCompareSameLengthCharSequences() {
        assertEquals(0, StringUtils.compare("abc", new String("abc")));
        assertEquals(0, StringUtils.compare(new StringBuilder("abc"), new StringBuilder("abc")));
    }

    @Test
    public void shouldCompareEmptyAndNullCharSequences() {
        assertEquals(0, StringUtils.compare(null, null));
        assertEquals(0, StringUtils.compare("", ""));
        assertTrue(StringUtils.compare("", null) > 0);
        assertTrue(StringUtils.compare(null, "") < 0);
    }

    @Test
    public void shouldCompareDifferentLengthStrings() {
        assertTrue(StringUtils.compare("abcd", new String("abc")) > 0);
        assertTrue(StringUtils.compare("abcd", new String("abcde")) < 0);
        assertTrue(StringUtils.compare("abcd", new String("")) > 0);
        assertTrue(StringUtils.compare("", new String("abcd")) < 0);
    }

}
