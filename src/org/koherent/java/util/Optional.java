package org.koherent.java.util;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.koherent.java.util.function.Consumer;
import org.koherent.java.util.function.Function;
import org.koherent.java.util.function.Predicate;
import org.koherent.java.util.function.Supplier;

public final class Optional<T> {
	private static final Optional<?> EMPTY = new Optional<>(null);

	private final T value;

	private Optional(T value) {
		this.value = value;
	}

	public static <T> Optional<T> of(T value) {
		return new Optional<>(Objects.requireNonNull(value));
	}

	@SuppressWarnings("unchecked")
	public static <T> Optional<T> empty() {
		return (Optional<T>) EMPTY;
	}

	public static <T> Optional<T> ofNullable(T value) {
		return new Optional<>(value);
	}

	public T get() {
		if (value == null) {
			throw new NoSuchElementException();
		}

		return value;
	}

	public boolean isPresent() {
		return value != null;
	}

	public void ifPresent(Consumer<? super T> consumer) {
		Objects.requireNonNull(consumer);

		if (isPresent()) {
			consumer.accept(value);
		}
	}

	public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);

		if (!isPresent()) {
			return empty();
		}

		U mapped = mapper.apply(value);

		return of(mapped);
	}

	public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
		Objects.requireNonNull(mapper);

		if (!isPresent()) {
			return empty();
		}

		return Objects.requireNonNull(mapper.apply(value));
	}

	public Optional<T> filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);

		if (!isPresent()) {
			return empty();
		}

		if (!predicate.test(value)) {
			return empty();
		}

		return this;
	}

	public T orElse(T other) {
		return isPresent() ? value : other;
	}

	public T orElseGet(Supplier<? extends T> other) {
		Objects.requireNonNull(other);

		return isPresent() ? value : other.get();
	}

	public <X extends Throwable> T orElseThrow(
			Supplier<? extends X> exceptionSupplier) throws X {
		Objects.requireNonNull(exceptionSupplier);

		if (!isPresent()) {
			throw exceptionSupplier.get();
		}

		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Optional)) {
			return false;
		}

		return Objects.equals(value, ((Optional<?>) obj).value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return isPresent() ? "Optional[" + value + "]" : "Optional.empty";
	}
}
