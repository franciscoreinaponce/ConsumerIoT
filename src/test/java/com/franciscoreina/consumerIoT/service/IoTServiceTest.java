package com.franciscoreina.consumerIoT.service;

import com.franciscoreina.consumerIoT.dto.IoTRequestDTO;
import com.franciscoreina.consumerIoT.dto.IoTResponseDTO;
import com.franciscoreina.consumerIoT.model.IoT;
import com.franciscoreina.consumerIoT.repository.IoTRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class IoTServiceTest {

    @InjectMocks
    private IoTService ioTService;

    @Mock
    private IoTRepository ioTRepository;

    private static final String INVALID_PATH = ".\\src\\test\\resources\\123.csv"; // This file doesn't exists.
    private static final String INVALID_CSV = ".\\src\\test\\resources\\invalidData.csv";
    private static final String VALID_CSV = ".\\src\\test\\resources\\data.csv";

    private List<IoT> iotList;

    private void loadDataHelper() throws Exception {

    }

    @BeforeEach
    private void setup() throws Exception {
        iotList = Whitebox.invokeMethod(
                ioTService, "retrieveDataFromCsv", VALID_CSV);
        given(ioTRepository.saveData(anyList())).willReturn(iotList);
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Successful service call which receives a valid CSV file and update the IoT list
     * What is the Result: Returns a valid IoTResponseDTO with the expected description
     */

    public void loadData_validCsvFileAsInput_validIotResponseDto() throws FileNotFoundException {
        // Given
        IoTRequestDTO ioTRequestDTO = new IoTRequestDTO();
        ioTRequestDTO.setFilepath(VALID_CSV);

        // When
        IoTResponseDTO ioTResponseDTO = ioTService.loadData(ioTRequestDTO);

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getDescription(), is("data refreshed"));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Unsuccessful service call which receives an invalid CSV
     * What is the Result: Throws RuntimeException
     */

    public void loadData_parseInvalidCsv_runtimeException() {
        // Given
        IoTRequestDTO ioTRequestDTO = new IoTRequestDTO();
        ioTRequestDTO.setFilepath(INVALID_CSV);

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> ioTService.loadData(ioTRequestDTO));

        // Then
        assertThat(exception.getMessage(), is("Error capturing CSV header!"));
    }

    /**
     * EXAMPLE OF TEST ON PRIVATE METHODS USING POWERMOCK
     *
     * Method to Test: retrieveDataFromCsv
     * What is the Scenario: Successful mapping from a valid CSV file
     * What is the Result: Returns a valid IoT List
     */

    public void retrieveDataFromCsv_parseValidCsv_validIoTList() throws Exception {
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
        List<IoT> iotList = Whitebox.invokeMethod(
                ioTService, "retrieveDataFromCsv", VALID_CSV);

        // Then
        assertThat(iotList, notNullValue());
        assertThat(iotList.size(), is(14));
        assertThat(iotList.get(0), is(firstIoT));
        assertThat(iotList.get(iotList.size() - 1), is(lastIoT));
    }

    /**
     * EXAMPLE OF TEST ON PRIVATE METHODS USING POWERMOCK
     *
     * Method to Test: retrieveDataFromCsv
     * What is the Scenario: Unsuccessful mapping from an invalid CSV file
     * What is the Result: Throws RuntimeException
     */

    public void retrieveDataFromCsv_parseInvalidCsv_runtimeException() {
        // When
        Exception exception = assertThrows(RuntimeException.class, () -> Whitebox.invokeMethod(
                ioTService, "retrieveDataFromCsv", INVALID_CSV));

        // Then
        assertThat(exception.getMessage(), is("Error capturing CSV header!"));
    }

    /**
     * EXAMPLE OF TEST ON PRIVATE METHODS USING POWERMOCK
     *
     * Method to Test: retrieveDataFromCsv
     * What is the Scenario: Unsuccessful mapping from an invalid CSV file
     * What is the Result: Throws FileNotFoundException
     */

    public void retrieveDataFromCsv_parseInvalidPath_runtimeException() {
        // When
        assertThrows(FileNotFoundException.class, () -> Whitebox.invokeMethod(
                ioTService, "retrieveDataFromCsv", INVALID_PATH));
    }


    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a CyclePlusTracker device
     * with less than 3 consecutive sets of gps coordinates
     * What is the Result: Returns a valid IoTResponseDTO
     */

    public void reportDevice_reportCyclePlusDeviceWithDescriptionInactive_validIotResponseDto() throws Exception {
        // Given
        loadDataHelper();

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("WG11155638", OptionalLong.of(1582605257000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("WG11155638"));
        assertThat(ioTResponseDTO.getName(), is("CyclePlusTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:34:17"));
        assertThat(ioTResponseDTO.getLongitude(), is("-0.1736"));
        assertThat(ioTResponseDTO.getLat(), is("51.5185"));
        assertThat(ioTResponseDTO.getStatus(), is("Inactive"));
        assertThat(ioTResponseDTO.getBattery(), is("Full"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location identified."));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a CyclePlusTracker device
     * with less than 3 consecutive sets of gps coordinates
     * What is the Result: Returns a valid IoTResponseDTO
     */

    public void reportDevice_reportCyclePlusDeviceWithDescriptionNA_validIotResponseDto() throws Exception {
        // Given
        loadDataHelper();

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("WG11155638", OptionalLong.of(1582605077000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("WG11155638"));
        assertThat(ioTResponseDTO.getName(), is("CyclePlusTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:31:17"));
        assertThat(ioTResponseDTO.getLongitude(), is("-0.1736"));
        assertThat(ioTResponseDTO.getLat(), is("51.5185"));
        assertThat(ioTResponseDTO.getStatus(), is("N/A"));
        assertThat(ioTResponseDTO.getBattery(), is("Full"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location identified."));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a CyclePlusTracker device
     * with less than 3 consecutive sets of gps coordinates
     * What is the Result: Returns a valid IoTResponseDTO
     */

    public void reportDevice_reportGeneralDeviceWithDescription_validIotResponseDto() throws Exception {
        // Given
        loadDataHelper();

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("WG11155638", OptionalLong.of(1582605257000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("WG11155638"));
        assertThat(ioTResponseDTO.getName(), is("CyclePlusTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:34:17"));
        assertThat(ioTResponseDTO.getLongitude(), is("-0.1736"));
        assertThat(ioTResponseDTO.getLat(), is("51.5185"));
        assertThat(ioTResponseDTO.getStatus(), is("Inactive"));
        assertThat(ioTResponseDTO.getBattery(), is("Full"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location identified."));
    }

}
