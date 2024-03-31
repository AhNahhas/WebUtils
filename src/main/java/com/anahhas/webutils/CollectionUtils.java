package com.anahhas.webutils;

import static com.anahhas.webutils.OptionalUtils.ofMappable;
import static com.anahhas.webutils.OptionalUtils.ofSupplied;
import static com.anahhas.webutils.OptionalUtils.orDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility methods to handle {@link java.util.Collection}.
 * @author Ahmed Amin Nahhas
 */
public class CollectionUtils {

    /**
     * Tests if a collection reference is null or the instance does not contain any element.
     * 
     * @param collection the collection reference
     * @return boolean equal to true if condition, false otherwise
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Tests if a collection reference is not null and the instance does contain at least one element.
     * 
     * @param collection the collection reference
     * @return boolean equal to true if condition, false otherwise
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Returns a shallow copy of the input collection containing distinct elements. The collection's 
     * elements must implement (or inherit from) the {@link java.lang.Comparable} interface. 
     * The distinction is then determined using {@link java.lang.Comparable#compareTo(Object) compareTo} 
     * method. An empty collection (or null) is returned if the input collection is empty (or null). 
     * This implementation is null friendly, if the input collection contains null references, the output 
     * collection, will contain a single null value. 
     * 
     * @param <T>         The type (or super type) of collection elements
     * @param collection  The collection to apply the distinction on
     * @return            Collection of distinct elements
     */
    public static <T extends Comparable<? super T>> Collection<T> distinct(Collection<? extends T> collection) {
        return distinct(nullFirstComparator(Comparator.naturalOrder()), collection);
    }

    /**
     * Returns a shallow copy of the input collection containing distinct elements. The distinction is 
     * determined first, by mapping the collection elements to {@link java.lang.Comparable} types, and 
     * then using {@link java.lang.Comparable#compareTo(Object) compareTo} on mapped objects. 
     * An empty collection (or null) is returned if the input collection is empty (or null).
     * This implementation is null friendly, if the input collection contains null references, the output 
     * collection, will contain a single null value.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param <U>        The type mapped by the identity mapper
     * @param identity   The mapper function
     * @param collection The collection to apply the distinction on
     * @return           Collection of distinct elements
     */
    public static <T, U extends Comparable<? super U>> Collection<T> distinct(
        Function<? super T, ? extends U> identity, 
        Collection<? extends T> collection
    ) {

        return distinct(nullFirstComparator(identity), collection);

    }

    /**
     * Returns a shallow copy of the input collection containing distinct elements.
     * The distinction is determined using a {@link java.util.Comparator} implementation.
     * An empty collection (or null) is returned if the input collection is empty (or null).
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param comparator The comparator implementation to determine distinction
     * @param collection The collection to apply the distinction on
     * @return           Collection of distinct elements
     */
    public static <T> Collection<T> distinct(Comparator<? super T> comparator, Collection<? extends T> collection) {
        
        if(isEmpty(collection))
            return mutableCopyOf(collection);

        List<T> distinctList = new ArrayList<>(collection.size());
        
        for(T element : collection)
            if(!containsAny(comparator, distinctList, element))
                distinctList.add(element);

        return distinctList;

    }

    /**
     * Returns a shallow copy collection containing elements that aren't common between the input collections. 
     * The collection elements must implement (or inherit from) the {@link java.lang.Comparable} interface. 
     * The comparison is determined using the {@link java.lang.Comparable#compareTo(Object) compareTo} method.  
     * An empty {@link java.util.Collection} is returned if the input parameter is null or is composed of empty 
     * collections. This implementation is null friendly, if the input collections contain an uncommon null 
     * reference, the output collection, will contain a single null value.
     * 
     * @param <T>         The type (or super type) of collection elements
     * @param collections Collection varargs to apply the outer join on 
     * @return            Collection of uncommon elements
     */
    @SafeVarargs
    public static <T extends Comparable<? super T>> Collection<T> outerJoin(
        Collection<? extends T>... collections
    ) {
        
        return outerJoin(nullFirstComparator(Comparator.naturalOrder()), collections);

    }

