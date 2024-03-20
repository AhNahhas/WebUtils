package com.anahhas.webutils;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
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

    public static <T extends CharSequence> T ofBlankOrDefault(T str, Supplier<? extends T> defSupplier) {
        return orDefault(ofBlank(str), defSupplier);
    }
    
    public static <T extends CharSequence> T ofBlankOrThrow(T str) {
        return orThrow(ofBlank(str));
    }

    public static <T extends CharSequence> T ofBlankOrThrow(T str, Supplier<? extends RuntimeException> exSupplier) {
        return orThrow(ofBlank(str), exSupplier);
    }

    public static <T, U> U ofMappable(T element, Function<? super T, ? extends U> mapper) {
        return Optional.ofNullable(element).map(mapper).orElse(null);
    }

    public static <T, U> U ofMappableOrThrow(T element, Function<? super T, ? extends U> mapper) {
        return orThrow(ofMappable(element, mapper));
    }

    public static <T, U> U ofMappableOrThrow(T element, Function<? super T, ? extends U> mapper, 
        Supplier<? extends RuntimeException> exSupplier) {
        
        return orThrow(ofMappable(element, mapper), exSupplier);

    }

    public static <T, U> U ofMappableOrDefault(T element, Function<? super T, ? extends U> mapper, 
        Supplier<? extends U> defSupplier) {

        return orDefault(ofMappable(element, mapper), defSupplier);

    }

    public static <T, U, R> R merge(Optional<? extends T> left, Optional<? extends U> right, 
        BiFunction<? super T, ? super U, ? extends R> combiner) {

        return left.flatMap(first -> 
            right.map(second -> combiner.apply(first, second)))
                .orElse(null);

    }

    public static <T, U, R> R mergeOrThrow(Optional<? extends T> left, Optional<? extends U> right, 
        BiFunction<? super T, ? super U, ? extends R> combiner) {

        return orThrow(merge(left, right, combiner));

    }

    public static <T, U, R> R mergeOrThrow(Optional<? extends T> left, Optional<? extends U> right, 
        BiFunction<? super T, ? super U, ? extends R> combiner, Supplier<? extends RuntimeException> exSupplier) {

        return  orThrow(merge(left, right, combiner), exSupplier);

    }

    public static <T, U, R> R mergeOrDefault(Optional<? extends T> left, Optional<? extends U> right, 
        BiFunction<? super T, ? super U, ? extends R> combiner, Supplier<? extends R> defSupplier) {

        return orDefault(merge(left, right, combiner), defSupplier);

    }
    
}
