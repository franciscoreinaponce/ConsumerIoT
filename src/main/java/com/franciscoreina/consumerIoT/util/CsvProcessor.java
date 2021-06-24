package com.franciscoreina.consumerIoT.util;

import com.franciscoreina.consumerIoT.model.IoT;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static com.franciscoreina.consumerIoT.constants.Constants.SEMICOLON;

public class CsvProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvProcessor.class);

    public static List<IoT> processTrackingDevicesFile(String filepath) throws FileNotFoundException {
        LOGGER.info("+++ Retrieving and mapping data from {} +++", filepath);

        return new CsvToBeanBuilder(new FileReader(filepath))
                .withSkipLines(1)
                .withSeparator(SEMICOLON)
                .withType(IoT.class)
                .build()
                .parse();
    }
    
}
