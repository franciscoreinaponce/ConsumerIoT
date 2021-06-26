package com.franciscoreina.consumerIoT.repository;

import com.franciscoreina.consumerIoT.model.IoT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class IoTRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(IoTRepository.class);

    private List<IoT> mockIoTList = new ArrayList<>();

    /**
     * Replaces the IoT list with the new data received
     *
     * @param iotList to store
     * @return IoT list stored
     */
    public List<IoT> saveData(List<IoT> iotList) {
        LOGGER.info("+++ Repository entry +++");

        mockIoTList = iotList;
        return mockIoTList;
    }

    /**
     * Given a ProductId and a DateTime, it searches for IoTs that match the ProductId
     * and have a DateTime equal to or earlier than the one provided.
     *
     * @param productId    to look for
     * @param timeToFilter to look for
     * @return IoT list filtered
     */
    public List<IoT> retrieveProductsToDate(String productId, long timeToFilter) {
        LOGGER.info("+++ Repository entry +++");

        return mockIoTList.stream()
                .filter(iot -> iot.getProductId().equals(productId))
                .filter(iot -> iot.getDateTime() <= timeToFilter).collect(Collectors.toList());
    }

}
