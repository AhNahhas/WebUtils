package com.anahhas.webutils;

import static com.anahhas.helpers.TestHelpers.getCaseInsensitiveComparator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import com.anahhas.helpers.TestHelpers;



public class CollectionUtilsTest {

    @Test
    public void shouldContainCommonStrings() {
        
        List<String> listOne = TestHelpers.getListOfString("One", "Two", "Three");
        List<String> listTwo = TestHelpers.getListOfString( "Three", "Four", "Five");
        List<String> listThree = TestHelpers.getListOfString( "Three", "Six", "Nine");

        Collection<String> result = CollectionUtils.innerJoin(Function.identity(), 
            listOne, listTwo, listThree);

        assertEquals(1, result.size());
        assertTrue(result.contains("Three"));

    }

    @Test
    public void shouldContainUncommonStrings() {
        
        List<String> listOne = TestHelpers.getListOfString("One", "Two", "Three");
        List<String> listTwo = TestHelpers.getListOfString( "Three", "Four", "Five");
        List<String> listThree = TestHelpers.getListOfString( "Three", "Six", "Nine");

        Collection<String> result = CollectionUtils.outerJoin(Function.identity(), listOne, listTwo, listThree);

        assertEquals(6, result.size());
        assertTrue(result.contains("One"));
        assertTrue(result.contains("Two"));
        assertTrue(result.contains("Four"));
        assertTrue(result.contains("Five"));
        assertTrue(result.contains("Six"));
        assertTrue(result.contains("Nine"));

    }

    @Test
    public void shouldMatchAllStrings() {

        List<String> list = TestHelpers.getListOfString();
        boolean result = CollectionUtils.allMatch(list, str -> str.length() <= 3);
        assertTrue(result);

    }

    @Test
    public void shouldNotMatchAllStrings() {

        List<String> list = TestHelpers.getListOfString("ABC", "DEF", "GHI", "LENGTHIER_STRING");
        boolean result = CollectionUtils.allMatch(list, str -> str.length() <= 3);
        assertFalse(result);

    }

    @Test
    public void shouldCountMatchingStrings() {

        List<String> list = TestHelpers.getListOfString();
        long result = CollectionUtils.countMatches(list, str -> str.startsWith("A"));
        assertEquals(1, result);

    }

    @Test
    public void shouldReturnDistinctStrings() {

        List<String> duplicateList = TestHelpers.getListOfString("ONE", "TWO", "ONE", 
            "THREE", "FOUR", "ONE");

        Collection<String> result = CollectionUtils.distinctBy(duplicateList, Function.identity());
        
        assertEquals(4, result.size());
        assertTrue(result.contains("ONE"));
        assertTrue(result.contains("TWO"));
        assertTrue(result.contains("THREE"));
        assertTrue(result.contains("FOUR"));

    }

    @Test
    public void shouldReturnDistinctStringsByComparator() {

        List<String> duplicateList = TestHelpers.getListOfString();
        Collection<String> result = CollectionUtils.distinctBy(duplicateList, Function.identity(), 
            (a, b) -> a.toUpperCase().compareTo(b.toUpperCase()));

        assertEquals(2, result.size());
        assertTrue(result.contains("ABC"));
        assertTrue(result.contains("123"));

    }

    @Test
    public void shouldMapCollectionStrings() {

        List<String> list = TestHelpers.getListOfString();
        Collection<String> result = CollectionUtils.mapElements(list, String::toUpperCase);
        
        assertEquals(3, result.size());
        assertTrue(result.contains("ABC"));
        assertTrue(result.contains("123"));

    }

    @Test
    public void shouldContainAllNumbers() {

        List<Integer> list = TestHelpers.getListOfInt();
        boolean result = CollectionUtils.containsAll(list, 1, 2, 3);
        assertTrue(result);

    }

    @Test
    public void shouldContainAllStringsByComparator() {

        List<String> list = TestHelpers.getListOfString("abc", "123");
        boolean result = CollectionUtils.containsAll(getCaseInsensitiveComparator(), list, "ABC");
        assertTrue(result);

    }

    @Test
    public void shouldNotContainAllNumbers() {

        List<Integer> list = TestHelpers.getListOfInt();
        boolean result = CollectionUtils.containsAll(list, 1, 2, 3, 5);
        assertFalse(result);

    }

    @Test
    public void shouldNotContainAllStringsByComparator() {

        List<String> list = TestHelpers.getListOfString("abc", "123");
        boolean result = CollectionUtils.containsAll(Comparator.naturalOrder(), list, "124", "ABc");
        assertFalse(result);

    }

    @Test
    public void shouldContainAnyOfNumbers() {

        List<Integer> list = TestHelpers.getListOfInt();
        boolean result = CollectionUtils.containsAny(list, 1, 5);
        assertTrue(result);

    }

    @Test
    public void shouldContainAnyOfStringsByComparator() {

        List<String> list = TestHelpers.getListOfString("abc", "123");
        boolean result = CollectionUtils.containsAny(getCaseInsensitiveComparator(), list, "1", "ABC");
        assertTrue(result);

    }

    @Test
    public void shouldNotContainAnyOfNumbers() {

        List<Integer> list = TestHelpers.getListOfInt();
        boolean result = CollectionUtils.containsAny(list, 5);
        assertFalse(result);

    }

    @Test
    public void shouldNotContainAnyOfStrings() {

        List<String> list = TestHelpers.getListOfString();
        boolean result = CollectionUtils.containsAny(Comparator.naturalOrder(), list, "12.3", "ABc");
        assertFalse(result);

    }

    @Test
    public void shouldMergeCollections() {

        List<String> strList = TestHelpers.getListOfString("ABC", "def", "GHI");
        List<Integer> intList = TestHelpers.getListOfInt(123, 456, 789);
        Collection<String> result = CollectionUtils.merge(strList, intList, (String a, Integer b) -> a + b.intValue());
        
        assertEquals(3, result.size());
        assertTrue(result.contains("ABC123"));
        assertTrue(result.contains("def456"));
        assertTrue(result.contains("GHI789"));

    }

}
