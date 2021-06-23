package com.franciscoreina.consumerIoT.repository;

import com.franciscoreina.consumerIoT.model.IoT;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class IoTRepository {

    private List<IoT> mockIoTList = new ArrayList<>();

    public List<IoT> saveData(List<IoT> iotList) {
        mockIoTList = iotList;
        return mockIoTList;
    }

    /**
     * TODO: Add description
     * */
    public List<IoT> retrieveProductsToDate(String productId, long timeToFilter) {
        return mockIoTList.stream()
                .filter(iot -> iot.getProductId().equals(productId))
                .filter(iot -> iot.getDateTime() <= timeToFilter).collect(Collectors.toList());
    }

}
