package com.store.oncommerce_web.service;


import com.store.oncommerce_web.model.Address;
import com.store.oncommerce_web.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    public List<Address> getAllAddressesByUserId(Long userId) {
        return addressRepository.findAllByUserId(userId);
    }

    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }


}