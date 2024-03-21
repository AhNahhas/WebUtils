package com.anahhas.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class TestHelpers {

    public static List<Integer> getListOfInt(Integer... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static List<Integer> getListOfInt() {
        return new ArrayList<>(Arrays.asList(1, 2, 3));
    }

    public static List<String> getListOfString(String... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static List<String> getListOfString() {
        return new ArrayList<>(Arrays.asList("ABC", "abc", "123"));
    }

    public static Comparator<String> getCaseInsensitiveComparator() {
        return (a, b) -> a.toUpperCase().compareTo(b.toUpperCase());
    }

    public static Comparator<String> getFirstLetterComparator() {
        return (a, b) -> Character.valueOf(a.charAt(0)).compareTo(
            Character.valueOf(b.charAt(0)));
    }

    public static Predicate<String> getMaxLengthPredicate(final int length) {
        return s -> s.length() <= length;
    }

    public static Predicate<String> getStartsWithPredicate(final String prefix) {
        return str -> str.startsWith(prefix);
    }
    
}
