package io.github.ahnahhas.webutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import io.github.ahnahhas.webutils.helpers.TestHelpers;

public class StreamUtilsTest {

    @Test
    public void shouldReturnAnyElement() {

        List<Integer> collection = TestHelpers.getListOfInt();
        assertNotNull(StreamUtils.anyElement(collection.stream()));

    }

    @Test
    public void shouldCollectionStreamElement() {

        List<String> list = TestHelpers.getListOfString();
        Set<String> set = StreamUtils.toCollection(list.stream(), HashSet::new);

        assertEquals(3, set.size());
        assertTrue(set.contains("ABC"));
        assertTrue(set.contains("abc"));
        assertTrue(set.contains("123"));

    }

    @Test
    public void shouldFilterDuplicateFromStream() {

        List<String> list = TestHelpers.getListOfString(null, "ABC", "ABC", "123", "EDF", "123", null);

        List<String> filtered = list.stream()
            .filter(StreamUtils.filterDuplicate())
            .collect(Collectors.toList());

        assertEquals(4, filtered.size());
        assertTrue(filtered.contains("ABC"));
        assertTrue(filtered.contains("EDF"));
        assertTrue(filtered.contains("123"));
        assertTrue(filtered.contains(null));

    }

    @Test
    public void shouldFilterDuplicateByComparatorFromStream() {

        List<String> list = TestHelpers.getListOfString("ABC", "ACD", "123", "EDF", "145");

        List<String> filtered = list.stream()
            .filter(StreamUtils.filterDuplicate(TestHelpers.getFirstLetterComparator()))
            .collect(Collectors.toCollection(ArrayList::new));

        assertEquals(3, filtered.size());
        assertTrue(filtered.contains("ABC"));
        assertTrue(filtered.contains("EDF"));
        assertTrue(filtered.contains("123"));

    }

    @Test
    public void shouldSumIntStreamElements() {

        List<Integer> list = TestHelpers.getListOfInt();
        long sum = StreamUtils.sum(list.stream(), t -> t.intValue());
        assertEquals(6, sum);
        
    }

    @Test
    public void shouldAverageIntStreamElements() {

        List<Integer> list = TestHelpers.getListOfInt();
        double sum = StreamUtils.average(list.stream(), t -> t.intValue());
        assertEquals(2, sum, 0);
        
    }

    @Test
    public void shouldGetStreamFromIterable() {

        Iterable<Integer> list = TestHelpers.getListOfInt();
        Stream<Integer> stream = StreamUtils.fromIterable(list);
        assertNotNull(stream);
        
    }

}
