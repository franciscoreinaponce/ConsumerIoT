package com.franciscoreina.consumerIoT.repository;

import com.franciscoreina.consumerIoT.model.IoT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(MockitoExtension.class)
public class IoTRepositoryTest {

    @InjectMocks
    private IoTRepository ioTRepository;

    private List<IoT> checkMockIotListStatus() {
        try {
            Field field = IoTRepository.class.getDeclaredField("mockIoTList");
            field.setAccessible(true);
            return (List<IoT>) field.get(ioTRepository);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<IoT> createIoTList() {
        IoT iotCPT_1 = new IoT();
        iotCPT_1.setDateTime(1582605077000L);
        iotCPT_1.setEventId(10001);
        iotCPT_1.setProductId("WG11155638");
        iotCPT_1.setLatitude(new BigDecimal("51.5185"));
        iotCPT_1.setLongitude(new BigDecimal("-0.1736"));
        iotCPT_1.setBattery(99);
        iotCPT_1.setLight(Optional.of(false));
        iotCPT_1.setAirplaneMode(Optional.of(false));
        IoT iotCPT_2 = new IoT();
        iotCPT_2.setDateTime(1582605137000L);
        iotCPT_2.setEventId(10002);
        iotCPT_2.setProductId("WG11155638");
        iotCPT_2.setLatitude(new BigDecimal("51.5185"));
        iotCPT_2.setLongitude(new BigDecimal("-0.1736"));
        iotCPT_2.setBattery(99);
        iotCPT_2.setLight(Optional.of(false));
        iotCPT_2.setAirplaneMode(Optional.of(false));
        IoT iotCPT_3 = new IoT();
        iotCPT_3.setDateTime(1582605197000L);
        iotCPT_3.setEventId(10003);
        iotCPT_3.setProductId("WG11155638");
        iotCPT_3.setLatitude(new BigDecimal("51.5185"));
        iotCPT_3.setLongitude(new BigDecimal("-0.1736"));
        iotCPT_3.setBattery(98);
        iotCPT_3.setLight(Optional.of(false));
        iotCPT_3.setAirplaneMode(Optional.of(false));

        IoT iotGT_1 = new IoT();
        iotGT_1.setDateTime(1582605257000L);
        iotGT_1.setEventId(10005);
        iotGT_1.setProductId("6900001001");
        iotGT_1.setLatitude(new BigDecimal("40.73061"));
        iotGT_1.setLongitude(new BigDecimal("-73.935242"));
        iotGT_1.setBattery(11);
        iotGT_1.setLight(Optional.empty());
        iotGT_1.setAirplaneMode(Optional.of(false));
        IoT iotGT_2 = new IoT();
        iotGT_2.setDateTime(1582605258000L);
        iotGT_2.setEventId(10006);
        iotGT_2.setProductId("6900001001");
        iotGT_2.setLatitude(new BigDecimal("40.73071"));
        iotGT_2.setLongitude(new BigDecimal("-73.935242"));
        iotGT_2.setBattery(10);
        iotGT_2.setLight(Optional.empty());
        iotGT_2.setAirplaneMode(Optional.of(false));
        IoT iotGT_3 = new IoT();
        iotGT_3.setDateTime(1582605259000L);
        iotGT_3.setEventId(10007);
        iotGT_3.setProductId("6900001001");
        iotGT_3.setLatitude(new BigDecimal("40.73081"));
        iotGT_3.setLongitude(new BigDecimal("-73.935242"));
        iotGT_3.setBattery(10);
        iotGT_3.setLight(Optional.empty());
        iotGT_3.setAirplaneMode(Optional.of(false));

        return Arrays.asList(iotCPT_1, iotCPT_2, iotCPT_3, iotGT_1, iotGT_2, iotGT_3);
    }

    /**
     * Method to Test: saveData
     * What is the Scenario:Updates the stored IoT list with the new data
     * What is the Result: The stored data has been replaced with the new data supplied
     */
    @Test
    public void saveData_updateIoTListStored_dataUpdated() {
        // Given
        List<IoT> mockIoTList = checkMockIotListStatus();
        List<IoT> iotListToStore = createIoTList();

        // When
        List<IoT> mockIoTListUpdated = ioTRepository.saveData(iotListToStore);

        // Then
        assertThat(mockIoTList, notNullValue());
        assertThat(mockIoTList.size(), is(0));
        assertThat(mockIoTListUpdated, notNullValue());
        assertThat(mockIoTListUpdated.size(), is(6));
    }

    /**
     * Method to Test: saveData
     * What is the Scenario: Retrieves previous records to the given date for CyclePlusTracker products
     * What is the Result: IoT list has been filtered by retrieving the 2 expected records
     */
    @Test
    public void retrieveProductsToDate_retrieveTwoOldestCptRecords_listFilteredByData() {
        // Given
        ioTRepository.saveData(createIoTList());

        IoT iotCPT_1 = new IoT();
        iotCPT_1.setDateTime(1582605077000L);
        iotCPT_1.setEventId(10001);
        iotCPT_1.setProductId("WG11155638");
        iotCPT_1.setLatitude(new BigDecimal("51.5185"));
        iotCPT_1.setLongitude(new BigDecimal("-0.1736"));
        iotCPT_1.setBattery(99);
        iotCPT_1.setLight(Optional.of(false));
        iotCPT_1.setAirplaneMode(Optional.of(false));

        IoT iotCPT_2 = new IoT();
        iotCPT_2.setDateTime(1582605137000L);
        iotCPT_2.setEventId(10002);
        iotCPT_2.setProductId("WG11155638");
        iotCPT_2.setLatitude(new BigDecimal("51.5185"));
        iotCPT_2.setLongitude(new BigDecimal("-0.1736"));
        iotCPT_2.setBattery(99);
        iotCPT_2.setLight(Optional.of(false));
        iotCPT_2.setAirplaneMode(Optional.of(false));

        // When
        List<IoT> mockIoTListUpdated = ioTRepository.retrieveProductsToDate("WG11155638", 1582605137000L);

        // Then
        assertThat(mockIoTListUpdated, notNullValue());
        assertThat(mockIoTListUpdated.size(), is(2));
        assertThat(mockIoTListUpdated.get(0), is(iotCPT_1));
        assertThat(mockIoTListUpdated.get(1), is(iotCPT_2));
    }

}
