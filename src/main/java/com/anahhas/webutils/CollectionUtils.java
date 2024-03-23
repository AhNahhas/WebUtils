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
 * 
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

    public static <T> boolean allMatch(Collection<? extends T> collection, Predicate<? super T> predicate) {

        if(isEmpty(collection))
            return false;
            
        return collection.stream().allMatch(predicate);
    }

    public static <T> long countMatches(Collection<? extends T> collection, Predicate<? super T> predicate) {

        return Stream.ofNullable(collection)
            .flatMap(Collection::stream)
            .filter(predicate)
            .count();

    }

    @SafeVarargs
    public static <T> Collection<T> merge(Collection<? extends T>... collections) {

        return Stream.of(collections)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));

    }

    public static <T extends Comparable<? super T>> long count(T object, Collection<? extends T> collection) {
        return count(nullFirstComparator(Comparator.naturalOrder()), object, collection);
    }

    public static <T, U extends Comparable<? super U>> long count(Function<? super T, ? extends U> identity, 
        T object, Collection<? extends T> collection) {

        return count(nullFirstComparator(identity), object, collection);
            
    }

    public static <T> long count(Comparator<? super T> comparator, T object, Collection<? extends T> collection) {

        return Stream.ofNullable(collection)
            .flatMap(Collection::stream)
            .filter((final var element) -> comparator.compare(element, object) == 0)
            .count();
            
    }

    public static <T extends Comparable<? super T>> boolean containsAny(Collection<? extends T> collection, 
        Collection<? extends T> container) {

        return containsAny(nullFirstComparator(Comparator.naturalOrder()), collection, container);

    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean containsAny(Collection<? extends T> collection, 
        T... elements) {

        return containsAny(nullFirstComparator(Comparator.naturalOrder()), collection, Arrays.asList(elements));

    }


    public static <T> boolean containsAny(Comparator<? super T> comparator, Collection<? extends T> collection, 
        final Collection<? extends T> container) {

        if(isEmpty(collection) || isEmpty(container))
            return false;

        return collection.stream()
            .anyMatch((final var element) -> container.stream()
                .anyMatch(object -> comparator.compare(element, object) == 0));

    }

    @SafeVarargs
    public static <T> boolean containsAny(Comparator<? super T> comparator, Collection<? extends T> collection, 
        T... elements) {

        return containsAny(comparator, collection, Arrays.asList(elements));

    }

    public static <T, U extends Comparable<? super U>> boolean containsAny(
        Function<? super T, ? extends U> identity,
        Collection<? extends T> collection, 
        Collection<? extends T> container
    ) {

        return containsAny(nullFirstComparator(identity), collection, container);

    }


    @SafeVarargs
    public static <T, U extends Comparable<? super U>> boolean containsAny(
        Function<? super T, ? extends U> identity,
        Collection<? extends T> collection, 
        T... elements
    ) {

        return containsAny(nullFirstComparator(identity), collection, Arrays.asList(elements));

    }

    public static <T extends Comparable<? super T>> boolean containsAll(Collection<? extends T> collection, 
        Collection<? extends T> container) {

        return containsAll(nullFirstComparator(Comparator.naturalOrder()), collection, container);

    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean containsAll(Collection<? extends T> collection, 
        T... elements) {

        return containsAll(nullFirstComparator(Comparator.naturalOrder()), collection, Arrays.asList(elements));

    }

    public static <T> boolean containsAll(final Comparator<? super T> comparator, 
        final Collection<? extends T> collection, final Collection<? extends T> container) {

        if(isEmpty(collection) || isEmpty(container))
            return false;

        return container.stream()
            .filter((final var element) -> containsAny(comparator, collection, element))
            .count() == container.size();

    }

    @SafeVarargs
    public static <T> boolean containsAll(Comparator<? super T> comparator, 
        Collection<? extends T> collection, T... elements) {

        return containsAll(comparator, collection, Arrays.asList(elements));

    }

    public static <T, U extends Comparable<? super U>> boolean containsAll(
        Function<? super T, ? extends U> identity,
        Collection<? extends T> collection, 
        Collection<? extends T> container
    ) {

        return containsAll(nullFirstComparator(identity), collection, container);

    }

    @SafeVarargs
    public static <T, U extends Comparable<? super U>> boolean containsAll(
        Function<? super T, ? extends U> identity,
        Collection<? extends T> collection, 
        T... elements
    ) {

        return containsAll(nullFirstComparator(identity), collection, Arrays.asList(elements));

    }

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

    @SafeVarargs
    public static <T> Collection<T> concat(Collection<? extends T>... collections) {

        return Stream.of(collections)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));
            
    }

    @SafeVarargs
    public static <T, U extends Comparable<? super U>> Map<U, List<T>> groupByIdentity(
        Function<? super T, ? extends U> identity,
        Collection<? extends T>... collections
    ) {

        return Stream.of(collections)
            .flatMap(Collection::stream)
            .collect(Collectors.groupingBy(identity, 
                () -> new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder())), 
                Collectors.toCollection(ArrayList::new)));

    }

    public static <T> Comparator<T> nullFirstComparator(Comparator<? super T> comparator) {
        return Comparator.nullsFirst(comparator);
    }

    public static <T, U extends Comparable<? super U>> Comparator<T> nullFirstComparator(
        Function<? super T, ? extends U> identity) {

        return nullFirstComparator(Comparator.comparing(identity));

    }

    public static <T> Comparator<T> nullLastComparator(Comparator<? super T> comparator) {
        return Comparator.nullsLast(comparator);
    }

    public static <T, U extends Comparable<? super U>> Comparator<T> nullLastComparator(
        Function<? super T, ? extends U> identity) {

        return nullLastComparator(Comparator.comparing(identity));

    }

    public static <T> Collection<T> mutableCopyOf(Collection<? extends T> collection) {

        return mapElements(collection, Function.identity());
        
    }

    public static <T, U> Collection<U> mapElements(Collection<? extends T> collection, 
        Function<? super T, ? extends U> mapper) {

        if(collection == null) return null;

        return collection.stream()
            .map(mapper)
            .collect(Collectors.toCollection(ArrayList::new));
            
    }

}
