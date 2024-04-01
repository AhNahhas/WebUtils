package com.anahhas.webutils;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility methods to handle {@link java.lang.String} & {@link java.lang.CharSequence}.
 * @author Ahmed Amin Nahhas
 */
public class StringUtils {

    private enum PadDirection { LEFT, RIGHT; }

    private static final int MAX_PAD_SIZE = 1000;

    private static final String EMPTY_STRING = "";

    /**
     * Returns the length of a {@link java.lang.CharSequence}. A null reference is 
     * considered to have a length equal to 0.
     * 
     * @param str CharSequence which length to calculate
     * @return    length of CharSequence  
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * Transforms a {@link java.lang.String} to lower case. If str is null then a null 
     * reference is returned.
     * 
     * @param str String to lower case
     * @return lower cased String
     */
    public static String lowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    /**
     * Transforms a {@link java.lang.String} to upper case. If str is null then a null 
     * reference is returned.
     * 
     * @param str String to upper case
     * @return    upper cased String
     */
    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    /**
     * Checks if a {@link java.lang.CharSequence} is composed of whitespaces only.
     * A null reference and an empty CharSequence are also considered blank.
     * 
     * @param str CharSequence to check
     * @return    boolean containing result of check
     */
    public static boolean isBlank(CharSequence str) {

        if(length(str) == 0) return true;
        return str.chars().allMatch(Character::isWhitespace);

    }

    /**
     * Checks if a {@link java.lang.CharSequence} contains a non whitespace character.
     * If it's a null reference or an empty CharSequence, false is returned.
     * 
     * @param str CharSequence to check
     * @return boolean containing result of check
     */
    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    /**
     * Compares lexicographically two {@link java.lang.CharSequence} references.
     * If one CharSequence contains the other, then the length is compared
     * using the natural order. If both CharSequence references are null or
     * empty then true is returned.
     * <p>
     * If left is lexicographically greater than right, a positive number is returned.
     * If right is lexicographically greater than left, a negative number is returned.
     * Otherwise, if left and right are equal, 0 is returned.
     * 
     * @param left  First CharSequence to compare
     * @param right Second CharSequence to compare
     * @return      int containing the result of comparison
     */
    public static int compare(CharSequence left, CharSequence right) {

        if(left == right) return 0;
        if(right == null) return 1;
        if(left == null) return -1;

        return CharSequence.compare(left, right);

    }

    /**
     * Compares lexicographically and case-insensitively two {@link java.lang.CharSequence} references. 
     * If one CharSequence contains the other, then the length is compared using the natural order.
     * <p>
     * If left is case-insensitively, lexicographically greater than right, a positive number is returned.
     * If right is case-insensitively, lexicographically greater than left, a negative number is returned.
     * Otherwise, if left and right are equal, 0 is returned.
     * 
     * @param left  First CharSequence to compare
     * @param right Second CharSequence to compare
     * @return      int containing the result of comparison
     */
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

    /**
     * Verifies if two {@link java.lang.CharSequence} references are lexicographically equal.
     * If both references are null or empty, then true is returned.
     * 
     * @param left  First CharSequence to compare
     * @param right Second CharSequence to compare
     * @return      boolean containing the result of verification
     */
    public static boolean equals(CharSequence left, CharSequence right) {
        return compare(left, right) == 0;
    }

    /**
     * Verifies if two {@link java.lang.CharSequence} references are case-insensitively lexicographically equal.
     * If both references are null or empty, then true is returned.
     * 
     * @param left  First CharSequence to compare
     * @param right Second CharSequence to compare
     * @return      boolean containing the result of verification
     */
    public static boolean equalsIgnoreCase(CharSequence left, CharSequence right) {
        return compareIgnoreCase(left, right) == 0;
    }

    /**
     * Verifies if a {@link java.lang.CharSequence} contains a specific char.
     * If str is null or empty, then false is returned.
     * 
     * @param left  CharSequence to verify
     * @param right CharSequence to search for
     * @return      boolean containing the result of verification
     */
    public static boolean contains(CharSequence str, char searched) {

        if(length(str) == 0) return false;
        return str.chars().anyMatch(c -> c == searched);

    }

    /**
     * Verifies if a {@link java.lang.CharSequence} lexicographically contains a another CharSequence.
     * If at least one CharSequece is null then false is returned.
     * 
     * @param left  CharSequence to verify
     * @param right CharSequence to search for
     * @return      boolean containing the result of verification
     */
    public static boolean contains(CharSequence str, CharSequence searched) {
        return contains(str, searched, false);
    }

