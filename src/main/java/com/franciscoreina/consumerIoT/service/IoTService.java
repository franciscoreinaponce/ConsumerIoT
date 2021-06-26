package com.franciscoreina.consumerIoT.service;

import com.franciscoreina.consumerIoT.constants.Messages;
import com.franciscoreina.consumerIoT.constants.enumType.ProductNames;
import com.franciscoreina.consumerIoT.constants.enumType.StatusDevice;
import com.franciscoreina.consumerIoT.dto.IoTRequestDTO;
import com.franciscoreina.consumerIoT.dto.IoTResponseDTO;
import com.franciscoreina.consumerIoT.model.IoT;
import com.franciscoreina.consumerIoT.repository.IoTRepository;
import com.franciscoreina.consumerIoT.util.CsvProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.AttributeNotFoundException;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.stream.Collectors;

@Service
public class IoTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IoTService.class);

    @Autowired
    private IoTRepository ioTRepository;

    /**
     * Given a path to a CSV file, loads the data into memory
     *
     * @param ioTRequestDTO contains the CSV filepath
     * @return IoTResponseDTO with a success  message if the upload was successful, with an error message otherwise
     */
    public IoTResponseDTO loadData(IoTRequestDTO ioTRequestDTO) throws FileNotFoundException {
        LOGGER.info("+++ Service entry +++");

        ioTRepository.saveData(CsvProcessor.processTrackingDevicesFile(ioTRequestDTO.getFilepath()));
        LOGGER.info("+++ IoT list updated +++");

        return IoTResponseDTO.builder().description(Messages.HTTP_200_DATA_REFRESHED).build();
    }

    /**
     * Given a ProductId and a DateTime, it searches for IoTs that match the ProductId
     * and have a DateTime equal to or earlier than the one provided.
     *
     * @param productId to look for
     * @param tstmp     is the DateTime to look for
     * @return IoT, if data is not found, returns an error
     */
    public IoTResponseDTO reportDevice(String productId, OptionalLong tstmp) throws AttributeNotFoundException {
        LOGGER.info("+++ Service entry +++");
        long timeToFilter = tstmp.orElse(Instant.now().toEpochMilli());

        List<IoT> iotList = ioTRepository.retrieveProductsToDate(productId, timeToFilter);
        IoT iotFound = findIotByDateTime(iotList, timeToFilter);

        checkIfDeviceIsAvailable(iotFound);

        String status = null;
        if (iotFound.getTrackerName().equals(ProductNames.CYCLE_PLUS_TRACKER.toString())) {
            LOGGER.info("+++ Device {} detected +++", ProductNames.CYCLE_PLUS_TRACKER);
            status = getStatusForCyclePlusTrackerDevices(iotList);
        }

        return IoTResponseDTO.from(iotFound, status);
    }

    /**
     * Given a IoT list and a DateTime, it searches for the IoT with a DateTime closest to the one provided.
     *
     * @param iotList      IoT to be filtered
     * @param timeToFilter data to look for
     * @return IoT if exists, otherwise it throws an exception
     * @throws NoSuchElementException if data is not found
     */
    private IoT findIotByDateTime(List<IoT> iotList, long timeToFilter) throws NoSuchElementException {
        LOGGER.info("+++ Filtering IoT list by DateTime +++");

        return iotList.stream()
                .min(Comparator.comparingLong(iot -> timeToFilter - iot.getDateTime()))
                .orElseThrow(NoSuchElementException::new);
    }

    private void checkIfDeviceIsAvailable(IoT iotFound) throws AttributeNotFoundException {
        if (iotFound.getAirplaneMode().isPresent()
                && iotFound.getAirplaneMode().get().equals(Boolean.FALSE)
                && !iotFound.isGpsDataAvailable()) {
            LOGGER.error("+++ AirplaneMode Disabled and GPS data Not Available +++");
            throw new AttributeNotFoundException();
        }
    }

    /**
     * (This functionality is only for CyclePlusTracker devices)
     * 
     * Given a IoT list returns the device status based on the GPS data retrieved.
     *
     * @param iotList of CyclePlusTracker devices
     * @return "N/A" if there are less than 3 records,
     * if there are, returns "Inactive" if the GPS data remains constant between records, "Active" if they do not match
     */
    private String getStatusForCyclePlusTrackerDevices(List<IoT> iotList) {
        LOGGER.info("+++ Get status for CyclePlusTracker device +++");

        List<IoT> iotListFiltered = iotList.stream()
                .sorted(Comparator.comparingLong(IoT::getDateTime).reversed())
                .limit(3)
                .collect(Collectors.toList());

        if (iotListFiltered.size() == 3 && iotListFiltered.stream().allMatch(IoT::isGpsDataAvailable)) {
            LOGGER.info("+++ 3 inputs with GPS data available +++");
            return areAllCoordinatesSame(iotListFiltered) ?
                    StatusDevice.STATUS_INACTIVE.toString() : StatusDevice.STATUS_ACTIVE.toString();
        } else {
            return StatusDevice.STATUS_NA.toString();
        }

    }

    private boolean areAllCoordinatesSame(List<IoT> iotList) {
        return iotList.stream().allMatch(iot -> Objects.equals(iot.getLatitude(), iotList.get(0).getLatitude())
                && Objects.equals(iot.getLongitude(), iotList.get(0).getLongitude()));
    }

}
