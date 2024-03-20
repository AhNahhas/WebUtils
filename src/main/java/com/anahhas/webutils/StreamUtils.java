package com.anahhas.webutils;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    public static <T> Stream<T> fromIterable(Iterable<? extends T> iterable, boolean isParallel) {
        return StreamSupport.stream(iterable.spliterator(), isParallel)
            .map(Function.identity());
    }

    public static <T> Stream<T> fromIterable(Iterable<? extends T> iterable) {
        return fromIterable(iterable, false);
    }

    public static <T> T anyElement(Stream<? extends T> stream) {
        return stream.findAny().orElse(null);
    }

    public static <T> T anyElement(Collection<? extends T> collection) {
        return anyElement(collection.stream());
    }

    public static <T> T anyElementOrThrow(Stream<? extends T> stream) {
        return stream.findAny().orElseThrow();
    }

    public static <T> T anyElementOrThrow(Collection<? extends T> collection) {
        return anyElementOrThrow(collection.stream());
    }

    public static <T> T anyElementOrThrow(Stream<? extends T> stream, Supplier<? extends RuntimeException> exSupplier) {
        return stream.findAny().orElseThrow(exSupplier);
    }

    public static <T> T anyElementOrThrow(Collection<? extends T> collection, 
        Supplier<? extends RuntimeException> exSupplier) {

        return anyElementOrThrow(collection.stream(), exSupplier);

    }

    public static <T, U> U anyElement(Stream<? extends T> stream, Function<? super T, ? extends U> mapper) {
        return stream.findAny().map(mapper).orElse(null);
    }

    public static <T, U> U anyElement(Collection<? extends T> collection, Function<? super T, ? extends U> mapper) {
        return anyElement(collection.stream(), mapper);
    }

    public static <T, U> U anyElementOrThrow(Stream<? extends T> stream, Function<? super T, ? extends U> mapper) {
        return stream.findAny().map(mapper).orElseThrow();
    }

    public static <T, U> U anyElementOrThrow(Collection<? extends T> collection, Function<? super T, ? extends U> mapper) {
        return anyElementOrThrow(collection.stream(), mapper);
    }

    public static <T, U> U anyElementOrThrow(Stream<? extends T> stream, Function<? super T, ? extends U> mapper,
        Supplier<? extends RuntimeException> exSupplier) {

        return stream.findAny().map(mapper).orElseThrow(exSupplier);

    }

    public static <T, U> U anyElementOrThrow(Collection<? extends T> collection, Function<? super T, ? extends U> mapper,
        Supplier<? extends RuntimeException> exSupplier) {

        return anyElementOrThrow(collection.stream(), mapper, exSupplier);

    }

}
