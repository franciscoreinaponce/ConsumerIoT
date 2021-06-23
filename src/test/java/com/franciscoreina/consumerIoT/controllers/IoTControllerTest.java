package com.franciscoreina.consumerIoT.controllers;

import com.franciscoreina.consumerIoT.controller.IoTController;
import com.franciscoreina.consumerIoT.converters.OptionalStringToOptionalLong;
import com.franciscoreina.consumerIoT.dto.IoTRequestDTO;
import com.franciscoreina.consumerIoT.dto.IoTResponseDTO;
import com.franciscoreina.consumerIoT.service.IoTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.management.AttributeNotFoundException;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalLong;

import static com.franciscoreina.consumerIoT.constants.Messages.HTTP_200_DATA_REFRESHED;
import static com.franciscoreina.consumerIoT.constants.Messages.HTTP_200_LOCATION_IDENTIFIED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class IoTControllerTest {

    @InjectMocks
    private IoTController ioTController;

    @Mock
    private IoTService ioTServiceMock;

    @Mock
    private IoTRequestDTO ioTRequestDTOMock;

    @Mock
    private OptionalStringToOptionalLong optionalStringToOptionalLongMock;

    /**
     * Method to Test: loadData
     * What is the Scenario: Successful service call which receives and returns valid DTO objects
     * What is the Result: Returns a valid IoTResponseDTO with the expected description
     */
    @Test
    public void loadData_successfulServiceCall_validIotResponseDto() throws FileNotFoundException {
        // Given
        IoTResponseDTO ioTResponseDTOService = IoTResponseDTO.builder().description(HTTP_200_DATA_REFRESHED).build();
        given(ioTServiceMock.loadData(ioTRequestDTOMock)).willReturn(ioTResponseDTOService);

        // When
        ResponseEntity responseEntity = ioTController.loadData(ioTRequestDTOMock);
        IoTResponseDTO ioTResponseDTO = (IoTResponseDTO) responseEntity.getBody();

        // Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getDescription(), is("data refreshed"));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Unsuccessful service call which receives a DTO but throws an exception during execution
     * What is the Result: Throws FileNotFoundException
     */
    @Test
    public void loadData_unsuccessfulServiceCall_dataNotFoundException() throws FileNotFoundException {
        // Given
        given(ioTServiceMock.loadData(ioTRequestDTOMock)).willThrow(new FileNotFoundException());

        // When
        assertThrows(FileNotFoundException.class, () -> ioTController.loadData(ioTRequestDTOMock));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Successful service call which receives a valid productId and DateTime and returns a valid DTO
     * What is the Result: Returns a valid IoTResponseDTO with the expected attributes
     */
    @Test
    public void reportDevice_successfulServiceCall_validIotResponseDto() throws AttributeNotFoundException {
        // Given
        IoTResponseDTO ioTResponseDTOService = IoTResponseDTO.builder()
                .id("6900001001")
                .name("GeneralTracker")
                .datetime("25/02/2020 04:34:18")
                .longitude("-73.935242")
                .lat("40.73071")
                .status("Active")
                .battery("Low")
                .description(HTTP_200_LOCATION_IDENTIFIED).build();
        given(ioTServiceMock.reportDevice("123", OptionalLong.of(456))).willReturn(ioTResponseDTOService);

        // When
        ResponseEntity responseEntity = ioTController.reportDevice("123", Optional.of("456"));
        IoTResponseDTO ioTResponseDTO = (IoTResponseDTO) responseEntity.getBody();

        // Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(ioTResponseDTO, notNullValue());
        assertThat(ioTResponseDTO.getId(), is("6900001001"));
        assertThat(ioTResponseDTO.getName(), is("GeneralTracker"));
        assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:34:18"));
        assertThat(ioTResponseDTO.getLongitude(), is("-73.935242"));
        assertThat(ioTResponseDTO.getLat(), is("40.73071"));
        assertThat(ioTResponseDTO.getStatus(), is("Active"));
        assertThat(ioTResponseDTO.getBattery(), is("Low"));
        assertThat(ioTResponseDTO.getDescription(), is("SUCCESS: Location identified."));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Unsuccessful service call which a valid productId and DateTime but throws an exception during execution
     * What is the Result: Throws NoSuchElementException
     */
    @Test
    public void reportDevice_unsuccessfulServiceCall_noSuchElementException() throws AttributeNotFoundException {
        // Given
        given(ioTServiceMock.reportDevice("123", OptionalLong.of(456))).willThrow(new NoSuchElementException());

        // When
        assertThrows(NoSuchElementException.class, () -> ioTController.reportDevice("123", Optional.of("456")));
    }

}
