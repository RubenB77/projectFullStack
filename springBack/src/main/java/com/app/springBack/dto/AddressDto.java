package com.app.springBack.dto;

import com.app.springBack.model.Address;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class AddressDto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int addressId;

    @Column(unique=true)
    String addressName;

    int postCode; 

    String city;

    double latitude;
    
    double longitude;

    public AddressDto() {}

    public AddressDto(int addressId, String addressName, int postCode, String city, double latitude, double longitude) {
        this.addressId = addressId;
        this.addressName = addressName;
        this.postCode = postCode;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AddressDto(Address address) {
        this.addressId = address.getAddressId();
        this.addressName = address.getAddressName();
        this.postCode = address.getPostCode();
        this.city = address.getCity();
        this.latitude = address.getlatitude();
        this.longitude = address.getLongitude();
    }

    public int getAddressId() {
        return this.addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return this.addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public int getPostCode() {
        return this.postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getlatitude() {
        return this.latitude;
    }

    public void setlatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