    /**
     * Returns a shallow copy collection containing elements that aren't common between the input collections. 
     * The comparison is determined first, by mapping the collection elements to {@link java.lang.Comparable} 
     * types, and then using {@link java.lang.Comparable#compareTo(Object) compareTo} on mapped objects. An empty 
     * {@link java.util.Collection} is returned if the input parameter is null or is composed of empty collections. 
     * This implementation is null friendly, if the input collections contain an uncommon null reference, the output 
     * collection, will contain a single null value.
     * 
     * @param <T>         The type (or super type) of collection elements
     * @param <U>         The type mapped by the identity mapper
     * @param identity    The mapper function
     * @param collections Collection varargs to apply the outer join on
     * @return            Collection of uncommon elements
     */
    @SafeVarargs
    public static <T, U extends Comparable<? super U>> Collection<T> outerJoin(
        Function<? super T, ? extends U> identity, 
        Collection<? extends T>... collections
    ) {
        
        return outerJoin(nullFirstComparator(identity), collections);

    }

    /**
     * Returns a shallow copy collection containing elements that aren't common between the input collections. 
     * The comparison is determined using a {@link java.util.Comparator} implementation. An empty 
     * {@link java.util.Collection} is returned if the input parameter is null or is composed of empty collections.
     * 
     * @param <T>         The type (or super type) of collection elements
     * @param comparator  The comparator implementation
     * @param collections Collection varargs to apply the outer join on
     * @return            Collection of uncommon elements
     */
    @SafeVarargs
    public static <T> Collection<T> outerJoin(Comparator<? super T> comparator,
        Collection<? extends T>... collections) {
        
        Collection<T> concat = concat(collections);
        
        if(isEmpty(concat)) return concat;

        List<T> uncommon = new ArrayList<>(concat.size());
        Predicate<T> isUnique = (element) -> 
            !containsAny(comparator, uncommon, element) && 
                count(comparator, element, concat) == 1;

        for(T element : concat)
            if(isUnique.test(element))
                uncommon.add(element);

        return uncommon;

    }

    /**
     * Returns a shallow copy collection containing elements that are common between the input collections. 
     * The collection elements must implement (or inherit from) the {@link java.lang.Comparable} interface. 
     * The comparison is determined using the {@link java.lang.Comparable#compareTo(Object) compareTo} method.  
     * An empty {@link java.util.Collection} is returned if the input collections does not contain any elements.
     * This implementation is null friendly, if the input collections contain a common null reference, the 
     * output collection, will contain a single null value.
     * 
     * @param <T>         The type (or super type) of collection elements
     * @param collections Collection varargs to apply the outer join on 
     * @return            Collection of common elements
     */
    @SafeVarargs
    public static <T extends Comparable<? super T>> Collection<T> innerJoin(
        Collection<? extends T>... collections
    ) { 

        return innerJoin(Function.identity(), collections);

    }

    /**
     * Returns a shallow copy collection containing elements that are common between the input collections. 
     * The comparison is determined first, by mapping the collection elements to {@link java.lang.Comparable} 
     * types, and then using {@link java.lang.Comparable#compareTo(Object) compareTo} on mapped objects. An 
     * empty {@link java.util.Collection} is returned if the input collections does not contain any elements.
     * This implementation is null friendly, if the input collections contain a common null reference, the output 
     * collection, will contain a single null value..
     * 
     * @param <T>         The type (or super type) of collection elements
     * @param <U>         The type mapped by the identity mapper
     * @param identity    The mapper function
     * @param collections Collection varargs to apply the outer join on
     * @return            Collection of common elements
     */
    @SafeVarargs
    public static <T, U extends Comparable<? super U>> Collection<T> innerJoin(
        Function<? super T, ? extends U> identity, 
        Collection<? extends T>... collections
    ) {
        
        return innerJoin(nullFirstComparator(identity), collections);

    }

