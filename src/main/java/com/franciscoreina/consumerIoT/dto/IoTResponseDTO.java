package com.franciscoreina.consumerIoT.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.franciscoreina.consumerIoT.constants.enumType.StatusDevice;
import com.franciscoreina.consumerIoT.model.IoT;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.franciscoreina.consumerIoT.constants.Messages.HTTP_200_AIRPLANE_MODE_ENABLED;
import static com.franciscoreina.consumerIoT.constants.Messages.HTTP_200_LOCATION_IDENTIFIED;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "datetime", "longitude", "lat", "status", "battery", "description"})
public class IoTResponseDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(IoTResponseDTO.class);

    private String id; // ProductId

    private String name;

    private String datetime;

    @JsonProperty("long")
    private String longitude;

    private String lat;

    private String status;

    private String battery;

    private String description;

    public static IoTResponseDTO from(IoT source, String status) {
        LOGGER.info("+++ Create IoTResponseDTO from IoT +++");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(source.getDateTime()), ZoneId.systemDefault());

        IoTResponseDTOBuilder builder = IoTResponseDTO.builder()
                .id(source.getProductId())
                .battery(source.getBatteryLife())
                .name(source.getTrackerName())
                .datetime(dateTime.format(dtf));

        if (source.getAirplaneMode().isPresent() && source.getAirplaneMode().get().equals(Boolean.FALSE)) {
            LOGGER.info("+++ AirplaneMode as False +++");
            builder = builder
                    .longitude(source.getLongitude().toString())
                    .lat(source.getLatitude().toString())
                    .status(StatusDevice.STATUS_ACTIVE.toString())
                    .description(HTTP_200_LOCATION_IDENTIFIED);
        } else {
            LOGGER.info("+++ AirplaneMode as True +++");
            builder = builder
                    .status(StatusDevice.STATUS_INACTIVE.toString())
                    .description(HTTP_200_AIRPLANE_MODE_ENABLED);
        }

        // Override the status
        if (status != null) {
            builder = builder.status(status);
        }

        return builder.build();
    }

}
