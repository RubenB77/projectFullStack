package com.app.springBack.model;

import java.util.List;

import com.app.springBack.dto.AddressDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="addresses")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int addressId;

    @Column(unique=true)
    String addressName;

    int postCode; 

    String city;
    
    double longitude;

    double latitude;

    public Address() {};

    public Address(String addressName, int postCode, String city, double longitude, double latitude) {
        this.addressName = addressName;
        this.postCode = postCode;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Address(AddressDto addressDto) {
        this.addressName = addressDto.getAddressName();
        this.city = addressDto.getCity();
        this.postCode = addressDto.getPostCode();
        this.latitude = addressDto.getlatitude();
        this.longitude = addressDto.getLongitude();
    }

    @OneToMany(mappedBy = "expenseAdress", cascade = CascadeType.ALL)
    private List<Expense> adressExpenses;

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

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getlatitude() {
        return this.latitude;
    }

    public void setlatitude(double latitude) {
        this.latitude = latitude;
    }

}