    /**
     * Returns a shallow copy collection containing elements that are common between the input collections. 
     * The comparison is determined using a {@link java.util.Comparator} implementation. An empty 
     * {@link java.util.Collection} is returned if the input collections does not contain any elements.
     * 
     * @param <T>         The type (or super type) of collection elements
     * @param comparator  The comparator implementation
     * @param collections Collection varargs to apply the outer join on
     * @return            Collection of common elements
     */
    @SafeVarargs
    public static <T> Collection<T> innerJoin(Comparator<? super T> comparator, 
        Collection<? extends T>... collections) {
        
        Collection<T> concat = concat(collections);

        if(isEmpty(concat)) return concat;

        List<T> common = new ArrayList<>(concat.size());
        Predicate<T> isCommon = (element) -> 
            !containsAny(comparator, common, element) && 
                count(comparator, element, concat) > 1;

        for(T element : concat)
            if(isCommon.test(element))
                common.add(element);

        return common;

    }

    /**
     * Returns the minimum value of the collection elements. Collection elements must implement 
     * (or inherit from) the {@link java.lang.Comparable} interface. The comparison is determined 
     * using the {@link java.lang.Comparable#compareTo(Object) compareTo} method.
     * This implementation is null friendly, a null value is considered the absolute maximum value.
     * For custom comparison, use {@link #maxOf(java.util.Comparator, java.util.Collection)}.
     * A null value is returned if the collection does not contain any elements.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param collection Collection to the determine the min from
     * @return           The minimum value or null if collection has no elements
     */
    public static <T extends Comparable<? super T>> T minOf(Collection<? extends T> collection) {
        return minOf(nullLastComparator(Comparator.naturalOrder()), collection);
    }

    /**
     * Returns the minimum value of the collection elements. The comparison is determined first, 
     * by mapping the collection elements to {@link java.lang.Comparable} types, and then using 
     * {@link java.lang.Comparable#compareTo(Object) compareTo} on mapped objects.
     * This implementation is null friendly, a null value is considered the absolute maximum value.
     * A null value is returned if the collection does not contain any elements.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param <U>        The type mapped by the identity mapper
     * @param identity   The mapper function
     * @param collection Collection to the determine the min from
     * @return           The minimum value or null if collection has no elements
     */
    public static <T, U extends Comparable<? super U>> T minOf(Function<? super T, ? extends U> identity,
        Collection<? extends T> collection) {

        return minOf(nullLastComparator(identity), collection);

    }

    /**
     * Returns the minimum value of the collection elements. The comparison is determined 
     * using a {@link java.util.Comparator} implementation. A null value is returned if 
     * the collection does not contain any elements.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param comparator The comparator implementation
     * @param collection Collection to the determine the min from
     * @return           The minimum value or null if collection has no elements
     */
    public static <T> T minOf(Comparator<? super T> comparator, Collection<? extends T> collection) {

        return Stream.ofNullable(collection)
            .flatMap(Collection::stream)
            .collect(Collectors.minBy((comparator)))
            .orElse(null);

    }

    /**
     * Returns the maximum value of the collection elements. Collection elements must implement 
     * (or inherit from) the {@link java.lang.Comparable} interface. The comparison is determined 
     * using the {@link java.lang.Comparable#compareTo(Object) compareTo} method.
     * This implementation is null friendly, a null value is considered the absolute minimum value.
     * For custom comparison, use {@link #maxOf(java.util.Comparator, java.util.Collection)}.
     * A null value is returned if the collection does not contain any elements.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param collection Collection to the determine the min from
     * @return           The maximum value or null if collection has no elements
     */
    public static <T extends Comparable<? super T>> T maxOf(Collection<? extends T> collection) {
        return maxOf(nullFirstComparator(Comparator.naturalOrder()), collection);
    }

    /**
     * Returns the maximum value of the collection elements. The comparison is determined first, 
     * by mapping the collection elements to {@link java.lang.Comparable} types, and then using 
     * {@link java.lang.Comparable#compareTo(Object) compareTo} on mapped objects.
     * This implementation is null friendly, a null value is considered the absolute minimum value.
     * A null value is returned if the collection does not contain any elements.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param <U>        The type mapped by the identity mapper
     * @param identity   The mapper function
     * @param collection Collection to the determine the min from
     * @return           The maximum value or null if collection has no elements
     */
    public static <T, U extends Comparable<? super U>> T maxOf(Function<? super T, ? extends U> identity,
        Collection<? extends T> collection) {

        return maxOf(nullFirstComparator(identity), collection);

    }

