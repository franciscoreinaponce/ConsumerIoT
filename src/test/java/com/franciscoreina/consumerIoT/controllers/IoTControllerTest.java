package com.franciscoreina.consumerIoT.controllers;

import com.franciscoreina.consumerIoT.controller.IoTController;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class IoTControllerTest {

    @InjectMocks
    private IoTController iotController;

    @Mock
    private IoTService iotServiceMock;

    @Mock
    private IoTRequestDTO iotRequestDTOMock;

    /**
     * Method to Test: loadData
     * What is the Scenario: Successful service call which receives and returns a valid DTO
     * What is the Result: Returns a valid IoTResponseDTO with the expected description
     */
    @Test
    public void loadData_successfulServiceCall_validIotResponseDto() throws FileNotFoundException {
        // Given
        IoTResponseDTO iotResponseDTOService = IoTResponseDTO.builder().description(HTTP_200_DATA_REFRESHED).build();
        given(iotServiceMock.loadData(iotRequestDTOMock)).willReturn(iotResponseDTOService);

        // When
        ResponseEntity<IoTResponseDTO> responseEntity = iotController.loadData(iotRequestDTOMock);
        IoTResponseDTO iotResponseDTO = responseEntity.getBody();

        // Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(iotResponseDTO, notNullValue());
        assertThat(iotResponseDTO.getDescription(), is("data refreshed"));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Unsuccessful service call which receives a DTO but throws an exception
     * What is the Result: FileNotFoundException thrown
     */
    @Test
    public void loadData_unsuccessfulServiceCall_dataNotFoundException() throws FileNotFoundException {
        // Given
        given(iotServiceMock.loadData(iotRequestDTOMock)).willThrow(new FileNotFoundException());

        // When
        assertThrows(FileNotFoundException.class, () -> iotController.loadData(iotRequestDTOMock));
    }

    /**
     * Method to Test: reportDevice
     * What is the Scenario: Successful service call which receives a ProductId and a DateTime and returns a valid DTO
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
        given(iotServiceMock.reportDevice(anyString(), any(OptionalLong.class))).willReturn(ioTResponseDTOService);

        // When
        ResponseEntity responseEntity = iotController.reportDevice("123", Optional.of("456"));
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
     * What is the Scenario: Unsuccessful service call which a receives a ProductId and DateTime but throws an exception
     * What is the Result: NoSuchElementException thrown
     */
    @Test
    public void reportDevice_unsuccessfulServiceCall_noSuchElementException() throws AttributeNotFoundException {
        // Given
        given(iotServiceMock.reportDevice(anyString(), any(OptionalLong.class))).willThrow(new NoSuchElementException());

        // When
        assertThrows(NoSuchElementException.class, () -> iotController.reportDevice("123", Optional.of("456")));
    }

}
