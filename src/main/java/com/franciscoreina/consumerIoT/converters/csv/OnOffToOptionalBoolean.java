package com.franciscoreina.consumerIoT.converters.csv;

import com.franciscoreina.consumerIoT.constants.SwitchModes;
import com.opencsv.bean.AbstractBeanField;

import java.util.Optional;

public class OnOffToOptionalBoolean extends AbstractBeanField {

    @Override
    protected Optional<Boolean> convert(String s) {
        return (s.equalsIgnoreCase(SwitchModes.ON.toString())) ? Optional.of(Boolean.TRUE) :
                ((s.equalsIgnoreCase(SwitchModes.OFF.toString())) ? Optional.of(Boolean.FALSE) : Optional.empty());
    }

}
