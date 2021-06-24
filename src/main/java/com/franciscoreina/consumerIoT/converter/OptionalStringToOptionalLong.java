package com.franciscoreina.consumerIoT.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.OptionalLong;

public class OptionalStringToOptionalLong {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionalStringToOptionalLong.class);

    public static OptionalLong convert(Optional<String> input) {
        OptionalLong inputLong = input.map(s -> OptionalLong.of(Long.parseLong(s))).orElseGet(OptionalLong::empty);
        LOGGER.info("+++ Optional<String> {} to OptionalLong {} +++", input.orElse(null), inputLong);
        return inputLong;
    }

}
