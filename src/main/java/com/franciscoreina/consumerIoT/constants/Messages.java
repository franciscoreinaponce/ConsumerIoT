package com.franciscoreina.consumerIoT.constants;

public interface Messages {

    // Successful
    String HTTP_200_DATA_REFRESHED = "data refreshed";
    String HTTP_200_AIRPLANE_MODE_ENABLED = "SUCCESS: Location not available: Please turn off airplane mode";
    String HTTP_200_LOCATION_IDENTIFIED = "SUCCESS: Location identified.";

    // Error
    String ERROR_NO_DATA_FOUND = "ERROR: no data file found";
    String ERROR_TECHNICAL_EXCEPTION = "ERROR: A technical exception occurred";
    String ERROR_GPS_NOT_REPORTED = "ERROR: Device could not be located";
    String ERROR_ID_NOT_FOUND = "ERROR: Id <insert productId> not found";

}
