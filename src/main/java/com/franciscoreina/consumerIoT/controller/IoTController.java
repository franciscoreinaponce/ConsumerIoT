package com.franciscoreina.consumerIoT.controller;

import com.franciscoreina.consumerIoT.converter.OptionalStringToOptionalLong;
import com.franciscoreina.consumerIoT.dto.IoTRequestDTO;
import com.franciscoreina.consumerIoT.dto.IoTResponseDTO;
import com.franciscoreina.consumerIoT.service.IoTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.management.AttributeNotFoundException;
import java.io.FileNotFoundException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/iot")
public class IoTController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IoTController.class);

    @Autowired
    private IoTService ioTService;

    @PostMapping(path = "/event/v1/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<IoTResponseDTO> loadData(@RequestBody IoTRequestDTO ioTRequestDTO) throws FileNotFoundException {
        LOGGER.info("+++ Calling loadData +++");

        return new ResponseEntity<>(ioTService.loadData(ioTRequestDTO), HttpStatus.OK);
    }

    @GetMapping(path = "/event/v1", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<IoTResponseDTO> reportDevice(@RequestParam("ProductId") String productId,
                                                       @RequestParam Optional<String> tstmp) throws AttributeNotFoundException {
        LOGGER.info("+++ Calling reportDevice +++");

        return new ResponseEntity<>(ioTService.reportDevice(productId, OptionalStringToOptionalLong.convert(tstmp)), HttpStatus.OK);
    }

}
