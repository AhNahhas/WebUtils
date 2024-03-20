package com.anahhas.helpers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestHelpers {

    public static List<Integer> getListOfInt(Integer... elements) {
        return new ArrayList<>(List.of(elements));
    }

    public static List<Integer> getListOfInt() {
        return new ArrayList<>(List.of(1, 2, 3));
    }

    public static List<String> getListOfString(String... elements) {
        return new ArrayList<>(List.of(elements));
    }

    public static List<String> getListOfString() {
        return new ArrayList<>(List.of("ABC", "abc", "123"));
    }

    public static Comparator<String> getCaseInsensitiveComparator() {
        return (a, b) -> a.toUpperCase().compareTo(b.toUpperCase());
    }
    
}
