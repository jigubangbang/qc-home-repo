package com.jigubangbang.com_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.com_service.model.CityDto;
import com.jigubangbang.com_service.model.CountryDto;
import com.jigubangbang.com_service.repository.TravelmateMapper;

@Service
public class TravelmateService {
    @Autowired
    private TravelmateMapper travelmateMapper;

    //국가 목록 조회
    public List<CountryDto> getAllCountries() {
        return travelmateMapper.selectAllCountries();
    }

    //도시 목록 조회
    public List<CityDto> getCitiesByCountryId(String countryId) {
        return travelmateMapper.selectCitiesByCountryId(countryId);
    }
}
