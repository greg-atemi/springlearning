package com.magrega.demo.dto.address;

import lombok.Data;

@Data
public class CreateAddressDTO {
    private String mapsPin;
    private String county;
    private String country;
    private String cityTown;
    private String localityArea;
}
