package com.anahhas.webutils;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OptionalUtils {

    public static <T> T orDefault(T nullable, Supplier<? extends T> defSupplier) {
        return Optional.ofNullable(nullable).orElseGet(defSupplier);
    }

    public static <T> T orThrow(T nullable) {
        return Optional.ofNullable(nullable).orElseThrow();
    }

    public static <T> T orThrow(T nullable, Supplier<? extends RuntimeException> exSupplier) {
        return Optional.ofNullable(nullable).orElseThrow(exSupplier);
    }

    public static <T extends CharSequence> T ofBlank(T str) {
        return Optional.ofNullable(str)
            .filter(StringUtils::isNotBlank)
            .orElse(null);
    }

    public static <T, U> U ofMappable(T element, Function<? super T, ? extends U> mapper) {
        return Optional.ofNullable(element).map(mapper).orElse(null);
    }

    public static <T> T ofTruthy(T element, boolean condition) {
        return ofTruthy(element, t -> condition);
    }

    public static <T> T ofTruthy(T element, Predicate<? super T> predicate) {

        return Optional.ofNullable(element)
            .filter(predicate)
            .orElse(null);
            
    }

    public static <T, U> T ofSupplied(Supplier<? extends T> elementSupplier, boolean canSupply) {
        return ofSupplied(elementSupplier, t -> canSupply);
    }

    public static <T, U> T ofSupplied(Supplier<? extends T> elementSupplier, Predicate<? super T> predicate) {

        return Optional.ofNullable(elementSupplier)
            .map(Supplier::get)
            .filter(predicate)
            .orElse(null);

    }

    public static <T, U, R> R merge(Optional<? extends T> left, Optional<? extends U> right, 
        BiFunction<? super T, ? super U, ? extends R> combiner) {

        return left.flatMap(first -> 
            right.map(second -> combiner.apply(first, second)))
                .orElse(null);

    }

    
}
