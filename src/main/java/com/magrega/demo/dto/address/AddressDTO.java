package com.magrega.demo.dto.address;

import lombok.Data;

@Data
public class AddressDTO {

    private String mapsPin;
    private String county;
    private String country;
    private String locality;
}
