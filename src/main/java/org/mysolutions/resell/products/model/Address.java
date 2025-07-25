package org.mysolutions.resell.products.model;

import lombok.Data;

@Data
public class Address {

    private String _id;
    private String address;
    private String pinCode;
    private double lat;
    private double lng;
}