    /**
     * Returns the maximum value of the collection elements. The comparison is determined 
     * using a {@link java.util.Comparator} implementation. A null value is returned if 
     * the collection does not contain any elements.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param comparator The comparator implementation
     * @param collection Collection to the determine the min from
     * @return           The maximum value or null if collection has no elements
     */
    public static <T> T maxOf(Comparator<? super T> comparator, Collection<? extends T> collection) {

        return Stream.ofNullable(collection)
            .flatMap(Collection::stream)
            .collect(Collectors.maxBy(comparator))
            .orElse(null);

    }
    
    /**
     * Returns a shallow copy of the input collection containing filtered elements that match the 
     * input {@link java.util.function.Predicate}. An empty collection is returned if the input
     * collection does not contain any elements.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param collection Collection to filter elements from
     * @param predicate  The predicate to test against the collection's elements
     * @return           Collection of filtered elements
     */
    public static <T> Collection<T> filter(Collection<? extends T> collection, Predicate<? super T> predicate) {

        return Stream.ofNullable(collection)
            .flatMap(Collection::stream)
            .filter(predicate)
            .collect(Collectors.toCollection(ArrayList::new));

    }

    /**
     * Checks if all the elements in the collection match the predicate and returns true.
     * If the collection is empty (or null), returns false.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param collection Collection to check
     * @param predicate  Predicate to check
     * @return           boolean indicating the check status
     */
    public static <T> boolean allMatch(Collection<? extends T> collection, Predicate<? super T> predicate) {

        if(isEmpty(collection))
            return false;
            
        return collection.stream().allMatch(predicate);
    }

    /**
     * Counts the elements of the collection that match the predicate
     * If the collection is empty (or null), returns 0.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param collection Collection to count matches from
     * @param predicate  Predicate to check
     * @return           long counting the elements passing the predicate
     */
    public static <T> long countMatches(Collection<? extends T> collection, Predicate<? super T> predicate) {

        return Stream.of(collection)
            .flatMap(Collection::stream)
            .filter(predicate)
            .count();

    }

