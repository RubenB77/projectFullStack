package com.app.springBack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.springBack.model.Address;


public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findByAddressName(String categoryName);
    
}
