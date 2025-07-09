package com.jigubangbang.com_service.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jigubangbang.com_service.model.CityDto;
import com.jigubangbang.com_service.model.CountryDto;

@Mapper
public interface TravelmateMapper {
    //검색
    List<CountryDto> selectAllCountries();
    List<CityDto> selectCitiesByCountryId(String countryId);
}
