package com.anahhas.webutils;

import static com.anahhas.helpers.TestHelpers.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.junit.Test;

import com.anahhas.helpers.TestHelpers;



public class CollectionUtilsTest {

    @Test
    public void shouldContainCommonStrings() {
        
        List<String> listOne = TestHelpers.getListOfString("One", "Two", "Three");
        List<String> listTwo = TestHelpers.getListOfString( "Three", "Four", "Five");
        List<String> listThree = TestHelpers.getListOfString( "Three", "Six", "Nine");

        Collection<String> result = CollectionUtils.innerJoin(Function.identity(), listOne, listTwo, listThree);

        assertEquals(1, result.size());
        assertTrue(result.contains("Three"));

    }

    @Test
    public void shouldContainCommonNullableStrings() {
        
        List<String> listOne = TestHelpers.getListOfString("One", "Two", "Three", null, null);
        List<String> listTwo = TestHelpers.getListOfString( "Three", "Four", "Five", null);
        List<String> listThree = TestHelpers.getListOfString( "Three", "Six", "Nine", null);

        Collection<String> result = CollectionUtils.innerJoin(Function.identity(), listOne, listTwo, listThree);

        assertEquals(2, result.size());
        assertTrue(result.contains("Three"));
        assertTrue(result.contains(null));

    }

