package com.anahhas.webutils;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StringUtils {

    private enum PadDirection { LEFT, RIGHT; }

    private static final int MAX_PAD_SIZE = 1000;

    private static final String EMPTY_STRING = "";

    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    public static String lowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static boolean isBlank(CharSequence str) {

        int len = length(str);
        if(len == 0) return true;

        for(int i=0; i<len; i++)
            if(!Character.isWhitespace(str.charAt(i)))
                return false;
        
        return true;
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static int compare(CharSequence left, CharSequence right) {

        if(left == right) return 0;
        if(right == null) return 1;
        if(left == null) return -1;

        return CharSequence.compare(left, right);

    }

    public static int compareIgnoreCase(CharSequence left, CharSequence right) {

        if(left == right) return 0;
        if(right == null) return 1;
        if(left == null) return -1;

        int leftLen = length(left);
        int rightLen = length(right);

        for(int i=0; i<leftLen; i++) {

            if(i>=rightLen)
                break;

            Character leftChar = Character.toUpperCase(left.charAt(i));
            Character rightChar = Character.toUpperCase(right.charAt(i));
            int result = leftChar.compareTo(rightChar);
            
            if(result != 0)
                return result;

        }

        return Integer.valueOf(leftLen).compareTo(rightLen);

    }

    public static boolean equals(CharSequence left, CharSequence right) {
        return compare(left, right) == 0;
    }

    public static boolean equalsIgnoreCase(CharSequence left, CharSequence right) {
        return compareIgnoreCase(left, right) == 0;
    }

    public static boolean contains(CharSequence str, char searched) {

        if(length(str) == 0) return false;
        return str.chars().anyMatch(c -> c == searched);

    }

    public static boolean contains(CharSequence str, CharSequence searched) {
        return contains(str, searched, false);
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence searched) {
        return contains(str, searched, true);
    }

    private static boolean contains(CharSequence str, CharSequence searched, final boolean ignoreCase) {

        
        if(str == null || searched == null) return false;

        int strLen = length(str);
        int searchedLen = length(searched);

        if(strLen == 0 && searchedLen == 0) return true;        
        if(searchedLen == 0) return true;
        
        if(strLen < searchedLen) return false;

        BiPredicate<Character, Character> predicate = (one, two) -> ignoreCase ?
            Character.toUpperCase(one) == Character.toUpperCase(two) : one == two;

        for(int j=0; j<strLen; j++) {

            if(strLen - j < searchedLen)
                break;
            
            int i=0, cursor = j, matchLen = 0;
            while(i < searchedLen && cursor < strLen && 
                predicate.test(str.charAt(cursor), searched.charAt(i))) {

                i++; cursor++; matchLen++;

            }

            if(matchLen == searchedLen) return true;
                
        }

        return false;
        
    }

    public static String leftPad(String str, int size) {
        return stringPad(str, size, PadDirection.LEFT);
    }

    public static String rightPad(String str, int size) {
        return stringPad(str, size, PadDirection.RIGHT);
    }

    private static String stringPad(String str, int size, PadDirection direction) {

        if(str == null) return null;

        StringBuilder padSb = new StringBuilder(size);
        if(size > 0 || size < MAX_PAD_SIZE){

            int i = 0;
            do 
                padSb.append(" ");
            while(++i < size);

        }
        
        StringBuilder sb = new StringBuilder(str);
        if(PadDirection.RIGHT.equals(direction))
            sb.append(padSb);
        else
            sb.insert(0, padSb);

        return sb.toString();

    }

    public static String concat(CharSequence... elements) {
        return joinFiltered(EMPTY_STRING, Objects::nonNull, elements);
    }

    public static String join(String delimiter, CharSequence... elements) {
        return joinFiltered(delimiter, Objects::nonNull, elements);
    }

    public static String concatIgnoreBlank(CharSequence... elements) {
        return joinFiltered(EMPTY_STRING, StringUtils::isNotBlank, elements);
    }

    public static String joinIgnoreBlank(String delimiter, CharSequence... elements) {
        return joinFiltered(delimiter, StringUtils::isNotBlank, elements);
    }

    public static String joinFiltered(String delimiter, Predicate<? super CharSequence> filter, 
        CharSequence... elements) {
        
        if(elements == null || elements.length == 0)
            return null;

        return Arrays.asList(elements).stream()
            .filter(filter)
            .collect(Collectors.joining(OptionalUtils.orDefault(
                OptionalUtils.ofBlank(delimiter), () -> EMPTY_STRING)));

    }

}
