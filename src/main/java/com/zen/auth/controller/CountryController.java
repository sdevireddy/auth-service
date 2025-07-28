package com.zen.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.auth.common.entity.Country;
import com.zen.auth.repository.CountryRepository;

@RestController
@RequestMapping("/countries")
public class CountryController {
    @Autowired private CountryRepository countryRepository;

    @GetMapping
    public List<Country> getCountries() {
        return countryRepository.findAll();
    }

    @PostMapping
    public Country createCountry(@RequestBody Country country) {
        return countryRepository.save(country);
    }
}

