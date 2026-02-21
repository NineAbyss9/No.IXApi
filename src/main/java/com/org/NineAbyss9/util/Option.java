
package com.org.NineAbyss9.util;

import com.bilibili.player_ix.noixmod_api.util.ObjectUtil;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**A util like {@linkplain Optional}
 * @author Player_IX*/
@SuppressWarnings("unused")
public final class Option<T>
implements Supplier<T> {
    /**The value of an {@linkplain Option}*/
    private T value;
    private final boolean isMutable;

    private Option(T value, boolean isMutable) {
        this.value = value;
        this.isMutable = isMutable;
    }

    private Option(T value) {
        this(value, false);
    }

    /**Returns a new empty {@linkplain Option}
     *
     * @return a new empty {@linkplain Option}*/
    @Nonnull
    public static<T> Option<T> empty() {
        return new Option<>(null);
    }

    @Nonnull
    public static <T> Option<T> of(T value) {
        return new Option<>(Objects.requireNonNull(value, "Value not present"));
    }

    @Nonnull
    public static <T> Option<T> ofNullable(T value) {
        return new Option<>(value);
    }

    @Nonnull
    public static <T> Option<T> mutable(T value) {
        return new Option<>(value, true);
    }

    /**
     * If a value is present, returns the value, otherwise throws
     * {@code NoSuchElementException}.
     *
     * @return the non-{@code null} value described by this {@code Optional}
     * @throws NoSuchElementException if no value is present
     */
    @Nonnull
    public T getNonnull() {
        if (isEmpty()) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }

    public T get() {
        return value;
    }

    /**
     * If a value is present, returns the value, otherwise returns
     * {@code null}.
     *
     * @return the value, if present, otherwise {@code other}
     */
    public T getIf(boolean flag) {
        return ifOrElse(flag, null);
    }

    /**
     * If a flag returns the value, otherwise returns
     * {@code other}.
     *
     * @param other the value to be returned, if no value is present.
     *        May be {@code null}.
     * @return the value, if present, otherwise {@code other}
     */
    public T ifOrElse(boolean flag, T other) {
        if (flag) {
            return this.value;
        } else {
            return other;
        }
    }

    public T ifOrElseThrow(boolean flag, Supplier<Throwable> exception) throws Throwable {
        if (flag) {
            return this.value;
        } else {
            throw exception.get();
        }
    }

    /**Run expresses uncheck*/
    public void run(@Nonnull Consumer<? super T> consumer) {
        consumer.accept(value);
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (isPresent()) {
            run(consumer);
        }
    }

    public void ifPresentOrElse(Consumer<? super T> consumer, Runnable runnable) {
        if (isPresent()) {
            consumer.accept(value);
        } else {
            runnable.run();
        }
    }

    public Option<T> filter(Predicate<? super T> predicate) {
        if (isEmpty()) {
            return this;
        } else {
            return predicate.test(value) ? this : Option.empty();
        }
    }

    /**
     * If a value is present, returns a sequential {@link Stream} containing
     * only that value, otherwise returns an empty {@code Stream}.
     *
     * @apiNote
     * This method can be used to transform a {@code Stream} of option
     * elements to a {@code Stream} of present value elements:
     * <pre>{@code
     *     Stream<Option<T>> os = ..
     *     Stream<T> s = os.flatMap(Option::stream)
     * }</pre>
     *
     * @return the option value as a {@code Stream}
     */
    public Stream<T> stream() {
        if (!isPresent()) {
            return Stream.empty();
        } else {
            return Stream.of(value);
        }
    }

    public boolean set(T newValue) {
        if (this.isMutable) {
            this.value = newValue;
        }
        return this.isMutable;
    }

    public Optional<T> asOptional() {
        if (this.isPresent()) {
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }

    @Nonnull
    public Supplier<T> asSupplier() {
        return () -> value;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (obj instanceof Option<?> option) {
                return ObjectUtil.nonnullEquals(value, option.value);
            } else {
                if (obj instanceof Optional<?> o) {
                    return o.filter(object -> ObjectUtil.nonnullEquals(object, value)).isPresent();
                }
                return false;
            }
        }
    }

    /**
     * Returns the hash code of the value, if present, otherwise {@code 0}
     * (zero) if no value is present.
     *
     * @return hash code value of the present value or {@code 0} if no value is
     *         present
     */
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**@return {@code  true} if a value not a number*/
    public boolean isNaN() {
        if (value instanceof Double d) {
            return d.isNaN();
        } else if (value instanceof Float f) {
            return f.isNaN();
        } else {
            return false;
        }
    }

    /**
     * Returns a non-empty string representation of this {@code Option}
     * suitable for debugging.  The exact presentation format is unspecified and
     * may vary between implementations and versions.
     *
     * @implSpec
     * If a value is present, the result must include its string representation
     * in the result.  Empty and present {@code Option}s must be unambiguously
     * differentiable.
     *
     * @return the string representation of this instance
     */
    @Nonnull
    public String toString() {
        return value != null
                ? ("Option[" + value + "]")
                : "Option.empty";
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isEmpty() {
        return value == null;
    }
}