    /**
     * Merges a vararg number of of collections into a single one.
     * If the parameter is null /or the collections are empty, a null reference/ or an empty
     * collection is returned.
     * 
     * @param <T>         The type (or super type) of collection elements
     * @param collections Collections to merge
     * @return            Merged collections
     */
    @SafeVarargs
    public static <T> Collection<T> merge(Collection<? extends T>... collections) {

        if(collections == null)
            return null;

        return Stream.of(collections)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));

    }

    /**
     * Counts the number of collection elements that are equal to object. Collection elements must 
     * implement or (inherit from) the {@link java.lang.Comparable} interface. Equality is determined 
     * using the {@link java.lang.Comparable#compareTo(Object) compareTo} method.
     * This implementation is null friendly, the counting of a null object will return the number of
     * null objects present in the collection, use {@link #count(java.util.Comparator, java.util.Collection)}.
     * to override this behaviour using a custom {@link java.util.Comparator}.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param object     The object equals to search for
     * @param collection Collection to count from
     * @return           long couting elements that are equal to object.
     */
    public static <T extends Comparable<? super T>> long count(T object, Collection<? extends T> collection) {
        return count(nullFirstComparator(Comparator.naturalOrder()), object, collection);
    }

    /**
     * Counts the number of collection elements that are equal to object. The equality is determined first, 
     * by mapping the collection elements to {@link java.lang.Comparable} types, and then using 
     * {@link java.lang.Comparable#compareTo(Object) compareTo} on mapped objects. This implementation is null 
     * friendly, the counting of a null object will return the number of null objects present in the collection.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param <U>        The type mapped by the identity mapper
     * @param identity   The mapper function
     * @param object     The object equals to search for 
     * @param collection Collection to count from
     * @return           long couting elements that are equal to object.
     */
    public static <T, U extends Comparable<? super U>> long count(Function<? super T, ? extends U> identity, 
        T object, Collection<? extends T> collection) {

        return count(nullFirstComparator(identity), object, collection);
            
    }

    /**
     * Counts the number of collection elements that are equal to object. The equality is determined 
     * using a {@link java.util.Comparator} implementation.
     * 
     * @param <T>        The type (or super type) of collection elements
     * @param comparator The comparator implementation
     * @param object     The object equals to search for 
     * @param collection Collection to count from
     * @return           long couting elements that are equal to object.
     */
    public static <T> long count(Comparator<? super T> comparator, T object, Collection<? extends T> collection) {

        return Stream.ofNullable(collection)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter((final var element) -> comparator.compare(element, object) == 0)
            .count();
            
    }

    /**
     * Verifies if collection contains at least one element from container. Both collections elements must 
     * implement (or inherit from) the {@link java.lang.Comparable} interface. Equality is determined 
     * using the {@link java.lang.Comparable#compareTo(Object) compareTo} method. This implementation
     * is null friendly, if both collection and container contain a null reference, then it is considered 
     * a match & true is returned. If collection or container is empty (or null) then false is returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param collection Collection of elements to verify
     * @param container  Collection of elements to search
     * @return           boolean containing result of verification
     */
    public static <T extends Comparable<? super T>> boolean containsAny(Collection<? extends T> collection, 
        Collection<? extends T> container) {

        return containsAny(nullFirstComparator(Comparator.naturalOrder()), collection, container);

    }

    /**
     * Verifies if collection contains at least one element from varargs. Both collection and varargs elements 
     * must implement (or inherit from) the {@link java.lang.Comparable} interface. Equality is determined using 
     * the {@link java.lang.Comparable#compareTo(Object) compareTo} method. This implementation is null friendly, 
     * if both collection and varargs parameter contain a null reference, then it is considered a match & true 
     * is returned. If collection or varargs is empty (or null) then false is returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param collection Collection of elements to verify
     * @param elements   Varargs of elements to search
     * @return           boolean containing result of verification
     */
    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean containsAny(Collection<? extends T> collection, 
        T... elements) {

        return containsAny(nullFirstComparator(Comparator.naturalOrder()), collection, Arrays.asList(elements));

    }

    /**
     * Verifies if collection contains at least one element from container. The equality is determined 
     * using a {@link java.util.Comparator} implementation. If collection or container is empty (or null) 
     * then false is returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param comparator Comparator implementation
     * @param collection Collection of elements to verify
     * @param container  Collection of elements to search
     * @return           boolean containing result of verification
     */
    public static <T> boolean containsAny(Comparator<? super T> comparator, Collection<? extends T> collection, 
        final Collection<? extends T> container) {

        if(isEmpty(collection) || isEmpty(container))
            return false;

        return collection.stream()
            .anyMatch((final var element) -> container.stream()
                .anyMatch(object -> comparator.compare(element, object) == 0));

    }

    /**
     * Verifies if collection contains at least one element from varargs. The equality is determined using a 
     * {@link java.util.Comparator} implementation. If collection or varargs is empty (or null) then false is 
     * returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param comparator Comparator implementation
     * @param collection Collection of elements to verify
     * @param elements   Varargs of elements to search
     * @return           boolean containing result of verification
     */
    @SafeVarargs
    public static <T> boolean containsAny(Comparator<? super T> comparator, Collection<? extends T> collection, 
        T... elements) {

        return containsAny(comparator, collection, Arrays.asList(elements));

    }

    /**
     * Verifies if collection contains at least one element from container. The equality is determined 
     * first, by mapping the collection elements to {@link java.lang.Comparable} types, and then using 
     * {@link java.lang.Comparable#compareTo(Object) compareTo} on mapped objects. This implementation 
     * is null friendly, if both collection and container contain a null reference, then it is considered 
     * a match & true is returned. If collection or container is empty (or null) then false is returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param <U>        The type mapped by the identity mapper
     * @param identity   The mapper function
     * @param collection Collection of elements to verify
     * @param container  Collection of elements to search
     * @return           boolean containing result of verification
     */
    public static <T, U extends Comparable<? super U>> boolean containsAny(
        Function<? super T, ? extends U> identity,
        Collection<? extends T> collection, 
        Collection<? extends T> container
    ) {

        return containsAny(nullFirstComparator(identity), collection, container);

    }

    /**
     * Verifies if collection contains at least one element from varargs. The equality is determined 
     * first, by mapping the collection elements to {@link java.lang.Comparable} types, and then using 
     * {@link java.lang.Comparable#compareTo(Object) compareTo} on mapped objects. This implementation 
     * is null friendly, if both collection and varargs parameter contain a null reference, then it is 
     * considered a match & true is returned. If collection is empty or varargs (or null) then false is 
     * returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param <U>        The type mapped by the identity mapper
     * @param identity   The mapper function
     * @param collection Collection of elements to verify
     * @param elements   Varargs of elements to search
     * @return           boolean containing result of verification
     */
    @SafeVarargs
    public static <T, U extends Comparable<? super U>> boolean containsAny(
        Function<? super T, ? extends U> identity,
        Collection<? extends T> collection, 
        T... elements
    ) {

        return containsAny(nullFirstComparator(identity), collection, Arrays.asList(elements));

    }

    /**
     * Verifies if collection contains all elements of container. Both collections elements must 
     * implement (or inherit from) the {@link java.lang.Comparable} interface. Equality is determined 
     * using the {@link java.lang.Comparable#compareTo(Object) compareTo} method. This implementation
     * is null friendly, if container contains a null reference, collection must also contain at least
     * one null reference for the verification. If collection or container is empty (or null) then false 
     * is returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param collection Collection of elements to verify
     * @param container  Collection of elements to search
     * @return           boolean containing result of verification
     */
    public static <T extends Comparable<? super T>> boolean containsAll(Collection<? extends T> collection, 
        Collection<? extends T> container) {

        return containsAll(nullFirstComparator(Comparator.naturalOrder()), collection, container);

    }

    /**
     * Verifies if collection contains all varargs elements. Both collections elements must implement 
     * (or inherit from) the {@link java.lang.Comparable} interface. Equality is determined using the 
     * {@link java.lang.Comparable#compareTo(Object) compareTo} method. This implementation is null 
     * friendly, if varargs contains a null reference, collection must also contain at least one null
     * reference for the verification. If collection or varargs is empty (or null) then false is returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param collection Collection of elements to verify
     * @param elements   Varargs of elements to search
     * @return           boolean containing result of verification
     */
    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean containsAll(Collection<? extends T> collection, 
        T... elements) {

        return containsAll(nullFirstComparator(Comparator.naturalOrder()), collection, Arrays.asList(elements));

    }

    /**
     * Verifies if collection contains all elements of container. The equality is determined using a 
     * {@link java.util.Comparator} implementation. If collection or container is empty (or null) then 
     * false is returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param comparator Comparator implementation
     * @param collection Collection of elements to verify
     * @param container  Collection of elements to search
     * @return           boolean containing result of verification
     */
    public static <T> boolean containsAll(final Comparator<? super T> comparator, 
        final Collection<? extends T> collection, final Collection<? extends T> container) {

        if(isEmpty(collection) || isEmpty(container))
            return false;

        return container.stream()
            .filter((final var element) -> containsAny(comparator, collection, element))
            .count() == container.size();

    }

    /**
     * Verifies if collection contains all elements of varargs. The equality is determined using a 
     * {@link java.util.Comparator} implementation. If collection or varargs is empty (or null) then 
     * false is returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param comparator Comparator implementation
     * @param collection Collection of elements to verify
     * @param elements   Varargs of elements to search
     * @return           boolean containing result of verification
     */
    @SafeVarargs
    public static <T> boolean containsAll(Comparator<? super T> comparator, 
        Collection<? extends T> collection, T... elements) {

        return containsAll(comparator, collection, Arrays.asList(elements));

    }

    /**
     * Verifies if collection contains all elements of container. The equality is determined first, 
     * by mapping the collection elements to {@link java.lang.Comparable} types, and then using 
     * {@link java.lang.Comparable#compareTo(Object) compareTo} on mapped objects. This implementation 
     * is null friendly, if container contains a null reference, collection must also contain at least one 
     * null reference for the verification. If collection or container is empty (or null) then false is 
     * returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param <U>        The type mapped by the identity mapper
     * @param identity   The mapper function
     * @param collection Collection of elements to verify
     * @param container  Collection of elements to search
     * @return           boolean containing result of verification
     */
    public static <T, U extends Comparable<? super U>> boolean containsAll(
        Function<? super T, ? extends U> identity,
        Collection<? extends T> collection, 
        Collection<? extends T> container
    ) {

        return containsAll(nullFirstComparator(identity), collection, container);

    }

    /**
     * Verifies if collection contains all elements of varargs. The equality is determined first, 
     * by mapping the collection elements to {@link java.lang.Comparable} types, and then using 
     * {@link java.lang.Comparable#compareTo(Object) compareTo} on mapped objects. This implementation 
     * is null friendly, if varargs contains a null reference, collection must also contain at least one 
     * null reference for the verification. If collection or carargs is empty (or null) then false is 
     * returned.
     * 
     * @param <T>        The type (or super type) of collections elements
     * @param <U>        The type mapped by the identity mapper
     * @param identity   The mapper function
     * @param collection Collection of elements to verify
     * @param elements   Varargs of elements to search
     * @return           boolean containing result of verification
     */
    @SafeVarargs
    public static <T, U extends Comparable<? super U>> boolean containsAll(
        Function<? super T, ? extends U> identity,
        Collection<? extends T> collection, 
        T... elements
    ) {

        return containsAll(nullFirstComparator(identity), collection, Arrays.asList(elements));

    }

    /**
     * Merges two collections into one collection, using a combiner to map nth element from both collection
     * using a {@link java.util.function.BiFunction} implementation. If the collections have different sizes
     * then the nth element from one collection is combined with a null reference using the combiner implementation.
     * If both collections are null then a null reference is returned.
     * 
     * @param <T>       The type (or super type) of the first collection elements
     * @param <U>       The type (or super type) of the second collection elements
     * @param <V>       The type (or super type) of combiner returned type
     * @param left      First collection to combine
     * @param right     Second collection to combine
     * @param combiner  The combiner implementation
     * @return          Collection of merged elements
     */
    public static <T, U, V> Collection<V> merge(Collection<? extends T> left, Collection<? extends U> right,
        BiFunction<? super T, ? super U, ? extends V> combiner) {

        Objects.requireNonNull(combiner);

        if(left == null && right == null) return null;
        
        Collection<V> collection = new ArrayList<>(
            orDefault(ofMappable(left, Collection::size), () -> right.size()));

        var iterL = ofMappable(left, Collection::iterator);
        var iterR = ofMappable(right, Collection::iterator);
        boolean hasNextL = false, hasNextR = false;
            
        while((hasNextL = ofMappable(iterL, Iterator::hasNext)) | 
                (hasNextR = ofMappable(iterR, Iterator::hasNext))) {

            var nextL = ofSupplied(() -> iterL.next(), hasNextL);
            var nextR = ofSupplied(() -> iterR.next(), hasNextR);

            collection.add(combiner.apply(nextL, nextR));
            
        }

        return collection;

    }

    /**
     * Concatenate varargs of collections into a single collection. If varargs is null then a null
     * reference is returned.
     * 
     * @param <T>         The type (or super type) of the first collection elements
     * @param collections Collections to concatenate
     * @return            Collection of concatenated collections
     */
    @SafeVarargs
    public static <T> Collection<T> concat(Collection<? extends T>... collections) {

        if(collections == null) return null;

        return Stream.of(collections)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));
            
    }

    /**
     * Groups collection elements that are mapped to the same object returned by the identity mapper,
     * into a list, then, maps the mapped object to the list of elements using a {@link java.util.Map}.
     * The identity mapper return type must implement (or inherit from) the {@link java.lang.Comparable} 
     * interface. If collections varargs is null or empty, then an emmpty map is returned.
     * 
     * @param <T>         The type (or super type) of the first collection elements
     * @param <U>         The type mapped by the identity mapper
     * @param identity    The mapper function
     * @param collections Collections to group
     * @return            Map of grouped elements value by mapped object key
     * @throws NullPointerException if the mapper tries to map a null key to a null value
     */
    @SafeVarargs
    public static <T, U extends Comparable<? super U>> Map<U, List<T>> groupByIdentity(
        Function<? super T, ? extends U> identity,
        Collection<? extends T>... collections
    ) throws NullPointerException {

        return Stream.ofNullable(concat(collections))
            .flatMap(Collection::stream)
            .collect(Collectors.groupingBy(identity, 
                () -> new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder())), 
                Collectors.toCollection(ArrayList::new)));

    }

    /**
     * Transforms a comparator into a null friendly one. A null reference is considered the lowest
     * value possible by the returned comparator.
     * 
     * @param <T>        The type (or super type) of the comparator
     * @param comparator Comparator implementation
     * @return           Comparator that is null friendly
     */
    public static <T> Comparator<T> nullFirstComparator(Comparator<? super T> comparator) {
        return Comparator.nullsFirst(comparator);
    }

    /**
     * Returns a null friendly comparator of a mapper {@link java.util.function.Function}.
     * A null reference is considered the lowest value possible by the returned comparator.
     * The mapper returned type  must implement (or inherit from) the {@link java.lang.Comparable} 
     * interface. The equality is determined using {@link java.lang.Comparable#compareTo(Object) compareTo} 
     * method on mapped objects
     * 
     * @param <T>      The type (or super type) of the comparator
     * @param <U>
     * @param identity
     * @return
     */
    public static <T, U extends Comparable<? super U>> Comparator<T> nullFirstComparator(
        Function<? super T, ? extends U> identity) {

        return nullFirstComparator(Comparator.comparing(identity));

    }

    /**
     * Transforms a comparator into a null friendly one. A null reference is considered the highest
     * value possible by the returned comparator.
     * 
     * @param <T>        The type (or super type) of the comparator
     * @param comparator Comparator implementation
     * @return           Comparator that is null friendly
     */
    public static <T> Comparator<T> nullLastComparator(Comparator<? super T> comparator) {
        return Comparator.nullsLast(comparator);
    }

    /**
     * Returns a null friendly comparator of a mapper {@link java.util.function.Function}.
     * A null reference is considered the highest value possible by the returned comparator.
     * The mapper returned type  must implement (or inherit from) the {@link java.lang.Comparable} 
     * interface. The equality is determined using {@link java.lang.Comparable#compareTo(Object) compareTo} 
     * method on mapped objects
     * 
     * @param <T>      The type (or super type) of the comparator
     * @param <U>      The type mapped by the identity mapper
     * @param identity The mapper
     * @return
     */
    public static <T, U extends Comparable<? super U>> Comparator<T> nullLastComparator(
        Function<? super T, ? extends U> identity) {

        return nullLastComparator(Comparator.comparing(identity));

    }

    /**
     * Returns a mutable shallow copy of the input collection. 
     * 
     * @param <T>        The type (or super type) of the collection elements
     * @param collection Collection to copy
     * @return           Mutable copy of input collection
     */
    public static <T> Collection<T> mutableCopyOf(Collection<? extends T> collection) {

        return mapElements(collection, Function.identity());
        
    }

    /**
     * Returns a shallow copy of mapped elements of input collection using a 
     * {@link java.util.function.Function} implementation mapper. If the input
     * collection is null then a null reference is returned.
     * 
     * @param <T>        The type (or super type) of the collection elements
     * @param <U>        The type mapped by the identity mapper
     * @param collection Collection to map
     * @param mapper     The mapper function
     * @return           Collection of mapped elements
    */
    public static <T, U> Collection<U> mapElements(Collection<? extends T> collection, 
        Function<? super T, ? extends U> mapper) {

        if(collection == null) return null;

        return collection.stream()
            .map(mapper)
            .collect(Collectors.toCollection(ArrayList::new));
            
    }

}
