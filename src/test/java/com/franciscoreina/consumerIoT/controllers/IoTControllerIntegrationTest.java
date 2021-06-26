package com.franciscoreina.consumerIoT.controllers;

import com.franciscoreina.consumerIoT.ConsumerIoTApplication;
import com.franciscoreina.consumerIoT.dto.IoTRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(classes = ConsumerIoTApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IoTControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private IoTRequestDTO ioTRequestDTO;

    private static final String INVALID_PATH = ".\\src\\test\\resources\\123.csv"; // This file doesn't exists.
    private static final String INVALID_CSV = ".\\src\\test\\resources\\invalidData.csv";
    private static final String VALID_CSV = ".\\src\\test\\resources\\data.csv";

    @BeforeEach
    public void setUp() {
        ioTRequestDTO = new IoTRequestDTO();
    }

    private void loadDataHelper() {
        ioTRequestDTO.setFilepath(VALID_CSV);
        String url = "http://localhost:" + port + "/iot/event/v1/";
        testRestTemplate.postForEntity(url, ioTRequestDTO, String.class);
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Successful service call which receives a valid CSV path
     * What is the Result: Returns HTTP Status 200
     */
    @Test
    public void loadData_successfulServiceCall_responseHttp200() {
        // Given
        ioTRequestDTO.setFilepath(VALID_CSV);
        String url = "http://localhost:" + port + "/iot/event/v1/";

        // When
        ResponseEntity response = testRestTemplate.postForEntity(url, ioTRequestDTO, String.class);

        // Then
        assertThat(response, notNullValue());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("{\"description\":\"data refreshed\"}"));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Unsuccessful service call which receives an invalid CSV path
     * What is the Result: Returns HTTP Status 404
     */
    @Test
    public void loadData_unsuccessfulServiceCall_responseHttp404() {
        // Given
        ioTRequestDTO.setFilepath(INVALID_PATH);
        String url = "http://localhost:" + port + "/iot/event/v1/";

        // When
        ResponseEntity response = testRestTemplate.postForEntity(url, ioTRequestDTO, String.class);

        // Then
        assertThat(response, notNullValue());
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(response.getBody(), is("{\"description\":\"ERROR: no data file found\"}"));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Unsuccessful service call which receives an invalid CSV file
     * What is the Result: Returns HTTP Status 500
     */
    @Test
    public void loadData_unsuccessfulServiceCall_responseHttp500() {
        // Given
        ioTRequestDTO.setFilepath(INVALID_CSV);
        String url = "http://localhost:" + port + "/iot/event/v1/";

        // When
        ResponseEntity response = testRestTemplate.postForEntity(url, ioTRequestDTO, String.class);

        // Then
        assertThat(response, notNullValue());
        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody(), is("{\"description\":\"ERROR: A technical exception occurred\"}"));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Successful service call which receives a valid ProductId and DateTime
     * and returns the IoT data found
     * What is the Result: Returns HTTP Status 200
     */
    @Test
    public void reportDevice_successfulServiceCall_responseHttp200() {
        // Given
        loadDataHelper();
        String url = "http://localhost:" + port + "/iot/event/v1?ProductId=WG11155638&tstmp=1582605137000";
        String expected = "{\"id\":\"WG11155638\",\"name\":\"CyclePlusTracker\",\"datetime\":\"25/02/2020 04:32:17\",\"long\":\"-0.1736\",\"lat\":\"51.5185\",\"status\":\"N/A\",\"battery\":\"Full\",\"description\":\"SUCCESS: Location identified.\"}";

        // When
        ResponseEntity response = testRestTemplate.getForEntity(url, String.class);

        // Then
        assertThat(response, notNullValue());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expected));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Successful service call which receives a valid ProductId and returns the IoT data found
     * What is the Result: Returns HTTP Status 200
     */
    @Test
    public void reportDevice_successfulServiceCallWithoutTstmp_responseHttp200() {
        // Given
        loadDataHelper();
        String url = "http://localhost:" + port + "/iot/event/v1?ProductId=WG11155638";
        String expected = "{\"id\":\"WG11155638\",\"name\":\"CyclePlusTracker\",\"datetime\":\"25/02/2020 04:38:17\",\"long\":\"-0.17538\",\"lat\":\"51.5185\",\"status\":\"Active\",\"battery\":\"High\",\"description\":\"SUCCESS: Location identified.\"}";

        // When
        ResponseEntity response = testRestTemplate.getForEntity(url, String.class);

        // Then
        assertThat(response, notNullValue());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expected));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Unsuccessful service call which receives a valid ProductId and DateTime
     * but the IoT found has no location data
     * What is the Result: Returns HTTP Status 400
     */
    @Test
    public void reportDevice_unsuccessfulServiceCall_responseHttp400() {
        // Given
        loadDataHelper();
        String url = "http://localhost:" + port + "/iot/event/v1?ProductId=6900233111&tstmp=1582612875000";

        // When
        ResponseEntity response = testRestTemplate.getForEntity(url, String.class);

        // Then
        assertThat(response, notNullValue());
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(response.getBody(), is("{\"description\":\"ERROR: Device could not be located\"}"));
    }

    /**
     * Method to Test: loadData
     * What is the Scenario: Unsuccessful service call which receives a invalid ProductId
     * What is the Result: Returns HTTP Status 404
     */
    @Test
    public void reportDevice_unsuccessfulServiceCall_responseHttp404() {
        // Given
        loadDataHelper();
        String url = "http://localhost:" + port + "/iot/event/v1?ProductId=0";

        // When
        ResponseEntity response = testRestTemplate.getForEntity(url, String.class);

        // Then
        assertThat(response, notNullValue());
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(response.getBody(), is("{\"description\":\"ERROR: Id <insert productId> not found\"}"));
    }

}
