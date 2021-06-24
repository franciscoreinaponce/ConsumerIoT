package com.franciscoreina.consumerIoT.converter.csv;

import com.franciscoreina.consumerIoT.constants.enumType.SwitchModes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OnOffToOptionalBooleanTest {

    private static OnOffToOptionalBoolean onfOnOffToOptionalBoolean;

    @BeforeAll
    public static void setUp() {
        onfOnOffToOptionalBoolean = new OnOffToOptionalBoolean();
    }

    @Test
    public void convert_onStringInput_optionalAsTrueOutput() {
        // When
        Optional<Boolean> optional = onfOnOffToOptionalBoolean.convert(SwitchModes.ON.toString());

        // Then
        assertThat(optional.isPresent(), is(Boolean.TRUE));
        assertThat(optional.get(), is(Boolean.TRUE));
    }

    @Test
    public void convert_offStringInput_optionalAsFalseOutput() {
        // When
        Optional<Boolean> optional = onfOnOffToOptionalBoolean.convert(SwitchModes.OFF.toString());

        // Then
        assertThat(optional.isPresent(), is(Boolean.TRUE));
        assertThat(optional.get(), is(Boolean.FALSE));
    }

    @Test
    public void convert_naStringInput_optionalAsEmptyOutput() {
        // When
        Optional<Boolean> optional = onfOnOffToOptionalBoolean.convert(SwitchModes.NA.toString());

        // Then
        assertThat(optional.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void convert_nullStringInput_nullPointerException() {
        // When
        assertThrows(NullPointerException.class, () -> onfOnOffToOptionalBoolean.convert(null));
    }

}
