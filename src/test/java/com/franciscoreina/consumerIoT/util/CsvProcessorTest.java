package com.franciscoreina.consumerIoT.util;

import com.franciscoreina.consumerIoT.model.IoT;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvProcessorTest {

    private static final String INVALID_PATH = ".\\src\\test\\resources\\123.csv"; // This file doesn't exists.
    private static final String INVALID_CSV = ".\\src\\test\\resources\\invalidData.csv";
    private static final String VALID_CSV = ".\\src\\test\\resources\\data.csv";

    /**
     * Method to Test: processTrackingDevicesFile
     * What is the Scenario: Successful mapping from a CSV file
     * What is the Result: Returns a valid list of IoTs
     */
    @Test
    public void processTrackingDevicesFile_mapValidCsv_validIoTList() throws Exception {
        // Given
        IoT firstIoT = new IoT();
        firstIoT.setDateTime(1582605077000L);
        firstIoT.setEventId(10001L);
        firstIoT.setProductId("WG11155638");
        firstIoT.setLatitude(new BigDecimal("51.5185"));
        firstIoT.setLongitude(new BigDecimal("-0.1736"));
        firstIoT.setBattery(99);
        firstIoT.setLight(Optional.of(false));
        firstIoT.setAirplaneMode(Optional.of(false));

        IoT lastIoT = new IoT();
        lastIoT.setDateTime(1582612875000L);
        lastIoT.setEventId(10014L);
        lastIoT.setProductId("6900233111");
        lastIoT.setLatitude(null);
        lastIoT.setLongitude(null);
        lastIoT.setBattery(10);
        lastIoT.setLight(Optional.empty());
        lastIoT.setAirplaneMode(Optional.of(false));

        // When
        List<IoT> iotList = CsvProcessor.processTrackingDevicesFile(VALID_CSV);

        // Then
        assertThat(iotList, notNullValue());
        assertThat(iotList.size(), is(14));
        assertThat(iotList.get(0), is(firstIoT));
        assertThat(iotList.get(iotList.size() - 1), is(lastIoT));
    }

    /**
     * Method to Test: processTrackingDevicesFile
     * What is the Scenario: Unsuccessful mapping from an invalid CSV file
     * What is the Result: RuntimeException thrown
     */
    @Test
    public void processTrackingDevicesFile_mapInvalidCsv_runtimeException() {
        // When
        Exception exception = assertThrows(RuntimeException.class, () -> CsvProcessor.processTrackingDevicesFile(INVALID_CSV));

        // Then
        assertThat(exception.getMessage(), is("Error capturing CSV header!"));
    }

    /**
     * Method to Test: processTrackingDevicesFile
     * What is the Scenario: Unsuccessful mapping from an invalid file path
     * What is the Result: FileNotFoundException thrown
     */
    @Test
    public void processTrackingDevicesFile_invalidPathProvided_fileNotFoundException() {
        // When
        Exception exception = assertThrows(FileNotFoundException.class, () -> CsvProcessor.processTrackingDevicesFile(INVALID_PATH));

        //Then
        assertThat(exception.getMessage(), is(".\\src\\test\\resources\\123.csv (The system cannot find the file specified)"));
    }

}
