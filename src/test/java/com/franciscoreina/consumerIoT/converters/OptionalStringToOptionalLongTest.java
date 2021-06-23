package com.franciscoreina.consumerIoT.converters;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.OptionalLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionalStringToOptionalLongTest {

    @Test
    public void convert_validOptionalStringInput_validOptionalLongOutput() {
        // When
        OptionalLong optionalLong = OptionalStringToOptionalLong.convert(Optional.of("1582605259000"));

        // Then
        assertThat(optionalLong, notNullValue());
        assertThat(optionalLong, is(OptionalLong.of(1582605259000L)));
    }

    @Test
    public void convert_emptyOptionalStringInput_emptyOptionalLongOutput() {
        // When
        OptionalLong optionalLong = OptionalStringToOptionalLong.convert(Optional.empty());

        // Then
        assertThat(optionalLong, notNullValue());
        assertThat(optionalLong, is(OptionalLong.empty()));
    }

    @Test
    public void convert_invalidOptionalStringInput_numberFormatException() {
        // When
        assertThrows(NumberFormatException.class, () -> OptionalStringToOptionalLong.convert(Optional.of("A")));
    }

    @Test
    public void convert_nullOptionalStringInput_nullPointerException() {
        // When
        assertThrows(NullPointerException.class, () -> OptionalStringToOptionalLong.convert(null));
    }

}
