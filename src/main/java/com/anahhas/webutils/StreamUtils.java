package com.anahhas.webutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    /**
     * Returns a {@link java.util.stream.Stream} from an {@link java.lang.Iterable}.
     * If isParallel is true, the returned Stream is parallel. Otherwise a sequential
     * Stream is returned.
     * 
     * @param <T>        Type (or super type) of Stream elements
     * @param iterable   Iterable to transform into Stream
     * @param isParallel Boolean to return a parallel or sequential Stream
     * @return           Stream from an Iterable
     */
    public static <T> Stream<T> fromIterable(Iterable<? extends T> iterable, boolean isParallel) {
        return StreamSupport.stream(iterable.spliterator(), isParallel)
            .map(Function.identity());
    }

    /**
     * Returns a sequential {@link java.util.stream.Stream} from an {@link java.lang.Iterable}.
     * 
     * @param <T>        Type (or super type) of Stream elements
     * @param iterable   Iterable to transform into Stream
     * @return           Sequential Stream from an Iterable
     */
    public static <T> Stream<T> fromIterable(Iterable<? extends T> iterable) {
        return fromIterable(iterable, false);
    }

    /**
     * Returns any element from the {@link java.util.stream.Stream}. If Stream is
     * empty, then a null reference is returned.
     * 
     * @param <T>    Type (or super type) of Stream elements
     * @param stream Stream to find element from
     * @return       Any element from the Steam
     */
    public static <T> T anyElement(Stream<? extends T> stream) {
        return stream.findAny().orElse(null);
    }

    /**
     * Collects {@link java.util.stream.Stream} elements into a {@link java.util.Collection}.
     * The Collection implementation is determined using a {@link java.util.function.Supplier}
     * implementation. If the Stream is empty, the Collection returned is also empty.
     * 
     * @param <T>                Type (or super type) of Stream elements
     * @param <C>                Type of Collection used to collect Stream elements
     * @param stream             Stream to collect
     * @param collectionSupplier Supplier of the collector collection
     * @return                   Collection of Stream elements
     */
    public static <T, C extends Collection<T>> C toCollection(Stream<? extends T> stream,
        Supplier<? extends C> collectionSupplier) {

        return stream.collect(Collectors.toCollection(collectionSupplier));

    }

    /**
     * Collects {@link java.util.stream.Stream} elements into a mutable {@link java.util.List}.
     * The List implementation used is an {@link java.util.ArrayList}. If the Stream is empty, 
     * the List returned is also empty.
     * 
     * @param <T>    Type (or super type) of Stream elements
     * @param stream Stream to collect
     * @return       List of Stream elements
     */
    public static <T> List<T> toList(Stream<? extends T> stream) {
        return toCollection(stream, ArrayList::new);
    }

    /**
     * Maps {@link java.util.stream.Stream} elements into ints, then sums the results.
     * If Stream is empty, then 0 is returned.
     * 
     * @param <T>    Type (or super type) of Stream elements
     * @param stream Stream to sum
     * @param mapper Mapper to transform a Stream element to an int
     * @return       Sum of mapped Stream elements
     */
    public static <T> long sum(Stream<? extends T> stream, ToIntFunction<? super T> mapper) {
        return stream.mapToInt(mapper).sum();
    }

    /**
     * Maps {@link java.util.stream.Stream} elements into ints, then calculates the average.
     * If Stream is empty, then 0.0 is returned.
     * 
     * @param <T>    Type (or super type) of Stream elements
     * @param stream Stream which average to calculate
     * @param mapper Mapper to transform a Stream element to an int
     * @return       Average of mapped Stream elements
     */
    public static <T> double average(Stream<? extends T> stream, ToIntFunction<? super T> mapper) {

        return stream.mapToInt(mapper)
            .average()
            .orElse(0);

    }

    /**
     * Returns a {@link java.util.function.Predicate} that filters out duplicate elements.
     * {@link java.util.stream.Stream} elements must implement (or inherit from) the 
     * {@link java.lang.Comparable} interface. The Predicate returned is null-friendly,
     * if the Stream contains multiple null references, then only one won't be filtered. 
     * For custom comparison, use {@link #filterDuplicate(java.util.Comparator)}.
     * 
     * @param <T> Type (or super type) of Stream elements
     * @return    Null-friendly Predicate that filters out duplicates
     */
    public static <T extends Comparable<? super T>> Predicate<T> filterDuplicate() {
        return filterDuplicate(CollectionUtils.nullFirstComparator());
    }

    /**
     * Returns a {@link java.util.function.Predicate} that filters out duplicate elements.
     * The comparison is determined using a {@link java.util.Comparator} implementation.
     * 
     * @param <T>        Type (or super type) of Stream elements
     * @param comparator The comparator implementation
     * @return           Predicate that filters out duplicates according to comparator
     */
    public static <T> Predicate<T> filterDuplicate(Comparator<? super T> comparator) {

        Map<T, Boolean> duplicateMap = new TreeMap<>(comparator);

        return (final var t) -> {

            if(duplicateMap.get(t) == null)
                return duplicateMap.put(t, true) == null;
            
            return false;

        };

    }

}
