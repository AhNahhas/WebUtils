package com.anahhas.webutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;

import com.anahhas.helpers.TestHelpers;

public class StreamUtilsTest {

    @Test
    public void shouldReturnAnyElement() {

        List<Integer> collection = TestHelpers.getListOfInt();
        assertNotNull(StreamUtils.anyElement(collection));

    }

    @Test
    public void shouldMapAndReturnAnyElement() {

        List<Integer> collection = TestHelpers.getListOfInt();
        assertNotNull(StreamUtils.anyElement(collection, String::valueOf));

    }

    @Test
    public void shouldMapAndReturnOnlyElement() {

        List<Integer> collection = TestHelpers.getListOfInt(1);
        String str = StreamUtils.anyElement(collection, String::valueOf);
        assertEquals("1", str);

    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionForEmptyCollection() {

        StreamUtils.anyElementOrThrow(List.of(), String::valueOf);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowCustomExceptionForEmptyCollection() {

        StreamUtils.anyElementOrThrow(List.of(), String::valueOf, () -> new IllegalArgumentException());

    }

}
