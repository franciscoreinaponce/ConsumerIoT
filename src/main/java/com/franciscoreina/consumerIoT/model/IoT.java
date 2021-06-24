package com.franciscoreina.consumerIoT.model;

import com.franciscoreina.consumerIoT.constants.enumType.Battery;
import com.franciscoreina.consumerIoT.constants.enumType.ProductId;
import com.franciscoreina.consumerIoT.constants.enumType.ProductNames;
import com.franciscoreina.consumerIoT.converter.csv.DecimalToInteger;
import com.franciscoreina.consumerIoT.converter.csv.OnOffToOptionalBoolean;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvNumber;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Optional;

import static com.franciscoreina.consumerIoT.constants.Constants.COLON;

@Data
public class IoT {

    @CsvBindByName(column = "DateTime", required = true)
    private long dateTime;

    @CsvBindByName(column = "EventId", required = true)
    private long eventId;

    @CsvBindByName(column = "ProductId", required = true)
    private String productId;

    @CsvBindByName(column = "Latitude")
    @CsvNumber(COLON)
    private BigDecimal latitude;

    @CsvBindByName(column = "Longitude")
    @CsvNumber(COLON)
    private BigDecimal longitude;

    @CsvCustomBindByName(column = "Battery", required = true, converter = DecimalToInteger.class)
    private Integer battery;

    @CsvCustomBindByName(column = "Light", required = true, converter = OnOffToOptionalBoolean.class)
    private Optional<Boolean> light;

    @CsvCustomBindByName(column = "AirplaneMode", required = true, converter = OnOffToOptionalBoolean.class)
    private Optional<Boolean> airplaneMode;

    public boolean isGpsDataAvailable() {
        return this.latitude != null && this.longitude != null;
    }

    public String getBatteryLife() {
        String status = Battery.BATTERY_CRITICAL.toString();

        if (this.battery >= 98) {
            status = Battery.BATTERY_FULL.toString();
        } else if (this.battery >= 60) {
            status = Battery.BATTERY_HIGH.toString();
        } else if (this.battery >= 40) {
            status = Battery.BATTERY_MEDIUM.toString();
        } else if (this.battery >= 10) {
            status = Battery.BATTERY_LOW.toString();
        }

        return status;
    }

    public String getTrackerName() {
        String productName = ProductNames.UNKNOWN_TRACKER.toString();

        if (this.productId.startsWith(ProductId.CPT_START_WITH.toString())) {
            productName = ProductNames.CYCLE_PLUS_TRACKER.toString();
        } else if (this.productId.startsWith(ProductId.GT_START_WITH.toString())) {
            productName = ProductNames.GENERAL_TRACKER.toString();
        }

        return productName;
    }

}
