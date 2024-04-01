package com.anahhas.webutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;

public class OptionalUtilsTest {
    
    @Test
    public void shouldReturnNullForNullString() {

        String result = OptionalUtils.ofBlank(null);
        assertNull(result);

    }

    @Test
    public void shouldReturnNullForEmptyString() {

        String result = OptionalUtils.ofBlank("");
        assertNull(result);

    }

    @Test
    public void shouldReturnNullForWhiteSpaceString() {

        String result = OptionalUtils.ofBlank(" ");
        assertNull(result);

    }

    @Test
    public void shouldReturnDefaultForNullString() {

        String result = OptionalUtils.orDefault(OptionalUtils.ofBlank(null), () -> "default");
        assertEquals("default", result);

    }
    
    @Test
    public void shouldReturnDefaultForEmptyString() {

        String result = OptionalUtils.orDefault(OptionalUtils.ofBlank(""), () -> "default");
        assertEquals("default", result);

    }

    @Test
    public void shouldReturnDefaultForWhiteSpaceString() {

        String result = OptionalUtils.orDefault(OptionalUtils.ofBlank("  "), () -> "default");
        assertEquals("default", result);

    }

    @Test(expected = NoSuchElementException.class)
    public void shouldReturnThrowExceptionForNullString() {
        OptionalUtils.orThrow(OptionalUtils.ofBlank(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnThrowCustomExceptionForNullString() {
        OptionalUtils.orThrow(OptionalUtils.ofBlank(null), () -> new IllegalArgumentException());
    }

    @Test
    public void shouldMapNonNullElement() {

        double result = OptionalUtils.ofMappable(1L, Number::doubleValue);
        assertEquals(1.0, result, 0);

    }

    @Test
    public void shouldMapNonNullString() {

        String result = OptionalUtils.ofMappable("abc", String::toUpperCase);
        assertEquals("ABC", result);

    }

    @Test
    public void shouldNotMapNullElement() {

        Double result = OptionalUtils.ofMappable(null, Number::doubleValue);
        assertNull(result);

    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionForNullMapable() {
        OptionalUtils.orThrow(OptionalUtils.ofMappable(null, Number::doubleValue));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowCustomExceptionForNullMapableElement() {
        OptionalUtils.orThrow(OptionalUtils.ofMappable(null, Number::doubleValue), 
            () -> new IllegalArgumentException());
    }

    @Test
    public void shouldReturnDefaultForNullMapable() {

        String result = OptionalUtils.orDefault(OptionalUtils.ofMappable((String) null, String::toUpperCase), 
            () -> "default");

        assertEquals("default", result);

    }

    @Test
    public void shouldMergeNonEmptyOptionals() {

        String result = OptionalUtils.merge(Optional.of("One"), Optional.of("Two"), String::concat);
        assertEquals("OneTwo", result);

    }

    @Test
    public void shouldApplyEquationOnNonEmptyOptionals() {

        Integer result = OptionalUtils.merge(Optional.of(3), Optional.of(2), (a, b) -> 5 * a - b);
        assertEquals(13, result.intValue());

    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionForEmptyOptionalMerger() {

        OptionalUtils.orThrow(OptionalUtils.merge(Optional.<Integer>empty(), Optional.of(2), (a, b) -> 5 * a - b));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowCustomExceptionForEmptyOptionalMerger() {

        OptionalUtils.orThrow(OptionalUtils.merge(Optional.<Integer>empty(), Optional.of(2), 
            (a, b) -> 5 * a - b), () -> new IllegalArgumentException());

    }

    @Test()
    public void shouldReturnDefaultForEmptyOptionalMerger() {

        Integer result = OptionalUtils.orDefault(OptionalUtils.merge(Optional.<Integer>empty(), Optional.of(2), 
            (a, b) -> 5 * a - b), () -> 1);
        
        assertEquals(1, result.intValue());

    }

}
