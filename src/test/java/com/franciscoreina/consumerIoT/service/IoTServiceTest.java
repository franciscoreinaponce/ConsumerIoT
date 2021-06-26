package com.franciscoreina.consumerIoT.service;

import com.franciscoreina.consumerIoT.dto.IoTRequestDTO;
import com.franciscoreina.consumerIoT.dto.IoTResponseDTO;
import com.franciscoreina.consumerIoT.model.IoT;
import com.franciscoreina.consumerIoT.repository.IoTRepository;
import com.franciscoreina.consumerIoT.util.CsvProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.AttributeNotFoundException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class IoTServiceTest {

    @InjectMocks
    private IoTService ioTService;

    @Mock
    private IoTRepository ioTRepository;

    private static final String INVALID_PATH = ".\\src\\test\\resources\\123.csv"; // This file doesn't exists.
    private static final String INVALID_CSV = ".\\src\\test\\resources\\invalidData.csv";
    private static final String VALID_CSV = ".\\src\\test\\resources\\data.csv";

    @BeforeAll
    public static void setUp() {
        mockStatic(CsvProcessor.class);
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Successful service call which receives a valid CSV path and updates the IoT list
     * What is the Result: Returns a IoTResponseDTO with the expected description
     */
    @Test
    public void loadData_validCsvPathAsInput_validIotResponseDto() throws FileNotFoundException {
        // Given
        IoTRequestDTO ioTRequestDTO = new IoTRequestDTO();
        ioTRequestDTO.setFilepath(VALID_CSV);
        given(CsvProcessor.processTrackingDevicesFile(anyString())).willReturn(anyList());

        // When
        IoTResponseDTO ioTResponseDTO = ioTService.loadData(ioTRequestDTO);

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getDescription(), is("data refreshed"));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Unsuccessful service call which receives an invalid CSV path
     * What is the Result: FileNotFoundException thrown
     */
    @Test
    public void loadData_parseInvalidCsv_fileNotFoundException() throws FileNotFoundException {
        // Given
        IoTRequestDTO ioTRequestDTO = new IoTRequestDTO();
        ioTRequestDTO.setFilepath(INVALID_PATH);
        given(CsvProcessor.processTrackingDevicesFile(ioTRequestDTO.getFilepath()))
                .willThrow(FileNotFoundException.class);

        // When-Then
        assertThrows(FileNotFoundException.class, () -> ioTService.loadData(ioTRequestDTO));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Unsuccessful service call which receives an invalid CSV path
     * What is the Result: RuntimeException thrown
     */
    @Test
    public void loadData_parseInvalidCsv_runtimeException() throws FileNotFoundException {
        // Given
        IoTRequestDTO ioTRequestDTO = new IoTRequestDTO();
        ioTRequestDTO.setFilepath(INVALID_CSV);
        given(CsvProcessor.processTrackingDevicesFile(ioTRequestDTO.getFilepath()))
                .willThrow(RuntimeException.class);

        // When-Then
        assertThrows(RuntimeException.class, () -> ioTService.loadData(ioTRequestDTO));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a IoT device given a ProductId and a DateTime
     * What is the Result: Returns the IoTResponseDTO expected
     */
    @Test
    public void reportDevice_retrieveIotGivenProductIdAndDateTime_validIotResponseDto() throws Exception {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582605077000L);
        iot_1.setEventId(10001);
        iot_1.setProductId("WG11155638");
        iot_1.setLatitude(new BigDecimal("51.5185"));
        iot_1.setLongitude(new BigDecimal("-0.1736"));
        iot_1.setBattery(99);
        iot_1.setLight(Optional.of(false));
        iot_1.setAirplaneMode(Optional.of(false));
        IoT iot_2 = new IoT();
        iot_2.setDateTime(1582605137000L);
        iot_2.setEventId(10002);
        iot_2.setProductId("WG11155638");
        iot_2.setLatitude(new BigDecimal("51.5185"));
        iot_2.setLongitude(new BigDecimal("-0.1736"));
        iot_2.setBattery(99);
        iot_2.setLight(Optional.of(false));
        iot_2.setAirplaneMode(Optional.of(false));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Arrays.asList(iot_1, iot_2));

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("WG11155638", OptionalLong.of(1582605137000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("WG11155638"));
        assertThat(ioTResponseDTO.getName(), is("CyclePlusTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:32:17"));
        assertThat(ioTResponseDTO.getLongitude(), is("-0.1736"));
        assertThat(ioTResponseDTO.getLat(), is("51.5185"));
        assertThat(ioTResponseDTO.getStatus(), is("N/A"));
        assertThat(ioTResponseDTO.getBattery(), is("Full"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location identified."));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: IoT not found given a non-exist ProductId and a DateTime
     * What is the Result: NoSuchElementException thrown
     */
    @Test
    public void reportDevice_iotNotFoundGivenProductIdAndDateTime_noSuchElementException() {
        // Given
        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class))).willReturn(Collections.emptyList());

        // When-Then
        assertThrows(NoSuchElementException.class,
                () -> ioTService.reportDevice("0", OptionalLong.of(1582605497000L)));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a IoT which has the airplane mode disabled and GPS Data
     * What is the Result: Returns a IoTResponseDTO with the expected description
     */
    @Test
    public void reportDevice_airplaneModeDisabledWithGpsData_validIotResponseDto() throws AttributeNotFoundException {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582605557000L);
        iot_1.setEventId(10012);
        iot_1.setProductId("6900001001");
        iot_1.setLatitude(new BigDecimal("40.73081"));
        iot_1.setLongitude(new BigDecimal("-73.935242"));
        iot_1.setBattery(10);
        iot_1.setLight(Optional.empty());
        iot_1.setAirplaneMode(Optional.of(false));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Collections.singletonList(iot_1));

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("6900001001", OptionalLong.of(1582605557000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("6900001001"));
        assertThat(ioTResponseDTO.getName(), is("GeneralTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:39:17"));
        assertThat(ioTResponseDTO.getLongitude(), is("-73.935242"));
        assertThat(ioTResponseDTO.getLat(), is("40.73081"));
        assertThat(ioTResponseDTO.getStatus(), is("Active"));
        assertThat(ioTResponseDTO.getBattery(), is("Low"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location identified."));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a IoT which has the airplane mode disabled and no GPS Data
     * What is the Result: AttributeNotFoundException thrown
     */
    @Test
    public void reportDevice_airplaneModeDisabledWithNoGpsData_validIotResponseDto() {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582612875000L);
        iot_1.setEventId(10014);
        iot_1.setProductId("6900233111");
        iot_1.setBattery(10);
        iot_1.setLight(Optional.empty());
        iot_1.setAirplaneMode(Optional.of(false));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Collections.singletonList(iot_1));

        // When-Then
        assertThrows(AttributeNotFoundException.class, () -> ioTService
                .reportDevice("6900233111", OptionalLong.of(1582605137000L)));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a IoT which has the airplane mode enabled and no GPS Data
     * What is the Result: Returns a IoTResponseDTO with the expected description
     */
    @Test
    public void reportDevice_airplaneModeEnabledWithNoGpsData_validIotResponseDto() throws AttributeNotFoundException {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582605615000L);
        iot_1.setEventId(10013);
        iot_1.setProductId("6900233111");
        iot_1.setBattery(10);
        iot_1.setLight(Optional.empty());
        iot_1.setAirplaneMode(Optional.of(true));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Collections.singletonList(iot_1));

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("6900233111", OptionalLong.of(1582605615000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("6900233111"));
        assertThat(ioTResponseDTO.getName(), is("GeneralTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:40:15"));
        assertThat(ioTResponseDTO.getStatus(), is("Inactive"));
        assertThat(ioTResponseDTO.getBattery(), is("Low"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location not available: Please turn off airplane mode"));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a IoT with the status as CyclePlusTracker
     * because the ProductId starts with WG
     * What is the Result: Returns the IoTResponseDTO expected
     */
    @Test
    public void reportDevice_retrieveCyclePlusTrackerAsStatus_validIotResponseDto1() throws Exception {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582605077000L);
        iot_1.setEventId(10001);
        iot_1.setProductId("WG11155638");
        iot_1.setLatitude(new BigDecimal("51.5185"));
        iot_1.setLongitude(new BigDecimal("-0.1736"));
        iot_1.setBattery(99);
        iot_1.setLight(Optional.of(false));
        iot_1.setAirplaneMode(Optional.of(false));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Collections.singletonList(iot_1));

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
     * What is the Scenario: Retrieve the report for a IoT with the status as GeneralTracker
     * because the ProductId starts with 69
     * What is the Result: Returns the IoTResponseDTO expected
     */
    @Test
    public void reportDevice_retrieveGeneralTrackerStatus_validIotResponseDto1() throws Exception {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582605557000L);
        iot_1.setEventId(10012);
        iot_1.setProductId("6900001001");
        iot_1.setLatitude(new BigDecimal("40.73081"));
        iot_1.setLongitude(new BigDecimal("-73.935242"));
        iot_1.setBattery(10);
        iot_1.setLight(Optional.empty());
        iot_1.setAirplaneMode(Optional.of(false));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Collections.singletonList(iot_1));

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("6900001001", OptionalLong.of(1582605557000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("6900001001"));
        assertThat(ioTResponseDTO.getName(), is("GeneralTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:39:17"));
        assertThat(ioTResponseDTO.getLongitude(), is("-73.935242"));
        assertThat(ioTResponseDTO.getLat(), is("40.73081"));
        assertThat(ioTResponseDTO.getStatus(), is("Active"));
        assertThat(ioTResponseDTO.getBattery(), is("Low"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location identified."));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a CyclePlusTracker device
     * with the status as Active since there are 3 or more datasets with different GPS coordinates
     * What is the Result: Returns a valid IoTResponseDTO
     */
    @Test
    public void reportDevice_reportCyclePlusDeviceWithStatusActive_validIotResponseDto() throws Exception {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582605197000L);
        iot_1.setEventId(10003);
        iot_1.setProductId("WG11155638");
        iot_1.setLatitude(new BigDecimal("51.5185"));
        iot_1.setLongitude(new BigDecimal("-0.1736"));
        iot_1.setBattery(98);
        iot_1.setLight(Optional.of(false));
        iot_1.setAirplaneMode(Optional.of(false));
        IoT iot_2 = new IoT();
        iot_2.setDateTime(1582605257000L);
        iot_2.setEventId(10004);
        iot_2.setProductId("WG11155638");
        iot_2.setLatitude(new BigDecimal("51.5185"));
        iot_2.setLongitude(new BigDecimal("-0.1736"));
        iot_2.setBattery(98);
        iot_2.setLight(Optional.of(false));
        iot_2.setAirplaneMode(Optional.of(false));
        IoT iot_3 = new IoT();
        iot_3.setDateTime(1582605497000L);
        iot_3.setEventId(10011);
        iot_3.setProductId("WG11155638");
        iot_3.setLatitude(new BigDecimal("51.5185"));
        iot_3.setLongitude(new BigDecimal("-0.17538"));
        iot_3.setBattery(95);
        iot_3.setLight(Optional.of(false));
        iot_3.setAirplaneMode(Optional.of(false));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Arrays.asList(iot_1, iot_2, iot_3));

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("WG11155638", OptionalLong.of(1582605257000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("WG11155638"));
        assertThat(ioTResponseDTO.getName(), is("CyclePlusTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:38:17"));
        assertThat(ioTResponseDTO.getLongitude(), is("-0.17538"));
        assertThat(ioTResponseDTO.getLat(), is("51.5185"));
        assertThat(ioTResponseDTO.getStatus(), is("Active"));
        assertThat(ioTResponseDTO.getBattery(), is("High"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location identified."));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a CyclePlusTracker device
     * with the status as Inactive since there are 3 or more datasets with the same GPS coordinates
     * What is the Result: Returns a valid IoTResponseDTO
     */
    @Test
    public void reportDevice_reportCyclePlusDeviceWithStatusInactive_validIotResponseDto() throws Exception {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582605077000L);
        iot_1.setEventId(10001);
        iot_1.setProductId("WG11155638");
        iot_1.setLatitude(new BigDecimal("51.5185"));
        iot_1.setLongitude(new BigDecimal("-0.1736"));
        iot_1.setBattery(99);
        iot_1.setLight(Optional.of(false));
        iot_1.setAirplaneMode(Optional.of(false));
        IoT iot_2 = new IoT();
        iot_2.setDateTime(1582605137000L);
        iot_2.setEventId(10002);
        iot_2.setProductId("WG11155638");
        iot_2.setLatitude(new BigDecimal("51.5185"));
        iot_2.setLongitude(new BigDecimal("-0.1736"));
        iot_2.setBattery(99);
        iot_2.setLight(Optional.of(false));
        iot_2.setAirplaneMode(Optional.of(false));
        IoT iot_3 = new IoT();
        iot_3.setDateTime(1582605197000L);
        iot_3.setEventId(10003);
        iot_3.setProductId("WG11155638");
        iot_3.setLatitude(new BigDecimal("51.5185"));
        iot_3.setLongitude(new BigDecimal("-0.1736"));
        iot_3.setBattery(98);
        iot_3.setLight(Optional.of(false));
        iot_3.setAirplaneMode(Optional.of(false));
        IoT iot_4 = new IoT();
        iot_4.setDateTime(1582605257000L);
        iot_4.setEventId(10004);
        iot_4.setProductId("WG11155638");
        iot_4.setLatitude(new BigDecimal("51.5185"));
        iot_4.setLongitude(new BigDecimal("-0.1736"));
        iot_4.setBattery(98);
        iot_4.setLight(Optional.of(false));
        iot_4.setAirplaneMode(Optional.of(false));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Arrays.asList(iot_1, iot_2, iot_3, iot_4));

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
     * with the status as N/A since there are less than 3 datasets of GPS coordinates
     * What is the Result: Returns a valid IoTResponseDTO
     */
    @Test
    public void reportDevice_reportCyclePlusDeviceWithStatusNA_validIotResponseDto() throws Exception {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582605077000L);
        iot_1.setEventId(10001);
        iot_1.setProductId("WG11155638");
        iot_1.setLatitude(new BigDecimal("51.5185"));
        iot_1.setLongitude(new BigDecimal("-0.1736"));
        iot_1.setBattery(99);
        iot_1.setLight(Optional.of(false));
        iot_1.setAirplaneMode(Optional.of(false));
        IoT iot_2 = new IoT();
        iot_2.setDateTime(1582605137000L);
        iot_2.setEventId(10002);
        iot_2.setProductId("WG11155638");
        iot_2.setLatitude(new BigDecimal("51.5185"));
        iot_2.setLongitude(new BigDecimal("-0.1736"));
        iot_2.setBattery(99);
        iot_2.setLight(Optional.of(false));
        iot_2.setAirplaneMode(Optional.of(false));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Arrays.asList(iot_1, iot_2));

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("WG11155638", OptionalLong.of(1582605137000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("WG11155638"));
        assertThat(ioTResponseDTO.getName(), is("CyclePlusTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:32:17"));
        assertThat(ioTResponseDTO.getLongitude(), is("-0.1736"));
        assertThat(ioTResponseDTO.getLat(), is("51.5185"));
        assertThat(ioTResponseDTO.getStatus(), is("N/A"));
        assertThat(ioTResponseDTO.getBattery(), is("Full"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location identified."));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a GeneralTracker device
     * with the status as Activate since the airplane mode is disabled
     * What is the Result: Returns a valid IoTResponseDTO
     */
    @Test
    public void reportDevice_reportGeneralDeviceWithStatusActive_validIotResponseDto() throws Exception {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582605257000L);
        iot_1.setEventId(10005);
        iot_1.setProductId("6900001001");
        iot_1.setLatitude(new BigDecimal("40.73061"));
        iot_1.setLongitude(new BigDecimal("-73.935242"));
        iot_1.setBattery(11);
        iot_1.setLight(Optional.empty());
        iot_1.setAirplaneMode(Optional.of(false));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Collections.singletonList(iot_1));

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("6900001001", OptionalLong.of(1582605257000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("6900001001"));
        assertThat(ioTResponseDTO.getName(), is("GeneralTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:34:17"));
        assertThat(ioTResponseDTO.getLongitude(), is("-73.935242"));
        assertThat(ioTResponseDTO.getLat(), is("40.73061"));
        assertThat(ioTResponseDTO.getStatus(), is("Active"));
        assertThat(ioTResponseDTO.getBattery(), is("Low"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location identified."));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Retrieve the report for a GeneralTracker device
     * with the status as Inactivate since the airplane mode is enabled
     * What is the Result: Returns a valid IoTResponseDTO
     */
    @Test
    public void reportDevice_reportGeneralDeviceWithStatusInactive_validIotResponseDto() throws Exception {
        // Given
        IoT iot_1 = new IoT();
        iot_1.setDateTime(1582605615000L);
        iot_1.setEventId(10013);
        iot_1.setProductId("6900233111");
        iot_1.setBattery(10);
        iot_1.setLight(Optional.empty());
        iot_1.setAirplaneMode(Optional.of(true));

        given(ioTRepository.retrieveProductsToDate(anyString(), any(Long.class)))
                .willReturn(Collections.singletonList(iot_1));

        // When
        IoTResponseDTO ioTResponseDTO = ioTService
                .reportDevice("6900233111", OptionalLong.of(1582605615000L));

        // Then
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("6900233111"));
        assertThat(ioTResponseDTO.getName(), is("GeneralTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:40:15"));
        assertThat(ioTResponseDTO.getStatus(), is("Inactive"));
        assertThat(ioTResponseDTO.getBattery(), is("Low"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location not available: Please turn off airplane mode"));
    }

}
