package com.franciscoreina.consumerIoT.converters.csv;

import com.opencsv.bean.AbstractBeanField;

import static com.franciscoreina.consumerIoT.constants.Constants.COLON;
import static com.franciscoreina.consumerIoT.constants.Constants.DOT;

public class DecimalToInteger extends AbstractBeanField {

    @Override
    protected Integer convert(String s) {
        return (int) (Double.parseDouble(s.replace(COLON, DOT)) * 100);
    }

}
