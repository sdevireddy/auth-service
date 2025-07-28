package com.zen.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.auth.common.entity.Location;
import com.zen.auth.repository.LocationRepository;

@RestController
@RequestMapping("/locations")
public class LocationController {
    @Autowired private LocationRepository locationRepository;

    @GetMapping
    public List<Location> getLocations() {
        return locationRepository.findAll();
    }

    @PostMapping
    public Location createLocation(@RequestBody Location location) {
        return locationRepository.save(location);
    }
}
