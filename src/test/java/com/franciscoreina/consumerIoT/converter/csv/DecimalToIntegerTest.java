package com.franciscoreina.consumerIoT.converter.csv;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DecimalToIntegerTest {

    private static DecimalToInteger decimalToInteger;

    @BeforeAll
    public static void setUp() {
        decimalToInteger = new DecimalToInteger();
    }

    @Test
    public void convert_validStringInput_validIntegerOutput() {
        // When
        Integer integer = decimalToInteger.convert("0,85");

        // Then
        assertThat(integer, notNullValue());
        assertThat(integer, is(85));
    }

    @Test
    public void convert_nullStringInput_nullPointerException() {
        // When
        assertThrows(NullPointerException.class, () -> decimalToInteger.convert(null));
    }

}
