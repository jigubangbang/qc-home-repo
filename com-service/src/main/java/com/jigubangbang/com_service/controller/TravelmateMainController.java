package com.jigubangbang.com_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.com_service.model.CityDto;
import com.jigubangbang.com_service.model.CountryDto;
import com.jigubangbang.com_service.service.TravelmateService;

@RestController
@RequestMapping("/com")
public class TravelmateMainController {
    @Autowired
    private TravelmateService travelmateService;

    //국가 목록 조회
    @GetMapping("/countries")
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        try {
            System.out.println("getAllCountries 메서드 호출됨");
            List<CountryDto> countries = travelmateService.getAllCountries();
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            System.err.println("에러 발생: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    //도시 목록 조회
    @GetMapping("/cities/{countryId}")
    public ResponseEntity<List<CityDto>> getCitiesByCountryId(@PathVariable String countryId) {
        try {
            List<CityDto> cities = travelmateService.getCitiesByCountryId(countryId);
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
