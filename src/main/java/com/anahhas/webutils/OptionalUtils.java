package com.anahhas.webutils;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utility methods to handle {@link java.util.Optional}.
 * @author Ahmed Amin Nahhas
 */
public class OptionalUtils {

    /**
     * Returns first parameter if not null. Or else, returns a default value
     * returned by the implementation of {@link java.util.function.Supplier}.
     * 
     * @param <T>         Type of the generic object
     * @param nullable    Nullable object
     * @param defSupplier Supplier of default value
     * @return            Object or default value
     */
    public static <T> T orDefault(T nullable, Supplier<? extends T> defSupplier) {
        return Optional.ofNullable(nullable).orElseGet(defSupplier);
    }

    /**
     * Returns object if not null. Or else throws {@link java.util.NoSuchElementException}.
     * 
     * @param <T>      Type of the generic object
     * @param nullable Nullable object
     * @return         Object if not null
     * @throws NoSuchElementException if object is null
     */
    public static <T> T orThrow(T nullable) throws NoSuchElementException {
        return Optional.ofNullable(nullable).orElseThrow();
    }

    /**
     * Returns object if not null. Or else throws {@link java.lang.RuntimeException}.
     * 
     * @param <T>        Type of the generic object
     * @param nullable   Nullable object
     * @param exSupplier Supplier of the {@link java.lang.RuntimeException} to throw
     * @return           Object if not null
     * @throws RuntimeException if object is null
     */
    public static <T> T orThrow(T nullable, Supplier<? extends RuntimeException> exSupplier) throws RuntimeException {
        return Optional.ofNullable(nullable).orElseThrow(exSupplier);
    }

    /**
     * Returns the non blank {@link java.lang.CharSequence}, or else a null reference is returned. 
     * 
     * @param <T> Type of the generic object (extends CharSequence)
     * @param str CharSequence to verify
     * @return    CharSequence if not blank, or else null
     */
    public static <T extends CharSequence> T ofBlank(T str) {
        return Optional.ofNullable(str)
            .filter(StringUtils::isNotBlank)
            .orElse(null);
    }

    /**
     * Maps non null object using {@link java.util.function.Function} implementation & returns result.
     * If object is null, a null reference is returned.
     * 
     * @param <T>     Type of the generic object
     * @param <U>     The type mapped by the identity mapper
     * @param element Object to map
     * @param mapper  The mapper function
     * @return        Mapped object
     */
    public static <T, U> U ofMappable(T element, Function<? super T, ? extends U> mapper) {

        return Optional.ofNullable(element)
            .map(mapper)
            .orElse(null);

    }

    /**
     * Returns object if the condition is truthful, or else, null is returned.
     * 
     * @param <T>       Type of the generic object
     * @param element   Nullable object
     * @param condition Boolean simulating condition
     * @return          Object if condition is true
     */
    public static <T> T ofTruthy(T element, boolean condition) {
        return ofTruthy(element, t -> condition);
    }

    /**
     * Returns object if the {@link java.util.function.Predicate} implementation returns true.
     * Or else, null is returned.
     * 
     * @param <T>       Type of the generic object
     * @param element   Nullable object
     * @param predicate Predicate simulating condition
     * @return          Object if predicate is true
     */
    public static <T> T ofTruthy(T element, Predicate<? super T> predicate) {

        return Optional.ofNullable(element)
            .filter(predicate)
            .orElse(null);
            
    }

    /**
     * Returns supplied object if the condition is truthful, or else, null is returned.
     * 
     * @param <T>             Type of the generic object
     * @param elementSupplier Object {@link java.util.function.Supplier}
     * @param condition       Boolean simulating condition
     * @return                Supplied object if condition is true
     */
    public static <T> T ofSupplied(Supplier<? extends T> elementSupplier, boolean condition) {
        return ofSupplied(elementSupplier, t -> condition);
    }

    /**
     * Returns supplied object if the {@link java.util.function.Predicate}  returns true. 
     * Or else, null is returned.
     * 
     * @param <T>             Type of the generic object
     * @param elementSupplier Object {@link java.util.function.Supplier}
     * @param predicate       Predicate simulating condition
     * @return                Supplied object if predicate is true
     */
    public static <T> T ofSupplied(Supplier<? extends T> elementSupplier, Predicate<? super T> predicate) {

        return Optional.ofNullable(elementSupplier)
            .map(Supplier::get)
            .filter(predicate)
            .orElse(null);

    }

    /**
     * Merges two {@link java.util.Optional} using a {@link java.util.function.BiFunction} implementation.
     * If one of the two Optional references is empty, then null is returned.
     * 
     * @param <T>      Type of first Optional
     * @param <U>      Type of second Optional
     * @param <R>      Type mapped by the combiner
     * @param left     First Optional
     * @param right    Second Optional
     * @param combiner Combiner to apply
     * @return         Merged result
     */
    public static <T, U, R> R merge(Optional<? extends T> left, Optional<? extends U> right, 
        BiFunction<? super T, ? super U, ? extends R> combiner) {

        return left.flatMap((final var first) -> 
            right.map(second -> combiner.apply(first, second)))
                .orElse(null);

    }

    
}
