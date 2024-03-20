package com.anahhas.webutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <T, U extends Comparable<? super U>> Collection<T> distinctBy(
        Collection<? extends T> collection, 
        Function<? super T, ? extends U> identityMapper
    ) {

        Map<U, T> collected = collection.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(identityMapper, 
                Function.identity(), (u, v) -> u, TreeMap::new));

        return collected.values();

    }

    public static <T, V, U extends Comparable<? super U>> Collection<V> distinctBy(
        Collection<? extends T> collection, 
        Function<? super T, ? extends U> identityMapper, 
        Function<? super T, ? extends V> mapper
    ) {

        return distinctBy(collection, identityMapper).stream()
            .map(mapper)
            .collect(Collectors.toCollection(ArrayList::new));

    }

    public static <T, U> Collection<T> distinctBy(
        Collection<? extends T> collection, 
        Function<? super T, ? extends U> keyExtractor,
        Comparator<? super U> comparator
    ) {

        Map<U, T> collected = collection.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(keyExtractor, 
                Function.identity(), (u, v) -> u, () -> new TreeMap<>(comparator)));

        return collected.values();

    }

    @SafeVarargs
    public static <T, U extends Comparable<? super U>> Collection<T> outerJoin(
        Function<? super T, ? extends U> identityMapper, 
        Collection<? extends T>... collections
    ) {
        
        Map<U, List<T>> groupMap = Stream.of(collections)
            .flatMap(Collection::stream)
            .collect(Collectors.groupingBy(identityMapper));

        groupMap.entrySet()
            .removeIf(entry -> entry.getValue() == null || entry.getValue().size() != 1);

        return groupMap.values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));

    }

    @SafeVarargs
    public static <T, U extends Comparable<? super U>> Collection<T> innerJoin(
        Function<? super T, ? extends U> identityMapper, 
        Collection<? extends T>... collections
    ) {
        
        Map<U, List<T>> groupMap = Stream.of(collections)
            .flatMap(Collection::stream)
            .collect(Collectors.groupingBy(identityMapper));

        groupMap.entrySet()
            .removeIf(entry -> entry.getValue() == null || entry.getValue().size() <= 1);

        return groupMap.values().stream()
            .map(collection -> List.copyOf(collection).subList(0, 1))
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));

    }

    public static <T extends Comparable<? super T>> T maxOf(Collection<? extends T> collection) {

        return collection.stream()
            .collect(Collectors.maxBy(Comparator.naturalOrder()))
            .orElse(null);

    }

    public static <T extends Comparable<? super T>> T minOf(Collection<? extends T> collection) {

        return collection.stream()
            .collect(Collectors.minBy(Comparator.naturalOrder()))
            .orElse(null);

    }

    public static <T> T maxOf(Collection<? extends T> collection, Comparator<? super T> comparator) {

        return collection.stream()
            .collect(Collectors.maxBy(comparator))
            .orElse(null);

    }

    public static <T> T minOf(Collection<? extends T> collection, Comparator<? super T> comparator) {

        return collection.stream()
            .collect(Collectors.minBy(comparator))
            .orElse(null);

    }

    public static <T> Collection<T> filter(Collection<? extends T> collection, Predicate<? super T> predicate) {

        return collection.stream()
            .filter(predicate)
            .collect(Collectors.toCollection(ArrayList::new));

    }

    public static <T> boolean allMatch(Collection<? extends T> collection, Predicate<? super T> predicate) {
        return filter(collection, predicate).size() == collection.size();
    }

    public static <T> long countMatches(Collection<? extends T> collection, Predicate<? super T> predicate) {
        return filter(collection, predicate).size();
    }

    @SafeVarargs
    public static <T> Collection<T> merge(Collection<? extends T>... collections) {

        return Stream.of(collections)
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));

    }

    public static long count(Object object, Collection<?> collection) {

        return collection.stream()
            .filter((final var element) -> Objects.equals(element, object))
            .count();

    }

    public static <T> long count(T object, Collection<? extends T> collection, Comparator<? super T> comparator) {

        return collection.stream()
            .filter((final var element) -> comparator.compare(element, object) == 0)
            .count();
            
    }

    public static <T extends Comparable<? super T>> boolean containsAny(Collection<? extends T> collection, 
        Collection<? extends T> container) {

        return collection.stream()
            .anyMatch((final var element) -> container.stream()
                .anyMatch(object -> element.compareTo(object) == 0));

    }

    public static <T> boolean containsAny(Comparator<? super T> comparator, Collection<? extends T> collection, 
        Collection<? extends T> container) {

        return collection.stream()
            .anyMatch((final var element) -> container.stream()
                .anyMatch(object -> comparator.compare(element, object) == 0));

    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean containsAny(Collection<? extends T> collection, 
        T... elements) {

        return containsAny(collection, List.of(elements));

    }

    @SafeVarargs
    public static <T> boolean containsAny(Comparator<? super T> comparator, Collection<? extends T> collection, 
        T... elements) {

        return containsAny(comparator, collection, List.of(elements));

    }

    public static <T extends Comparable<? super T>> boolean containsAll(
        Collection<? extends T> collection, Collection<? extends T> container) {

        return container.stream()
            .filter((final var element) -> containsAny(collection, element))
            .count() == container.size();

    }

    public static <T> boolean containsAll(Comparator<? super T> comparator, Collection<? extends T> collection, 
        Collection<? extends T> container) {

        return container.stream()
            .filter((final var element) -> containsAny(comparator, collection, element))
            .count() == container.size();

    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean containsAll(Collection<? extends T> collection, T... elements) {

        return containsAll(collection, List.of(elements));

    }

    @SafeVarargs
    public static <T> boolean containsAll(Comparator<? super T> comparator, 
        Collection<? extends T> collection, T... elements) {

        return containsAll(comparator, collection, List.of(elements));

    }

    public static <T, U, V> Collection<V> merge(Collection<? extends T> left, Collection<? extends U> right,
        BiFunction<? super T, ? super U, ? extends V> combiner) {

        Collection<V> collection = new ArrayList<>();
        var iterL = left.iterator();
        var iterR = right.iterator();

        while(iterL.hasNext())
            collection.add(combiner.apply(iterL.next(), iterR.next()));

        return collection;

    }

    public static <T, U> Collection<U> mapElements(Collection<? extends T> collection, Function<? super T, ? extends U> mapper) {
        return collection.stream().map(mapper)
            .collect(Collectors.toCollection(ArrayList::new));
    }

}
