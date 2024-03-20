package com.anahhas.webutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    public static <T extends Comparable<? super T>> Collection<T> distinct(Collection<? extends T> collection) {
        return distinct(Function.identity(), collection);
    }

    public static <T, U extends Comparable<? super U>> Collection<T> distinct(
        Function<? super T, ? extends U> identity, 
        Collection<? extends T> collection
    ) {

        return distinct(Comparator.comparing(identity), collection);

    }

    public static <T> Collection<T> distinct(Comparator<? super T> comparator, Collection<? extends T> collection) {
        
        List<T> distinctList = new ArrayList<>();

        for(T element : collection)
            if(!containsAny(Comparator.nullsFirst(comparator), distinctList, element))
                distinctList.add(element);

        return distinctList;

    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> Collection<T> outerJoin(
        Collection<? extends T>... collections
    ) {
        
        return outerJoin(Function.identity(), collections);

    }

    @SafeVarargs
    public static <T, U extends Comparable<? super U>> Collection<T> outerJoin(
        Function<? super T, ? extends U> identity, 
        Collection<? extends T>... collections
    ) {
        
        Collection<T> concat = concat(collections);
        List<T> unique = new ArrayList<>(concat.size());
        Predicate<T> isUnique = (element) -> !containsAny(identity, unique, element) && 
            count(identity, element, concat) == 1;

        for(T element : concat)
            if(isUnique.test(element))
                unique.add(element);

        return unique;

    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> Collection<T> innerJoin(
        Collection<? extends T>... collections
    ) { 

        return innerJoin(Function.identity(), collections);

    }

    @SafeVarargs
    public static <T, U extends Comparable<? super U>> Collection<T> innerJoin(
        Function<? super T, ? extends U> identity, 
        Collection<? extends T>... collections
    ) {
        
        Collection<T> concat = concat(collections);
        List<T> common = new ArrayList<>(concat.size());
        Predicate<T> isCommon = (element) -> !containsAny(identity, common, element) && 
            count(identity, element, concat) > 1;

        for(T element : concat)
            if(isCommon.test(element))
                common.add(element);

        return common;

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

    public static <T extends Comparable<? super T>> T minOf(Collection<? extends T> collection) {
        return minOf(Comparator.naturalOrder(), collection);
    }

    public static <T, U extends Comparable<? super U>> T minOf(Function<? super T, ? extends U> identity,
        Collection<? extends T> collection) {

        return minOf(Comparator.comparing(identity), collection);

    }

    public static <T> T minOf(Comparator<? super T> comparator, Collection<? extends T> collection) {

        return collection.stream()
            .collect(Collectors.minBy(Comparator.nullsFirst(comparator)))
            .orElse(null);

    }

    public static <T extends Comparable<? super T>> T maxOf(Collection<? extends T> collection) {
        return maxOf(Comparator.naturalOrder(), collection);
    }

    public static <T, U extends Comparable<? super U>> T maxOf(Function<? super T, ? extends U> identity,
        Collection<? extends T> collection) {

        return maxOf(Comparator.comparing(identity), collection);

    }

    public static <T> T maxOf(Comparator<? super T> comparator, Collection<? extends T> collection) {

        return collection.stream()
            .collect(Collectors.maxBy(Comparator.nullsFirst(comparator)))
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

    public static <T extends Comparable<? super T>> long count(T object, Collection<? extends T> collection) {
        return count(Comparator.naturalOrder(), object, collection);
    }

    public static <T, U extends Comparable<? super U>> long count(Function<? super T, ? extends U> identity, 
        T object, Collection<? extends T> collection) {

        return collection.stream()
            .filter((final var element) -> Comparator.comparing(identity)
                .compare(element, object) == 0
            )
            .count();
            
    }

    public static <T> long count(Comparator<? super T> comparator, T object, Collection<? extends T> collection) {

        return collection.stream()
            .filter((final var element) -> comparator.compare(element, object) == 0)
            .count();
            
    }

    public static <T extends Comparable<? super T>> boolean containsAny(Collection<? extends T> collection, 
        Collection<? extends T> container) {

        return containsAny(Comparator.naturalOrder(), collection, container);

    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean containsAny(Collection<? extends T> collection, 
        T... elements) {

        return containsAny(collection, Arrays.asList(elements));

    }


    public static <T> boolean containsAny(Comparator<? super T> comparator, Collection<? extends T> collection, 
        Collection<? extends T> container) {

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

        return containsAny(Comparator.comparing(identity), collection, container);

    }


    @SafeVarargs
    public static <T, U extends Comparable<? super U>> boolean containsAny(
        Function<? super T, ? extends U> identity,
        Collection<? extends T> collection, 
        T... elements
    ) {

        return containsAny(identity, collection, Arrays.asList(elements));

    }

    public static <T extends Comparable<? super T>> boolean containsAll(Collection<? extends T> collection, 
        Collection<? extends T> container) {

        return containsAll(Comparator.naturalOrder(), collection, container);

    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean containsAll(Collection<? extends T> collection, 
        T... elements) {

        return containsAll(collection, Arrays.asList(elements));

    }

    public static <T> boolean containsAll(Comparator<? super T> comparator, Collection<? extends T> collection, 
        Collection<? extends T> container) {

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

        return containsAll(Comparator.comparing(identity), collection, container);

    }

    @SafeVarargs
    public static <T, U extends Comparable<? super U>> boolean containsAll(
        Function<? super T, ? extends U> identity,
        Collection<? extends T> collection, 
        T... elements
    ) {

        return containsAll(Comparator.comparing(identity), collection, Arrays.asList(elements));

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

    @SafeVarargs
    public static <T> Collection<T> concat(Collection<? extends T>... collections) {

        return Stream.of(collections)
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));
            
    }

    public static <T, U> Collection<U> mapElements(Collection<? extends T> collection, Function<? super T, ? extends U> mapper) {
        return collection.stream().map(mapper)
            .collect(Collectors.toCollection(ArrayList::new));
    }

}