    /**
     * Verifies if a {@link java.lang.CharSequence} case-insensitively lexicographically contains a another CharSequence.
     * If at least one CharSequece is null then false is returned.
     * 
     * @param left  CharSequence to verify
     * @param right CharSequence to search for
     * @return      boolean containing the result of verification
     */
    public static boolean containsIgnoreCase(CharSequence str, CharSequence searched) {
        return contains(str, searched, true);
    }

    /**
     * Leftpads a {@link java.lang.String} with a number of blank characters.
     * If size is negative or equal to zero then str is returned.
     * 
     * @param str  String to leftpad
     * @param size size of leftpad
     * @return     String with leftpad
     */
    public static String leftPad(String str, int size) {
        return stringPad(str, size, PadDirection.LEFT);
    }

    /**
     * Rightpads a {@link java.lang.String} with a number of blank characters.
     * If size is negative or equal to zero then str is returned.
     * 
     * @param str  String to rightpad
     * @param size size of rightpad
     * @return     String with rightpad
     */
    public static String rightPad(String str, int size) {
        return stringPad(str, size, PadDirection.RIGHT);
    }

    /**
     * Concatenates CharSequence references into a String, filtering out 
     * null references. This method returns a null reference if varargs 
     * parameter is null or not assigned.
     *      
     * @param elements Varargs number of CharSequence references
     * @return         Concatenated String
     */
    public static String concat(CharSequence... elements) {
        return joinFiltered(EMPTY_STRING, Objects::nonNull, elements);
    }

    /**
     * Joins {@link java.lang.CharSequence} references into a String, using the given delimiter 
     * and filtering out null references. This method returns a null reference if varargs 
     * parameter is null or not assigned.  If delimiter is null or blank, and empty string ("") 
     * is used as a delimiter.
     *      
     * @param delimiter Delimeter used between joined CharSequence references
     * @param elements  Varargs number of CharSequence references
     * @return          Joined String
     */
    public static String join(String delimiter, CharSequence... elements) {
        return joinFiltered(delimiter, Objects::nonNull, elements);
    }

    /**
     * Concatenates {@link java.lang.CharSequence} references into a String, filtering out blank 
     * CharSequence references. This method returns a null reference if varargs parameter is null 
     * or not assigned.
     *      
     * @param elements Varargs number of CharSequence references
     * @return         Concatenated String
     */
    public static String concatIgnoreBlank(CharSequence... elements) {
        return joinFiltered(EMPTY_STRING, StringUtils::isNotBlank, elements);
    }

    /**
     * Joins {@link java.lang.CharSequence} references into a String, using the given delimiter 
     * and filtering out blank CharSequence references. This method returns a null reference if 
     * varargs parameter is null or not assigned.  If delimiter is null or blank, and empty string ("") 
     * is used as a delimiter.
     *      
     * @param delimiter Delimeter used between joined CharSequence references
     * @param elements  Varargs number of CharSequence references
     * @return          Joined String
     */
    public static String joinIgnoreBlank(String delimiter, CharSequence... elements) {
        return joinFiltered(delimiter, StringUtils::isNotBlank, elements);
    }

    /**
     * Joins {@link java.lang.CharSequence} references into a String, using the given delimiter and 
     * filtering CharSequence references using a {@link java.util.function.Predicate} implementation. 
     * This method returns a null reference if varargs parameter is  null or not assigned. If delimiter 
     * is null or blank, and empty string ("") is used as a delimiter.
     *      
     * @param delimiter Delimeter used between joined CharSequence references
     * @param elements  Varargs number of CharSequence references
     * @return          Joined String
     */
    public static String joinFiltered(String delimiter, Predicate<? super CharSequence> filter, 
        CharSequence... elements) {
        
        if(elements == null || elements.length == 0)
            return null;

        return Arrays.asList(elements).stream()
            .filter(filter)
            .collect(Collectors.joining(OptionalUtils.orDefault(
                OptionalUtils.ofBlank(delimiter), () -> EMPTY_STRING)));

    }

    /*************************** API PRIVATE METHODS ***************************/

    private static String stringPad(String str, int size, PadDirection direction) {

        if(str == null) return null;
        if(size <= 0) return str;

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

}