    @Test
    public void shouldContainCommonStringsByComporator() { 

        List<String> listOne = TestHelpers.getListOfString("One", "Two", "Three");
        List<String> listTwo = TestHelpers.getListOfString( "Three", "Four", "Five");
        List<String> listThree = TestHelpers.getListOfString( "Three", "Six", "Nine");

        Collection<String> result = CollectionUtils.innerJoin(getFirstLetterComparator(), 
            listOne, listTwo, listThree);

        assertEquals(2, result.size());
        assertTrue(result.contains("Two"));
        assertTrue(result.contains("Four"));

    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForCommonStringSearchByComparator() { 

        List<String> listOne = TestHelpers.getListOfString("One", "Two", "Three", null);
        List<String> listTwo = TestHelpers.getListOfString( "Three", "Four", "Five");
        CollectionUtils.innerJoin(getFirstLetterComparator(), listOne, listTwo);

    }

    @Test
    public void shouldContainUncommonStrings() {
        
        List<String> listOne = TestHelpers.getListOfString("One", "Two", "Three", null);
        List<String> listTwo = TestHelpers.getListOfString( "Three", "Four", "Five", null);
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
    public void shouldContainUncommonNullableStrings() {
        
        List<String> listOne = TestHelpers.getListOfString("One", "Two", "Three", null);
        List<String> listTwo = TestHelpers.getListOfString( "Three", "Four", "Five");
        List<String> listThree = TestHelpers.getListOfString( "Three", "Six", "Nine");

        Collection<String> result = CollectionUtils.outerJoin(Function.identity(), listOne, listTwo, listThree);

        assertEquals(7, result.size());
        assertTrue(result.contains("One"));
        assertTrue(result.contains("Two"));
        assertTrue(result.contains("Four"));
        assertTrue(result.contains("Five"));
        assertTrue(result.contains("Six"));
        assertTrue(result.contains("Nine"));
        assertTrue(result.contains(null));

    }

    @Test
    public void shouldContainUnCommonStringsByComporator() { 

        List<String> listOne = TestHelpers.getListOfString("One", "Two", "Three");
        List<String> listTwo = TestHelpers.getListOfString( "Three", "Four", "Five");
        List<String> listThree = TestHelpers.getListOfString( "Three", "Six", "Nine");

        Comparator<String> comparator = (a, b) -> 
            Character.valueOf(a.charAt(0)).compareTo(Character.valueOf(b.charAt(0)));

        Collection<String> result = CollectionUtils.outerJoin(comparator, listOne, listTwo, listThree);

        assertEquals(3, result.size());
        assertTrue(result.contains("One"));
        assertTrue(result.contains("Six"));
        assertTrue(result.contains("Six"));

    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForUnCommonStringSearchByComparator() { 

        List<String> listOne = TestHelpers.getListOfString("One", "Two", "Three", null);
        List<String> listTwo = TestHelpers.getListOfString( "Three", "Four", "Five");

        Comparator<String> comparator = (a, b) -> 
            Character.valueOf(a.charAt(0)).compareTo(Character.valueOf(b.charAt(0)));

        CollectionUtils.innerJoin(comparator, listOne, listTwo);

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
        boolean result = CollectionUtils.allMatch(list, getMaxLengthPredicate(3));
        assertFalse(result);

    }

    @Test
    public void shouldCountMatchingStrings() {

        List<String> list = TestHelpers.getListOfString();
        long result = CollectionUtils.countMatches(list, getStartsWithPredicate("A"));
        assertEquals(1, result);

    }

    @Test
    public void shouldReturnDistinctStrings() {

        List<String> duplicateList = TestHelpers.getListOfString("ONE", "TWO", "ONE", 
            "THREE", "FOUR", "ONE");

        Collection<String> result = CollectionUtils.distinct(duplicateList);
        
        assertEquals(4, result.size());
        assertTrue(result.contains("ONE"));
        assertTrue(result.contains("TWO"));
        assertTrue(result.contains("THREE"));
        assertTrue(result.contains("FOUR"));

    }

    @Test
    public void shouldReturnDistinctNullableStrings() {

        List<String> duplicateList = TestHelpers.getListOfString("ONE", "TWO", "ONE", 
            "THREE", "FOUR", "ONE", null, null);

        Collection<String> result = CollectionUtils.distinct(duplicateList);
        
        assertEquals(5, result.size());
        assertTrue(result.contains("ONE"));
        assertTrue(result.contains("TWO"));
        assertTrue(result.contains("THREE"));
        assertTrue(result.contains("FOUR"));
        assertTrue(result.contains(null));


    }

    @Test
    public void shouldReturnDistinctStringsByComparator() {

        List<String> duplicateList = TestHelpers.getListOfString();
        Collection<String> result = CollectionUtils.distinct(getCaseInsensitiveComparator(), duplicateList);

        assertEquals(2, result.size());
        assertTrue(result.contains("ABC"));
        assertTrue(result.contains("123"));

    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForDistinctSearchByComparator() {

        List<String> duplicateList = TestHelpers.getListOfString("A", "B", null);
        CollectionUtils.distinct(getCaseInsensitiveComparator(), duplicateList);
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
    public void shouldMapNullableCollectionStrings() {

        List<String> list = TestHelpers.getListOfString();
        Collection<String> result = CollectionUtils.mapElements(list, 
            s -> s.startsWith("A") ? null : s);
        
        assertEquals(3, result.size());
        assertTrue(result.contains(null));
        assertTrue(result.contains("123"));
        assertTrue(result.contains("abc"));

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
    public void shouldNotContainAllStrings() {

        List<String> list = TestHelpers.getListOfString("abc", "123");
        boolean result = CollectionUtils.containsAll(Comparator.naturalOrder(), list, "124", "ABc");
        assertFalse(result);

    }

    @Test
    public void shouldNotContainAllNumbersInCollection() {

        List<Integer> list = TestHelpers.getListOfInt();
        boolean result = CollectionUtils.containsAll(list, List.of(1, 2, 3, 5));
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
    public void shouldContainAnyOfStringsByIdentity() {

        List<String> list = TestHelpers.getListOfString("abc", "123");
        boolean result = CollectionUtils.containsAny(
            s -> Character.toUpperCase(s.charAt(0)), list, "XYZ", "ABC");
            
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

    @Test
    public void shouldGroupByIdentity() {

        List<StringBuilder> sbList = List.of(new StringBuilder("ABC"), new StringBuilder("AEF"),
            new StringBuilder("DEF"), new StringBuilder("123"));

        Map<Character, List<StringBuilder>> result = CollectionUtils.groupByIdentity(
            getFirstLetterMapper(), sbList);

        assertEquals(3, result.size());

        Set<Character> keys = result.keySet();
        assertTrue(keys.contains('A'));
        assertTrue(keys.contains('D'));
        assertTrue(keys.contains('1'));

        List<StringBuilder> startsWithA = result.get('A');
        assertEquals(2, startsWithA.size());
        assertEquals("ABC", startsWithA.get(0).toString());
        assertEquals("AEF", startsWithA.get(1).toString());

    }

    @Test
    public void shouldConcatCollections() {

        List<CharSequence> result = List.copyOf(CollectionUtils.concat(getListOfString(), Arrays.asList(
            new StringBuilder("ABC"), new StringBuilder("AEF"), new StringBuilder("DEF"))));

        assertEquals(6, result.size());
        assertEquals("ABC", result.get(0).toString());
        assertEquals("abc", result.get(1).toString());
        assertEquals("123", result.get(2).toString());
        assertEquals("ABC", result.get(3).toString());
        assertEquals("AEF", result.get(4).toString());
        assertEquals("DEF", result.get(5).toString());

    }

    @Test
    public void shouldGroupByNullableIdentity() {

        List<String> strList = TestHelpers.getListOfString("ABC", "def", "GHI", null);
        Function<String, String> customMapper = (str) -> Optional.ofNullable(str).orElse("123");
        Map<String, List<String>> result = CollectionUtils.groupByIdentity(customMapper, strList);
        assertEquals(4, result.size());

    }

}
